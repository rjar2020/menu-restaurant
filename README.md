# Auth0 with Spring Boot
Following Auth0 tutorial in [Secure Spring Boot API]

## Using Kotlin
Instead of following the tuto on Java, it has been implemented in Kotlin.
Not every step was a straightforward translation, so the following posts were used as references to tackle some minor issues:

- [Building web applications with Spring Boot and Kotlin] -> mostly for testing.
- [Kotlin-Spring Boot: Gotchas] ->  for validating HTTP requests.

## GUI
The tuto has all the details, however is important to highlight 
that this [demo client] can be used to do some exploratory testing with this API

[Secure Spring Boot API]: https://auth0.com/blog/spring-boot-java-tutorial-build-a-crud-api/
[Building web applications with Spring Boot and Kotlin]: https://spring.io/guides/tutorials/spring-boot-kotlin/
[Kotlin-Spring Boot: Gotchas]: https://hackernoon.com/kotlin-spring-boot-gotchas-e267be7ec022
[demo client]: https://dashboard.whatabyte.app

## Notes
- With the basic plan and demo steps, I was able to easily authenticate with a social provider
- Role-Based Access Control related code changes aren't in place