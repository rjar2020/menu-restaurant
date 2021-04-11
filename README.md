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

## Notes
- If you are exploring Auth0 usage for complex/at scale implementation see [Auth0 Architecture Scenarios]
- With the basic plan and demo steps, I was able to easily authenticate with a social provider
- For Role-Based Access Control, besides the code changes displayed in the last PR, you should assign the permissions needed to yor API client ID in the Auth0 Dashboard -> APIs -> Your API (Menu API in this example) -> Machine to Machine apps

[Secure Spring Boot API]: https://auth0.com/blog/spring-boot-java-tutorial-build-a-crud-api/
[Building web applications with Spring Boot and Kotlin]: https://spring.io/guides/tutorials/spring-boot-kotlin/
[Kotlin-Spring Boot: Gotchas]: https://hackernoon.com/kotlin-spring-boot-gotchas-e267be7ec022
[demo client]: https://dashboard.whatabyte.app
[Auth0 Architecture Scenarios]: https://auth0.com/docs/architecture-scenarios