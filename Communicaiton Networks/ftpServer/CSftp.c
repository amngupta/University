#include "common.h"

// Trevor:
// TO UPPER AND PARENT DIRECTORY SECURITY
// AMAN:
// Code Clean Up

ssize_t sendBinaryFile(int out_fd, int in_fd, off_t *offset, size_t count)
{
    off_t orig;
    char buf[BUF_SIZE];
    size_t toRead, numRead, numSent, totSent;

    if (offset != NULL)
    {
        orig = lseek(in_fd, 0, SEEK_CUR);
        if (orig == -1)
            return -1;
        if (lseek(in_fd, *offset, SEEK_SET) == -1)
            return -1;
    }

    totSent = 0;
    while (count > 0)
    {
        toRead = count < BUF_SIZE ? count : BUF_SIZE;

        numRead = read(in_fd, buf, toRead);
        if (numRead == -1)
            return -1;
        if (numRead == 0)
            break; /* EOF */

        numSent = write(out_fd, buf, numRead);
        if (numSent == -1)
            return -1;
        if (numSent == 0)
        {
            perror("sendfile: write() transferred 0 bytes");
            exit(-1);
        }

        count -= numSent;
        totSent += numSent;
    }

    if (offset != NULL)
    {
        *offset = lseek(in_fd, 0, SEEK_CUR);
        if (*offset == -1)
            return -1;
        if (lseek(in_fd, orig, SEEK_SET) == -1)
            return -1;
    }
    return totSent;
}

void generatePort(Port *port)
{
    srand(time(NULL));
    port->p1 = 128 + (rand() % 64);
    port->p2 = rand() % 0xff;
}

void getIPAddr(int sock, int *ip)
{
    socklen_t addr_size = sizeof(struct sockaddr_in);
    struct sockaddr_in addr;
    getsockname(sock, (struct sockaddr *)&addr, &addr_size);

    char *host = inet_ntoa(addr.sin_addr);
    sscanf(host, "%d.%d.%d.%d", &ip[0], &ip[1], &ip[2], &ip[3]);
}

void sigchld_handler(int s)
{
    // waitpid() might overwrite errno, so we save and restore it:
    int saved_errno = errno;

    while (waitpid(-1, NULL, WNOHANG) > 0)
        ;

    errno = saved_errno;
}

void *get_in_addr(struct sockaddr *sa)
{
    if (sa->sa_family == AF_INET)
    {
        return &(((struct sockaddr_in *)sa)->sin_addr);
    }

    return &(((struct sockaddr_in6 *)sa)->sin6_addr);
}

void parse_command(char *cmdstring, Command *cmd)
{
    sscanf(cmdstring, "%s %s", cmd->command, cmd->arg);
    int i = 0;
    while (cmd->command[i]){
        cmd->command[i] =  (toupper(cmd->command[i]));
        i++;
    }
    i = 0;
    while (cmd->arg[i]){
        cmd->arg[i] = (toupper(cmd->arg[i]));
        i++;
    }
}

int create_socket(int port)
{
    int sock;
    int reuse = 1;

    /* Server addess */
    struct sockaddr_in server_address = (struct sockaddr_in){
        AF_INET,
        htons(port),
        (struct in_addr){INADDR_ANY}};

    if ((sock = socket(AF_INET, SOCK_STREAM, 0)) < 0)
    {
        fprintf(stderr, "Cannot open socket");
        exit(EXIT_FAILURE);
    }

    /* Address can be reused instantly after program exits */
    setsockopt(sock, SOL_SOCKET, SO_REUSEADDR, &reuse, sizeof reuse);

    /* Bind socket to server address */
    if (bind(sock, (struct sockaddr *)&server_address, sizeof(server_address)) < 0)
    {
        fprintf(stderr, "Cannot bind socket to address");
        exit(EXIT_FAILURE);
    }

    listen(sock, 5);
    return sock;
}

