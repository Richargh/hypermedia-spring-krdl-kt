package de.richargh.sandbox.hypermedia.with.spring.commons.error

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class ItemNotFound(val rawId: String, val itemName: String) : Exception("Could not find $itemName with id $rawId")

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "fooo")
class InvalidBody(val itemName: String) : Exception("Body $itemName is not valid")

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "fooo")
class AffordanceNotAvailable(val rawId: String, val itemName: String, val affordanceName: String)
    : Exception("Affordance $affordanceName is not valid for $itemName with id $rawId")
