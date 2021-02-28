package com.example.menu.item

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface InMemoryItemRepository : CrudRepository<Item, Long>
