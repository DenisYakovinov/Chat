[![Java CI with Maven](https://github.com/DenisYakovinov/job4j_chat/actions/workflows/maven.yml/badge.svg)](https://github.com/DenisYakovinov/job4j_chat/actions/workflows/maven.yml)
# job4j_chat

Creating a chat on the Rest API.<br>
Implementation via Spring Boot.<br>
The application implements an imaginary chat with rooms.
<br> 
<br>
<h2>Technologies</h2>
<ul>
    <li>Spring Boot, MVC, Data JPA, AOP, Security</li>
    <li>Postgres</li>
    <Li>liquibase</Li>
    <Li>slf4j, logback</Li>
    <Li>Swagger</Li>
</ul>
in progress..

![scheme_db](img/scheme_db.png) <br>

Swagger UI documentation available via link ```localhost:8080/swagger-ui/index.html```
![swaggerMainPage](img/swaggerMainPage.png) <br>
![swaggerPostUser](img/swaggerPostUser.png) <br>
![swaggerRoomById](img/swaggerRoomById.png) <br>
![swaggerSchm](img/swaggerSchm.png) <br>

To run the app (need at least java 11), clone the project
```
https://github.com/DenisYakovinov/job4j_chat.git
```
then using terminal from root directory:<br>

1. run db in docker
```
docker-compose up chat_db
```
2. then
```
mvn install
```
3. and run
```
java -jar target/job4j_chat-1.0-SNAPSHOT.jar
```
4. or
```
mvn spring-boot:run
```

REST API:<br>

first, register user
![register](img/register.png) <br>
then, login (Get JWT token for further authorization)
![authorization](img/authorization.png) <br>
## then you need to use the received token to make a requests. ##
all rooms:
![rooms](img/rooms.png) <br>
<br>
create a new room:
![newRoom](img/newRoom.png) <br>
create a new message:
![newMessage](img/newMessage.png) <br>
get room with messages:
![roomWithMessage](img/roomWithMessage.png) <br>

