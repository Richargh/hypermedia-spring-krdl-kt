package de.richargh.sandbox.hypermedia.with.spring.features.account.web

import de.richargh.sandbox.hypermedia.with.spring.features.account.domain.AccountFacade
import de.richargh.sandbox.hypermedia.with.spring.features.account.domain.api.AccountId
import de.richargh.sandbox.hypermedia.with.spring.features.account.domain.dto.AccountDto
import de.richargh.sandbox.hypermedia.with.spring.features.account.domain.translate.toDto
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
@RequestMapping(value = ["/accounts"])
class AccountController(
        @Autowired private val accountFacade: AccountFacade
) {

    @GetMapping(value = ["/"], produces = ["application/hal+json"])
    fun getAccounts(): CollectionModel<AccountDto> {
        val accounts = accountFacade.all()
        val dtos = accounts
                .map { it -> it.toDto() }
                .map {
                    val selfLink: Link = linkTo(
                            methodOn(AccountController::class.java).getAccountById(it._id)
                    ).withSelfRel()

                    it.add(selfLink)
                }
                .toList()

        return CollectionModel.of(dtos)
    }

    @GetMapping("/{rawAccountId}")
    fun getAccountById(@PathVariable rawAccountId: String): AccountDto {
        val dto = accountFacade[AccountId(rawAccountId)].toDto()

        val selfLink: Link = linkTo(
                methodOn(AccountController::class.java).getAccountById(dto._id)
        ).withSelfRel()
        dto.add(selfLink)

        return dto
    }
}