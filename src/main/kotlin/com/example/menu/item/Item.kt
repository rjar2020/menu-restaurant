package com.example.menu.item

import org.hibernate.validator.constraints.URL
import org.springframework.data.annotation.Id
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Positive
import javax.validation.constraints.Size

data class Item(
    @Id val id: Long,
    @field:NotNull(message = "name is required")
    @field:Size(min = 2)
    @field:Pattern(
        regexp = "^[a-zA-Z ]+$",
        message = "name must be a string"
    ) val name: String,
    @field:NotNull(message = "price is required")
    @field:Positive(message = "price must be positive")
    val price: Long,
    @field:NotNull(message = "description is required")
    @field:Size(min = 2)
    @field:Pattern(
        regexp = "^[a-zA-Z ]+$",
        message = "description must be a string"
    ) val description: String,
    @field:NotNull(message = "image is required")
    @field:Size(min = 2)
    @field:URL(message = "image must be a URL")
    val image: String
) {
    fun copy(item: Item) =
        Item(this.id, item.name, item.price, item.description, item.image)
}
