package de.richargh.sandbox.hypermedia.with.spring.features.owners.web

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
    fun getAccounts(): CollectionModel<OwnerDto> {
        val accounts = ownerFacade.all()
        val dtos = accounts
                .map { it -> it.toDto() }
                .map {
                    val selfLink: Link = linkTo(
                            methodOn(OwnerController::class.java).getOwnerById(it._id)
                    ).withSelfRel()

                    it.add(selfLink)
                }
                .toList()

        val link: Link = linkTo(OwnerController::class.java).withSelfRel()
        return CollectionModel.of(dtos, link)
    }


    @GetMapping("/{rawOwnerId}", produces = ["application/prs.hal-forms+json"])
    fun getOwnerById(@PathVariable rawOwnerId: String): OwnerDto {
        val (account) = ownerFacade[OwnerId(rawOwnerId)]
        val dto = account.toDto()

        val links: Link = linkTo(methodOn(OwnerController::class.java).getOwnerById(dto._id)).withSelfRel()
        dto.add(links)

        return dto
    }
}