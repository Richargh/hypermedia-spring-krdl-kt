package de.richargh.sandbox.hypermedia.with.spring.features.owners.web

import org.springframework.hateoas.LinkRelation

class OwnerRelations private constructor() {
    companion object {
        val owner = LinkRelation.of("owner")
    }
}