# Ping Pong Server

1) Publish the protocol and run the server by:

```
sbt publishLocal runServer
```


2) Then run the client:

```
sbt runClient
```

3) You will see:

In the server:

```
INFO  - PingPongServer - Starting server at localhost:9111
INFO  - PingPongService - Request: Ping(Ping!)
```

In the client:

```
INFO  - The response is: Pong!
```