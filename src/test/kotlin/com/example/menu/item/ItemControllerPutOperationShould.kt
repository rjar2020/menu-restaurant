package com.example.menu.item

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ItemControllerPutOperationShould(@Autowired val restTemplate: TestRestTemplate) {

    @Test
    fun `modify an item`() {
        val newItemJson = loadStringContentFromJson("/post/new-item-pizza.json")
        val newItem = OBJECT_MAPPER.readValue(newItemJson, Item::class.java)
        val entity = restTemplate.postItem(newItem)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.CREATED)
        assertCustomEqualItem(entity, newItem)
        val updatedItem = entity.body!!.copy(price = 1000L)
        restTemplate.put("$ABSOLUTE_ITEM_CONTROLLER_ROOT/${updatedItem.id}", updatedItem, Item::class.java)
        val updatedResponse = restTemplate.getItemById(updatedItem.id)
        assertCustomEqualItem(updatedResponse, updatedItem)
    }

    @Test
    fun `create an item when not present`() {
        val newItemJson = loadStringContentFromJson("/put/new-item-cachapa.json")
        val newItem = OBJECT_MAPPER.readValue(newItemJson, Item::class.java)
        restTemplate.put("$ABSOLUTE_ITEM_CONTROLLER_ROOT/${newItem.id}", newItem, Item::class.java)
        val entity = restTemplate.getAllItems()
        val actualResponse = entity.body!!.map { it.toItem() }.find { it.name == "Cachapa" }
        assertCustomEqualItem(actualResponse!!, newItem)
    }
}