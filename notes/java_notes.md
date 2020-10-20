# Java Notes
## Versions
- Java Micro Edition (Java ME)
    - Embedded devices
- Java Standard Edition (Java SE)
    - Client / mobile/tv devices
- Java Enterprise Edition (Java EE)
    - Server-side

## IDE's
- NetBeans
- IntelliJ IDEA
- Eclipse

## Processes
- Need host name? 
    - Windows command line
        - type: hostname
- Service is running and want to shut it down?
    - Windows command line
        - type: tasklist
        - find javaw.exe
            - taskkill /F /PID <PID_NUMBER>



## Networking
- Programming at **application layer**
- Communicate
    - TCP: Transmission Control Protocol
    - UDP: User Datagram Protocol
- **TCP**
    - Like a telephone call
    - reliable flow of data between 2 computers
- **UDP**
    - send packets of data, known as datagrams
    - unreliable, no guarantee of arrival
        - firewalls & routers to not allow UDP packets
- IP Address (32-bit)
    - determine which network
- Ports (16-bit)
    - determine which application in the network
    - TCP
        - server with port
    - UDP
        - packet with port
- Classes
    - TCP
        - URL, URLConnection, Socket, Serversocket, 
    - UDP
        - DatagramPacket, DatagramSocket, MulticastSocket
- **URL**
    - Uniform Resource Locator
    - reference to resource on internet
    - components
        - protocol identifier (http, https)
        - resource name 
            - host name (name of machine)
            - filename (pathname to file)
            - port number (optional)
            - reference
    - Java
        - Don't need to ever parse
- client-server 
    - client sends connection request, with port
    - server bounds socket to the port
- Multiple clients, need threads for each user
- **Socket**
    - represent 1 end-point of 2-way communication link
 
- **Datagram**
    - sends packets of data
- **Network Interface**
    - network interface card (NIC), can be software
    - useful for multi-homed system
        - can specify NIC


- Learn CookieManager later