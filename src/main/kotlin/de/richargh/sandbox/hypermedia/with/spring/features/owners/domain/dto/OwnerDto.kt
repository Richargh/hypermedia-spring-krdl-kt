package de.richargh.sandbox.hypermedia.with.spring.features.owners.domain.dto

import org.springframework.hateoas.RepresentationModel

open class OwnerDto(
        val _id: String,
        val name: String
) : RepresentationModel<OwnerDto>() {
}