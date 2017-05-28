# Diff Challenge
Diff challenge provided by WAES

## Tech stack
- Java 8
- Spring Boot
    - Data Rest
    - Data JPA
- H2 database
- Lombok
- Maven

## How to build and run
This project already includes a minimized version of maven. All other dependencies will be downloaded by it.

Execute maven build (It may take a while in the first run):
```sh
$ ./mvnw clean install
```

After the message of BUILD SUCCESS, the application is ready to run:
```sh
$ ./mvnw spring-boot:run
```

And the running application will be ready to accept and answer the API endpoints defined in challenge.

The preconfigured localhost port is 8080. If you need, you can change it in `application.properties`.

## Improvement suggestions
- [ ] Use Swagger for API documentation. Make easier to client understand the endpoints
- [ ] Use MongoDB. Able to save requests with minimum conversions, better scalability
- [ ] Use JMeter/Gatling for performance measurements.