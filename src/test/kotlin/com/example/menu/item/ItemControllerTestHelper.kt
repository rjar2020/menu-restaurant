package com.example.menu.item


import com.example.menu.security.AccessToken
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.assertj.core.api.Assertions
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration


internal const val ABSOLUTE_ITEM_CONTROLLER_ROOT = "/$ITEM_CONTROLLER_ROOT"
internal val OBJECT_MAPPER = jacksonObjectMapper()

internal fun TestRestTemplate.postItem(newItem: Item) =
        this.postForEntity(
                ABSOLUTE_ITEM_CONTROLLER_ROOT,
                getAuthenticatedRequest(newItem),
                Item::class.java)

internal fun TestRestTemplate.getAllItems() =
        this.getForEntity<List<Map<String, String>>>(ABSOLUTE_ITEM_CONTROLLER_ROOT)

internal fun TestRestTemplate.getItemById(id: Long) =
        this.getForEntity<Item>("$ABSOLUTE_ITEM_CONTROLLER_ROOT/$id")

internal fun loadStringContentFromJson(jsonResourceRelativePath: String) =
        ItemControllerGetOperationsShould::class.java
                .getResource(jsonResourceRelativePath)
                .readText()

internal fun assertCustomEqualItem(entity: ResponseEntity<Item>, newItem: Item) {
    Assertions.assertThat(entity.body!!.id).isNotEqualTo(0)
    Assertions.assertThat(entity.body!!.name).isEqualTo(newItem.name)
    Assertions.assertThat(entity.body!!.description).isEqualTo(newItem.description)
    Assertions.assertThat(entity.body!!.price).isEqualTo(newItem.price)
    Assertions.assertThat(entity.body!!.image).isEqualTo(newItem.image)
}

internal fun assertCustomEqualItem(entity: Item, newItem: Item) {
    Assertions.assertThat(entity.id).isNotEqualTo(0)
    Assertions.assertThat(entity.name).isEqualTo(newItem.name)
    Assertions.assertThat(entity.description).isEqualTo(newItem.description)
    Assertions.assertThat(entity.price).isEqualTo(newItem.price)
    Assertions.assertThat(entity.image).isEqualTo(newItem.image)
}

internal fun Map<String, Any>.toItem() = Item(
        (this["id"]!! as Number).toLong(),
        this["name"]!! as String,
        (this["price"]!! as Number).toLong(),
        this["description"] as String,
        this["image"] as String
)

internal fun getAuthenticatedRequest(newItem: Any) = HttpEntity(newItem, getAuthHeaders())

internal fun getAuthHeaders(): HttpHeaders {
    val response = HttpClient.newHttpClient().send(
            HttpRequest.newBuilder()
                    .uri(URI.create("https://dev-9hiw86-v.eu.auth0.com/oauth/token"))
                    .timeout(Duration.ofSeconds(3))
                    .header("content-type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString("{\"client_id\":\"YdltwoNva3WilLBHM2mM8FHy1vHk1K35\",\"client_secret\":\"GTfNf7z453EnuGROkeZsbyNT21VsjD58GZEWWgSq4gZqhREztQpqGVfFXJOPS4bP\",\"audience\":\"https://menu-api.example.com\",\"grant_type\":\"client_credentials\"}"))
                    .build(), HttpResponse.BodyHandlers.ofString()
    )
    val token = OBJECT_MAPPER.readValue(response.body(), AccessToken::class.java)
    val headers = HttpHeaders()
    headers.set("Authorization", "Bearer ${token.token}")
    return headers
}