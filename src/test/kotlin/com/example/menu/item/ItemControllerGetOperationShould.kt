package com.example.menu.item

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ItemControllerGetOperationsShould(@Autowired val restTemplate: TestRestTemplate) {

    @Test
    fun `get all the existing menu items`() {
        val expectedJson = loadStringContentFromJson("/get/all-items.json")
        val entity = restTemplate.getAllItems()
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        val actualResponse = entity.body!!.map { it.toItem() }.toSet()
        val expectedResponse = OBJECT_MAPPER.readValue<List<Item>>(
            expectedJson, OBJECT_MAPPER.typeFactory.constructCollectionType(
                MutableList::class.java,
                Item::class.java
            )
        ).toSet()
        assertThat(actualResponse.intersect(expectedResponse)).isEqualTo(expectedResponse)
    }

    @Test
    fun `get an exiting item by id`() {
        val expectedJson = loadStringContentFromJson("/get/item-id-1.json")
        val entity = restTemplate.getItemById(1)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        val expectedResponse = OBJECT_MAPPER.readValue(expectedJson, Item::class.java)
        assertThat(entity.body).isEqualTo(expectedResponse)
    }

    @Test
    fun `get HTTP 404 when retrieving nonexistent id`() {
        val entity = restTemplate.getItemById(69)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }
}