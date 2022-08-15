[![Java CI with Maven](https://github.com/DenisYakovinov/job4j_chat/actions/workflows/maven.yml/badge.svg)](https://github.com/DenisYakovinov/job4j_chat/actions/workflows/maven.yml)
# job4j_chat

Creating a chat on the Rest API.<br>
REST API Implementation via Spring Boot.<br>
Contains four Person models. role. room, message.<br>
The application implements a chat with rooms.
<br> 
<br>
<h2>Technologies</h2>
<ul>
    <li>Spring Boot</li>
    <li>Spring MVC</li>
    <li>Spring Data JPA</li>
    <li>Spring AOP</li>
    <li>Spring Security</li>
    <li>Postgres</li>
    <Li>liquibase</Li>
</ul>
in progress..

![scheme_db](img/scheme_db.png) <br>

REST API:<br>

first, register user
![register](img/register.png) <br>
then, login (Get JWT token for further authorization)
![authorization](img/authorization.png) <br>
## then you need to use the received token to make a requests. ##
all rooms:
![rooms](img/rooms.png) <br>
<br>
further in progress..

