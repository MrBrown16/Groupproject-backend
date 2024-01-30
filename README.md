## GROUPPROJECT BACKEND

contents:
- OLDRESOURCESERVER (faulty but repurposable "business logic" for the resource server)
- resource-server (spring oauth2 resource server, only the bones)
- authorization-server(spring oauth2 authorization server, only the bones, some configuration to work with the [frontend](https://github.com/Attila732/Onkormanyzat_))


REQUIREMENTS:
- JAVA 21


to run:
- start authorization server VSCode run / terminal: ./mvnw spring-boot:run
- start resource server VSCode run / terminal: ./mvnw spring-boot:run
- start [frontend](https://github.com/Attila732/Onkormanyzat_) see the README 

PORTS:
- authorization server: 8083
- OLDRESOURCESERVER:8080
- resource server: 8082
- frontend server: 8081
