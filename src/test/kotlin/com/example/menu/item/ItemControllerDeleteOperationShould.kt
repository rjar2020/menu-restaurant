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
class ItemControllerDeleteOperationShould(@Autowired val restTemplate: TestRestTemplate) {

    @Test
    fun `delete an item`() {
        val newItemJson = loadStringContentFromJson("/delete/new-item-empanada.json")
        val mapper = jacksonObjectMapper()
        val newItem = mapper.readValue(newItemJson, Item::class.java)
        val entity = restTemplate.postForEntity(ABSOLUTE_ITEM_CONTROLLER_ROOT, newItem, Item::class.java)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(entity.body!!.id).isNotEqualTo(0)
        assertThat(entity.body!!.name).isEqualTo(newItem.name)
        restTemplate.delete("$ABSOLUTE_ITEM_CONTROLLER_ROOT/${entity.body!!.id}")
        val updatedResponse = restTemplate.getForEntity<String>("$ABSOLUTE_ITEM_CONTROLLER_ROOT/${entity.body!!.id}")
        assertThat(updatedResponse.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }

    private fun loadStringContentFromJson(jsonResourceRelativePath: String) =
        ItemControllerGetOperationsShould::class.java
            .getResource(jsonResourceRelativePath)
            .readText()
}