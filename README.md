# moneytransfer

Sample **Spring Boot** project exposing a **Rest API** where a user after registering and logging in, can cash-in money to his account and transfer money to another user.

## Setup Project

The project uses **Maven** for package management. To setup the project first execute:
```
mvn install
```


## Running Tests

The project contains Integration Tests covering the functionality. You can run them executing the following command: 
```
mvn test
```

## Running The Application

You can run the application by executing:
```
mvn spring-boot:run
```

## Swagger

The API supports **SwaggerUI** on the following URI: 
```
localhost:8080/swagger-ui.html
```
