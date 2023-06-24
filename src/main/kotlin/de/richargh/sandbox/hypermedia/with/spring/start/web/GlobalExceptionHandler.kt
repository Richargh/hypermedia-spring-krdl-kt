package de.richargh.sandbox.hypermedia.with.spring.start.web

import de.richargh.sandbox.hypermedia.with.spring.commons.error.ItemNotFound
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(ItemNotFound::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleItemNotFoundException(
            itemNotFoundException: ItemNotFound,
            request: WebRequest?
    ): ResponseEntity<ErrorDto> {
        return buildErrorResponse(HttpStatus.NOT_FOUND);
    }

    private fun buildErrorResponse(
            httpStatus: HttpStatus
    ): ResponseEntity<ErrorDto> {
        val errorResponse = ErrorDto(httpStatus.value())
        return ResponseEntity.status(httpStatus).body(errorResponse)
    }
}

class ErrorDto(val status: Int)

