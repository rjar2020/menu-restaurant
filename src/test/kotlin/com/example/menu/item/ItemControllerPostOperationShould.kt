package com.example.menu.item

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ItemControllerPostOperationShould(@Autowired val restTemplate: TestRestTemplate) {

    @Test
    @SuppressWarnings("unchecked", "List<Map<String, String> cast is required")
    fun `post a new item`() {
        val newItemJson = loadStringContentFromJson("/post/new-item-pizza.json")
        val mapper = jacksonObjectMapper()
        val newItem = mapper.readValue(newItemJson, Item::class.java)
        val entity = restTemplate.postForEntity(ABSOLUTE_ITEM_CONTROLLER_ROOT, newItem, Item::class.java)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(entity.body!!.id).isNotEqualTo(0)
        assertThat(entity.body!!.name).isEqualTo(newItem.name)
        assertThat(entity.body!!.description).isEqualTo(newItem.description)
        assertThat(entity.body!!.price).isEqualTo(newItem.price)
        assertThat(entity.body!!.image).isEqualTo(newItem.image)
    }

    @Test
    fun `response with bad request when invalid item`() {
        mapOf(
            "/post/new-item-arepa-invalid-name.json" to "name must be a string",
            "/post/new-item-arepa-invalid-price.json" to "price must be positive",
            "/post/new-item-arepa-invalid-description.json" to "description must be a string",
            "/post/new-item-arepa-invalid-image.json" to "image must be a URL"
        ).forEach {
            val newItemJson = loadStringContentFromJson(it.key)
            val mapper = jacksonObjectMapper()
            val newItem = mapper.readValue(newItemJson, Item::class.java)
            val entity = restTemplate.postForEntity(ABSOLUTE_ITEM_CONTROLLER_ROOT, newItem, String::class.java)
            assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
            assertThat(entity.body).contains(it.value)
        }
    }

    private fun loadStringContentFromJson(jsonResourceRelativePath: String) =
        ItemControllerGetOperationsShould::class.java
            .getResource(jsonResourceRelativePath)
            .readText()
}