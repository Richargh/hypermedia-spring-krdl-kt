package de.richargh.sandbox.hypermedia.with.spring.features.account.web

import de.richargh.sandbox.hypermedia.with.spring.commons.error.InvalidBody
import de.richargh.sandbox.hypermedia.with.spring.commons.search.SearchParams
import de.richargh.sandbox.hypermedia.with.spring.features.account.domain.AccountFacade
import de.richargh.sandbox.hypermedia.with.spring.features.account.domain.api.AccountAffordance
import de.richargh.sandbox.hypermedia.with.spring.features.account.domain.api.AccountId
import de.richargh.sandbox.hypermedia.with.spring.features.account.domain.dto.AccountDto
import de.richargh.sandbox.hypermedia.with.spring.features.account.domain.dto.DepositDto
import de.richargh.sandbox.hypermedia.with.spring.features.account.domain.dto.WithdrawDto
import de.richargh.sandbox.hypermedia.with.spring.features.account.domain.translate.toDto
import de.richargh.sandbox.hypermedia.with.spring.features.owners.web.OwnerController
import de.richargh.sandbox.hypermedia.with.spring.features.owners.web.OwnerRelations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.Link
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping(value = ["/accounts"])
class AccountController(
        @Autowired private val accountFacade: AccountFacade
) {

    @GetMapping(value = [""], produces = ["application/prs.hal-forms+json"])
    fun search(limit: Int?, offset: Int?): CollectionModel<AccountDto> {
        val searchParams = SearchParams.of(limit = limit, offset = offset)
        val result = accountFacade.search(searchParams)
        val accounts = result.items
        val dtos = accounts
                .map { it -> it.toDto() }
                .map {
                    val selfLink: Link = linkTo(methodOn(AccountController::class.java).findOne(it._id))
                            .withSelfRel()
                    val ownerLink = linkTo(methodOn(OwnerController::class.java).findOne(it._ownerid))
                            .withRel("owner")

                    it.add(selfLink, ownerLink)
                }
                .toList()

        val links = mutableListOf<Link>()
        links.add(linkTo(OwnerController::class.java).withSelfRel())
        links.add(linkTo(OwnerController::class.java).withRel(OwnerRelations.owner))
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

    @GetMapping("/{rawAccountId}", produces = ["application/prs.hal-forms+json"])
    fun findOne(@PathVariable rawAccountId: String): AccountDto {
        val (account, affordances) = accountFacade[AccountId(rawAccountId)]
        val dto = account.toDto()

        var links: Link = linkTo(methodOn(AccountController::class.java).findOne(dto._id)).withSelfRel()
        if (affordances.contains(AccountAffordance.WITHDRAW))
            links = links.andAffordance(afford(methodOn(AccountController::class.java).withdraw(rawAccountId, null)))
        if (affordances.contains(AccountAffordance.DEPOSIT))
            links = links.andAffordance(afford(methodOn(AccountController::class.java).deposit(rawAccountId, null)))
        val ownerLink = linkTo(methodOn(OwnerController::class.java).findOne(dto._ownerid))
                .withRel(OwnerRelations.owner)
        dto.add(links, ownerLink)

        return dto
    }

    @PostMapping("/{rawAccountId}/deposit", produces = ["application/prs.hal-forms+json"])
    fun deposit(
            @PathVariable rawAccountId: String,
            @RequestBody body: DepositDto?): AccountDto {
        if (body == null) throw InvalidBody("DepositDto")

        accountFacade.deposit(AccountId(rawAccountId), body.amount)

        return findOne(rawAccountId)
    }

    @PostMapping("/{rawAccountId}/withdraw", produces = ["application/prs.hal-forms+json"])
    fun withdraw(
            @PathVariable rawAccountId: String,
            @RequestBody body: WithdrawDto?): AccountDto {
        if (body == null) throw InvalidBody("WithdrawDto")

        accountFacade.withdraw(AccountId(rawAccountId), body.amount)

        return findOne(rawAccountId)
    }
}