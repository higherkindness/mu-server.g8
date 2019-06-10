# Ping Pong Server

1) Create a `mu-server` project by using this g8 template:

```
sbt new higherkindness/mu-server.g8
```

2) Go in the new proyect:

```
cd mu-server
```

3) Publish the protocol and run the server by:

```
sbt publishLocal runServer
```

4) Create a `mu-client` project by using this g8 template:

```
sbt new higherkindness/mu-client.g8
```

5) Go in the new proyect:

```
cd mu-client
```

6) Then run the client:

```
sbt runClient
```

7) You will see:

In the server:

```
INFO  - PingPongServer - Starting server at localhost:9111
INFO  - PingPongService - Request: Ping(Ping!)
```

In the client:

```
INFO  - The response is: Pong!
```