int main(int argc, char **argv)
{

    if (argc != 2)
    {
        usage(argv[0]);
        return -1;
    }

    int numbytes;
    char cwd[1024];

    char buf[MAXDATASIZE];
    int sockfd, new_fd, pasv_fd; // listen on sock_fd, new connection on new_fd
    int passive = 0;
    struct addrinfo hints, *servinfo, *p;
    struct sockaddr_storage their_addr;      // connector's address information
    struct sockaddr_storage their_pasv_addr; // connector's address information
    socklen_t sin_size;
    struct sigaction sa;
    int yes = 1;
    char s[INET6_ADDRSTRLEN];
    int rv;
    Command *cmd = malloc(sizeof(Command));

    void sendResponse(char *status)
    {
        if (send(new_fd, status, strlen(status), 0) == -1)
            perror("send");
    }

    memset(&hints, 0, sizeof hints);
    hints.ai_family = AF_UNSPEC;
    hints.ai_socktype = SOCK_STREAM;
    hints.ai_flags = AI_PASSIVE; // use my IP

    if ((rv = getaddrinfo(NULL, argv[1], &hints, &servinfo)) != 0)
    {
        fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(rv));
        return 1;
    }

    for (p = servinfo; p != NULL; p = p->ai_next)
    {
        if ((sockfd = socket(p->ai_family, p->ai_socktype,
                             p->ai_protocol)) == -1)
        {
            perror("server: socket");
            continue;
        }

        if (setsockopt(sockfd, SOL_SOCKET, SO_REUSEADDR, &yes,
                       sizeof(int)) == -1)
        {
            perror("setsockopt");
            exit(1);
        }

        if (bind(sockfd, p->ai_addr, p->ai_addrlen) == -1)
        {
            close(sockfd);
            perror("server: bind");
            continue;
        }

        break;
    }

    freeaddrinfo(servinfo); // all done with this structure

    if (p == NULL)
    {
        fprintf(stderr, "server: failed to bind\n");
        exit(1);
    }

    if (listen(sockfd, BACKLOG) == -1)
    {
        perror("listen");
        exit(1);
    }

    sa.sa_handler = sigchld_handler; // reap all dead processes
    sigemptyset(&sa.sa_mask);
    sa.sa_flags = SA_RESTART;
    if (sigaction(SIGCHLD, &sa, NULL) == -1)
    {
        perror("sigaction");
        exit(1);
    }
    char *name = (char *)malloc(20);
    printf("server: waiting for connections...\n");

    while (1)
    { // main accept() loop
        sin_size = sizeof their_addr;
        new_fd = accept(sockfd, (struct sockaddr *)&their_addr, &sin_size);
        if (new_fd == -1)
        {
            perror("accept");
            continue;
        }

        inet_ntop(their_addr.ss_family,
                  get_in_addr((struct sockaddr *)&their_addr),
                  s, sizeof s);
        printf("server: got connection from %s\n", s);

        if (!fork())
        {                  // this is the child process
            close(sockfd); // child doesn't need the listener
            //Loop here until QUIT
            sendResponse("220, Service ready for new user.\n");
            while (1)
            {

                if ((numbytes = recv(new_fd, buf, MAXDATASIZE - 1, 0)) == -1)
                {
                    perror("recv");
                    exit(1);
                }
                if (numbytes == 0) break;
                buf[numbytes] = '\0';
                parse_command(buf, cmd);
                printf("server: received %s", buf);
                
                if (strncmp(cmd->command, "QUIT", 4) == 0)
                {
                    printf("next connection\n");
                    break;
                }
                else if ((strncmp(cmd->command, "USER", 10) == 0) && (strncmp(cmd->arg, "CS317", 10) == 0))
                {
                    sendResponse("230 Login successful.\n");
                    while (1)
                    {
                        
                        if ((numbytes = recv(new_fd, buf, MAXDATASIZE - 1, 0)) == -1)
                        {
                            perror("recv");
                            exit(1);
                        }
                        else
                        {

                            buf[numbytes] = '\0';
                            printf("server: received %s", buf);
                            parse_command(buf, cmd);

                            
                            if (strncmp(cmd->command, "SYST", 4) == 0)
                            {
                                sendResponse("215 UNIX \n");
                            }
                            else if (strncmp(cmd->command, "FEAT", 4) == 0)
                            {
                                sendResponse("211\n");
                            }
                            else if (strncmp(cmd->command, "PWD", 3) == 0)
                            {
                                sendResponse("212\n");
                            }
                            else if (strncmp(cmd->command, "CWD", 3) == 0)
                            {
                                if (strstr(cmd->arg,"../")){
                                    sendResponse("550 Failed to change  directory.\n");
                                }
                                else if (strncmp(cmd->arg,"./",2) == 0){
                                    sendResponse("550 Failed to change directory.\n");   
                                }
                                else if (chdir(cmd->arg) == 0)
                                {
                                    sendResponse("250 Directory successfully changed.\n");
                                    // if (send(new_fd, "250 Directory successfully changed.\n", 37, 0) == -1)
                                    //     perror("send");
                                }
                                else
                                {
                                    sendResponse("550 Failed to change directory.\n");
                                }
                            }
                            else if (strncmp(cmd->command, "CDUP", 4) == 0)
                            {
                                if (chdir("..") == 0)
                                {
                                    sendResponse("250 Directory successfully changed.\n");
                                }
                                else
                                {
                                    sendResponse("550 Failed to change directory.\n");
                                }
                            }
                            else if (strncmp(cmd->command, "TYPE", 4) == 0)
                            {
                                if (cmd->arg[0] == 'I')
                                {
                                    sendResponse("200 Switching to Binary mode.\n");
                                }
                                else if (cmd->arg[0] == 'A')
                                {
                                    /* Type A must be always accepted according to RFC */
                                    sendResponse("200 Switching to ASCII mode.\n");
                                }
                                else
                                {
                                    sendResponse("504 Command not implemented for that parameter.\n");
                                }
                            }
                            else if (strncmp(cmd->command, "MODE", 4) == 0)
                            {
                                if (cmd->arg[0] == 'C')
                                {
                                    sendResponse("220 Mode set to C\n");
                                }
                                else if (cmd->arg[0] == 'B')
                                {
                                    sendResponse("220 Mode set to B\n");
                                }
                                else if (cmd->arg[0] == 'S')
                                {
                                    sendResponse("220 Mode set to S\n");
                                }
                                else
                                {
                                    sendResponse("504 Bad MODE command\n");
                                }
                            }
                            else if (strncmp(cmd->command, "STRU", 4) == 0)
                            {
                                if (cmd->arg[0] == 'F') 
                                {
                                    sendResponse("220 Structure set to F\n");
                                }
                                else 
                                {
                                   sendResponse("504 Bad STRU command\n"); 
                                } 
                            }
                            else if (strncmp(cmd->command, "RETR", 4) == 0)
                            {
                                int connection;
                                int fd;
                                struct stat stat_buf;
                                off_t offset = 0;
                                int sent_total = 0;
                                if (passive)
                                {
                                    if (access(cmd->arg, R_OK) == 0 && (fd = open(cmd->arg, O_RDONLY)))
                                    {
                                        fstat(fd, &stat_buf);

                                        sendResponse("150 Opening BINARY mode data connection.\n");

                                        // connection = accept_connection(pasv_fd);
                                        // close(state->sock_pasv);
                                        if (sent_total = sendBinaryFile(pasv_fd, fd, &offset, stat_buf.st_size))
                                        {

                                            if (sent_total != stat_buf.st_size)
                                            {
                                                perror("ftp_retr:sendBinaryFile");
                                                exit(EXIT_SUCCESS);
                                            }

                                            sendResponse("226 File send OK.\n");
                                        }
                                        else
                                        {
                                            sendResponse("550 Failed to read file.\n");
                                        }
                                    }
                                    else
                                    {
                                        sendResponse("550 Failed to get file\n");
                                    }
                                    passive = 0;
                                    close(pasv_fd);
                                }
                                else
                                {
                                    sendResponse("550 Please use PASV instead of PORT.\n");
                                }
                            }
                            else if (strncmp(cmd->command, "PASV", 4) == 0)
                            {
                                int ip[4];
                                char buff[255];
                                char *response = "227 Entering Passive Mode (%d,%d,%d,%d,%d,%d)\n";
                                Port *port = malloc(sizeof(Port));
                                generatePort(port);
                                getIPAddr(sockfd, ip);
                                int sock_pasv = create_socket((256 * port->p1) + port->p2);
                                printf("port: %d\n", 256 * port->p1 + port->p2);
                                sprintf(buff, response, ip[0], ip[1], ip[2], ip[3], port->p1, port->p2);
                                sendResponse(buff);
                                struct timeval tv;
                                tv.tv_sec = (long)15;
                                tv.tv_usec = 0;
                                int rc;
                                rc = select(sock_pasv + 1, NULL, NULL, NULL, &tv);
                                if (rc > 0)
                                {
                                    pasv_fd = accept(sock_pasv, (struct sockaddr *)&their_pasv_addr, &sin_size);
                                    if (pasv_fd == -1)
                                    {
                                        perror("accept");
                                        continue;
                                    }
                                    else
                                    {
                                        passive = 1;
                                    }
                                }
                                else
                                {
                                    sendResponse("426 PASV server timed out. \n");
                                }
                                continue;
                            }
                            else if (strncmp(cmd->command, "NLST", 4) == 0)
                            {
                                sendResponse("150 Here comes the directory listing.\n");
                                if (getcwd(cwd, sizeof(cwd)) != NULL)
                                {
                                    if (passive)
                                    {

                                        listFiles(pasv_fd, cwd);
                                        passive = 0;
                                        close(pasv_fd);
                                        sendResponse("226 Directory send OK.\n");
                                    }
                                    else
                                    {
                                        sendResponse("425 Use PORT or PASV first\n");
                                    }
                                }
                                else
                                {
                                    perror("getcwd() error");
                                }
                            }
                            else if (strncmp(cmd->command, "QUIT", 4) == 0)
                            {
                                printf("next connection\n");
                                exit(0);
                            }
                            else {
                                sendResponse("500 Unknown Command\n");
                            }
                        }
                    }
                }
                else
                {
                    sendResponse("530 Please login with USER\n");
                }
            }
            close(new_fd);
            exit(0);
        }
        close(new_fd);

        // parent doesn't need this
    }

    return 0;
}

// Check the command line arguments

// This is how to call the function in dir.c to get a listing of a directory.
// It requires a file descriptor, so in your code you would pass in the file descriptor
// returned for the ftp server's data connection

//printf("Printed %d directory entries\n", listFiles(1, "."));
