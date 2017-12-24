#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <sys/stat.h>
#include <netinet/in.h>
#include <netdb.h>
#include <arpa/inet.h>
#include <sys/wait.h>
#include <fcntl.h>
#include <signal.h>
#include "dir.h"
#include "usage.h"
#include <ctype.h>
#include <time.h>

typedef struct Command
{
    char command[5];
    char arg[128];
} Command;

typedef struct Port
{
    int p1;
    int p2;
} Port;


#define PORT "3492"
#define BACKLOG 10
#define MAXDATASIZE 100
#define BUF_SIZE 8192