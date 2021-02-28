package com.example.menu.item

import org.springframework.data.map.repository.config.EnableMapRepositories
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Service
import java.time.Clock
import java.util.*

@Service
@EnableMapRepositories
class ItemService(private val repository: CrudRepository<Item, Long>) {

    init {
        this.repository.saveAll(defaultItems())
    }

    fun findAll(): List<Item> {
        val list: MutableList<Item> = ArrayList()
        repository.findAll().forEach { e: Item -> list.add(e) }
        return list
    }

    fun find(id: Long): Item? = repository.findById(id).orElse(null)

    fun create(item: Item) =
        repository.save(
            Item(
                // To ensure the item ID remains unique,
                // use the current timestamp.
                Clock.systemUTC().millis(),
                item.name,
                item.price,
                item.description,
                item.image
            )
        )

    // Only update an item if it can be found first.
    fun update(id: Long, newItem: Item): Item? =
        repository.findById(id)
            .map { oldItem -> repository.save(oldItem.copy(newItem)) }
            .orElse(null)

    fun delete(id: Long) = repository.deleteById(id)

    private fun defaultItems(): List<Item> {
        return listOf(
            Item(1L, "Burger", 599L, "Tasty", "https://cdn.auth0.com/blog/whatabyte/burger-sm.png"),
            Item(2L, "Pizza", 299L, "Cheesy", "https://cdn.auth0.com/blog/whatabyte/pizza-sm.png"),
            Item(3L, "Tea", 199L, "Informative", "https://cdn.auth0.com/blog/whatabyte/tea-sm.png")
        )
    }
}