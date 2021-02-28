package com.example.menu.item

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ItemControllerPutOperationShould(@Autowired val restTemplate: TestRestTemplate) {

    @Test
    fun `modify an item`() {
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
        val updatedItem = entity.body!!.copy(price = 1000L)
        restTemplate.put("$ABSOLUTE_ITEM_CONTROLLER_ROOT/${updatedItem.id}", updatedItem, Item::class.java)
        val updatedResponse = restTemplate.getForEntity<Item>("$ABSOLUTE_ITEM_CONTROLLER_ROOT/${updatedItem.id}")
        assertThat(entity.body!!.id).isNotEqualTo(0)
        assertThat(entity.body!!.name).isEqualTo(updatedResponse.body!!.name)
        assertThat(entity.body!!.description).isEqualTo(updatedResponse.body!!.description)
        assertThat(updatedItem.price).isEqualTo(updatedResponse.body!!.price)
        assertThat(entity.body!!.image).isEqualTo(updatedResponse.body!!.image)
    }

    @Test
    @SuppressWarnings("unchecked", "List<Map<String, String> cast is required")
    fun `create an item when not present`() {
        val newItemJson = loadStringContentFromJson("/put/new-item-cachapa.json")
        val mapper = jacksonObjectMapper()
        val newItem = mapper.readValue(newItemJson, Item::class.java)
        restTemplate.put("$ABSOLUTE_ITEM_CONTROLLER_ROOT/${newItem.id}", newItem, Item::class.java)
        val entity = restTemplate.getForEntity<List<Item>>(ABSOLUTE_ITEM_CONTROLLER_ROOT)
        val actualResponse = (entity.body as List<Map<String, String>>).map { it.toItem() }
            .find { it.name == "Cachapa" }
        assertThat(actualResponse!!.id).isNotEqualTo(0)
        assertThat(actualResponse.name).isEqualTo(newItem.name)
        assertThat(actualResponse.description).isEqualTo(newItem.description)
        assertThat(actualResponse.price).isEqualTo(newItem.price)
        assertThat(actualResponse.image).isEqualTo(newItem.image)
    }

    private fun loadStringContentFromJson(jsonResourceRelativePath: String) =
        ItemControllerGetOperationsShould::class.java
            .getResource(jsonResourceRelativePath)
            .readText()

    private fun Map<String, Any>.toItem() = Item(
        (this["id"]!! as Number).toLong(),
        this["name"]!! as String,
        (this["price"]!! as Number).toLong(),
        this["description"] as String,
        this["image"] as String
    )
}