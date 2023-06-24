package de.richargh.sandbox.hypermedia.with.spring.commons.error

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.Exception

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class ItemNotFound(val rawId: String, val name: String): Exception("Could not find $name with id $rawId")
