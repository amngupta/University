# CSftp Server

## FTP Server written in C 
Use `make run` + _Port Number_ to build and run the Server

### Commands Implemented:  [RFC 959](https://www.ietf.org/rfc/rfc959.txt).
- USER - (4.1.1)
- QUIT - (4.1.1)
- CWD - (4.1.1)
- CDUP - (4.1.1)
- TYPE - (4.1.1)
- MODE - Only supports Stream mode (3.4.1)
- STRU - Only supports File structure type (3.1.2, 3.1.2.1)
- RETR - (4.1.3)
- PASV - (4.1.1)
- NLST - Only supports NLST with no parameters (4.1.3) 