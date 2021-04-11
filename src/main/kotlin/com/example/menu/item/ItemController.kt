package com.example.menu.item

import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI
import java.util.*
import java.util.function.Consumer
import javax.validation.Valid

const val ITEM_CONTROLLER_ROOT = "api/menu/items"

@RestController
@RequestMapping(ITEM_CONTROLLER_ROOT)
class ItemController(private val service: ItemService) {

    @GetMapping
    fun findAll(): ResponseEntity<List<Item>> =
            ResponseEntity.ok().body(service.findAll())

    @GetMapping("/{id}")
    fun find(@PathVariable("id") id: Long): ResponseEntity<Item> =
            ResponseEntity.of(Optional.ofNullable(service.find(id)))

    @PostMapping
    @PreAuthorize("hasAuthority('create:items')")
    fun create(@Valid @RequestBody item: Item): ResponseEntity<Item> {
        val created = service.create(item)
        val location: URI = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id)
                .toUri()
        return ResponseEntity.created(location).body(created)
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('update:items')")
    fun update(
            @PathVariable("id") id: Long,
            @Valid @RequestBody updatedItem: Item
    ): ResponseEntity<Item> = when (val updated = service.update(id, updatedItem)) {
        null -> {
            val created = service.create(updatedItem)
            val location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(created.id)
                    .toUri()
            ResponseEntity.created(location).body(created)
        }
        else -> ResponseEntity.ok().body(updated)
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('delete:items')")
    fun delete(@PathVariable("id") id: Long): ResponseEntity<Item> {
        service.delete(id)
        return ResponseEntity.noContent().build()
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, String?>>? {
        val errors = ex.bindingResult.allErrors
        val map: MutableMap<String, String?> = HashMap(errors.size)
        errors.forEach(Consumer { error: ObjectError ->
            val key = (error as FieldError).field
            val `val` = error.getDefaultMessage()
            map[key] = `val`
        })
        return ResponseEntity.badRequest().body(map)
    }
}