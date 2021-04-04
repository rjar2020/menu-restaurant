package com.example.menu.item

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ItemControllerDeleteOperationShould(@Autowired val restTemplate: TestRestTemplate) {

    @Test
    fun `delete an item`() {
        val newItemJson = loadStringContentFromJson("/delete/new-item-empanada.json")
        val newItem = OBJECT_MAPPER.readValue(newItemJson, Item::class.java)
        val entity = restTemplate.postItem(newItem)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.CREATED)
        val id = (entity.body!!.id as Number).toLong()
        assertCustomEqualItem(entity, newItem)
        restTemplate.exchange(
            "$ABSOLUTE_ITEM_CONTROLLER_ROOT/$id",
            HttpMethod.DELETE,
            HttpEntity<Any>(getAuthHeaders()),
            String::class.java
        )
        val updatedResponse = restTemplate.getItemById(id)
        assertThat(updatedResponse.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }
}