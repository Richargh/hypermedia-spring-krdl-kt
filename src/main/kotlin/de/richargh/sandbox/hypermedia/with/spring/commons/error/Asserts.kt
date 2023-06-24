package de.richargh.sandbox.hypermedia.with.spring.commons.error

fun <Item> orThrow(id: String, name: String, item: Item?): Item {
    if(item == null)
        throw ItemNotFound(id, name)
    else
        return item
}
