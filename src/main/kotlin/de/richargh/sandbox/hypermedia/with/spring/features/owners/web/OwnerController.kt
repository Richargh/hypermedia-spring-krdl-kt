package de.richargh.sandbox.hypermedia.with.spring.features.owners.web

import de.richargh.sandbox.hypermedia.with.spring.commons.search.SearchParams
import de.richargh.sandbox.hypermedia.with.spring.features.owners.domain.OwnerFacade
import de.richargh.sandbox.hypermedia.with.spring.features.owners.domain.api.OwnerId
import de.richargh.sandbox.hypermedia.with.spring.features.owners.domain.dto.OwnerDto
import de.richargh.sandbox.hypermedia.with.spring.features.owners.domain.translate.toDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.Link
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/owners"])
class OwnerController(
        @Autowired private val ownerFacade: OwnerFacade
) {

    @GetMapping(value = [""], produces = ["application/prs.hal-forms+json"])
    fun search(limit: Int?, offset: Int?): CollectionModel<OwnerDto> {
        val searchParams = SearchParams.of(limit = limit, offset = offset)
        val result = ownerFacade.search(searchParams)
        val dtos = result.items
                .map { it -> it.toDto() }
                .map {
                    val selfLink: Link = linkTo(methodOn(OwnerController::class.java).findOne(it._id)).withSelfRel()
                    it.add(selfLink)
                }
                .toList()

        val links = mutableListOf<Link>()
        links.add(linkTo(OwnerController::class.java).withSelfRel())
        if (result.hasPrevious) {
            val previousParams = searchParams.previous()
            links.add(Link.of("http://localhost:8080/owners{?limit,offset}")
                    .expand(mapOf("limit" to previousParams.limit, "offset" to previousParams.offset))
                    .withRel("previous"))
        }
        if (result.hasNext) {
            val nextParams = searchParams.next()
            links.add(Link.of("http://localhost:8080/owners{?limit,offset}")
                    .expand(mapOf("limit" to nextParams.limit, "offset" to nextParams.offset))
                    .withRel("next"))
        }

        return CollectionModel.of(dtos, links)
    }


    @GetMapping("/{rawOwnerId}", produces = ["application/prs.hal-forms+json"])
    fun findOne(@PathVariable rawOwnerId: String): OwnerDto {
        val (account) = ownerFacade[OwnerId(rawOwnerId)]
        val dto = account.toDto()

        val links: Link = linkTo(methodOn(OwnerController::class.java).findOne(dto._id)).withSelfRel()
        dto.add(links)

        return dto
    }

}