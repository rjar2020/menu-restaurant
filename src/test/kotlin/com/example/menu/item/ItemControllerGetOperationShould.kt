package com.example.menu.item

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.http.HttpStatus

const val ABSOLUTE_ITEM_CONTROLLER_ROOT = "/$ITEM_CONTROLLER_ROOT"

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ItemControllerGetOperationsShould(@Autowired val restTemplate: TestRestTemplate) {

    @Test
    @SuppressWarnings("unchecked", "List<Map<String, String> cast is required")
    fun `get all the existing menu items`() {
        val expectedJson = loadStringContentFromJson("/get/all-items.json")
        val mapper = jacksonObjectMapper()
        val entity = restTemplate.getForEntity<List<Item>>(ABSOLUTE_ITEM_CONTROLLER_ROOT)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        val actualResponse = (entity.body as List<Map<String, String>>).map { it.toItem() }.toSet()
        val expectedResponse = mapper.readValue<List<Item>>(
            expectedJson, mapper.typeFactory.constructCollectionType(
                MutableList::class.java,
                Item::class.java
            )
        ).toSet()
        assertThat(actualResponse.intersect(expectedResponse)).isEqualTo(expectedResponse)
    }

    @Test
    fun `get an exiting item by id`() {
        val expectedJson = loadStringContentFromJson("/get/item-id-1.json")
        val mapper = jacksonObjectMapper()
        val entity = restTemplate.getForEntity<Item>("$ABSOLUTE_ITEM_CONTROLLER_ROOT/1")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        val expectedResponse = mapper.readValue(expectedJson, Item::class.java)
        assertThat(entity.body).isEqualTo(expectedResponse)
    }

    @Test
    fun `get HTTP 404 when retrieving nonexistent id`() {
        val entity = restTemplate.getForEntity<Item>("$ABSOLUTE_ITEM_CONTROLLER_ROOT/69")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
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