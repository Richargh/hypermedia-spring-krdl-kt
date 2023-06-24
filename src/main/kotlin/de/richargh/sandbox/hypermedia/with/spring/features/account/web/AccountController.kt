package de.richargh.sandbox.hypermedia.with.spring.features.account.web

import de.richargh.sandbox.hypermedia.with.spring.commons.error.InvalidBody
import de.richargh.sandbox.hypermedia.with.spring.features.account.domain.AccountFacade
import de.richargh.sandbox.hypermedia.with.spring.features.account.domain.api.AccountAffordance
import de.richargh.sandbox.hypermedia.with.spring.features.account.domain.api.AccountId
import de.richargh.sandbox.hypermedia.with.spring.features.account.domain.dto.AccountDto
import de.richargh.sandbox.hypermedia.with.spring.features.account.domain.dto.DepositDto
import de.richargh.sandbox.hypermedia.with.spring.features.account.domain.dto.WithdrawDto
import de.richargh.sandbox.hypermedia.with.spring.features.account.domain.translate.toDto
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

    @GetMapping(value = ["/"], produces = ["application/prs.hal-forms+json"])
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

        val link: Link = linkTo(AccountController::class.java).withSelfRel()
        return CollectionModel.of(dtos, link)
    }

    @GetMapping("/{rawAccountId}", produces = ["application/prs.hal-forms+json"])
    fun getAccountById(@PathVariable rawAccountId: String): AccountDto {
        val (account, affordances) = accountFacade[AccountId(rawAccountId)]
        val dto = account.toDto()

        var links: Link = linkTo(
                methodOn(AccountController::class.java).getAccountById(dto._id)
        ).withSelfRel()
        if (affordances.contains(AccountAffordance.WITHDRAW))
            links = links.andAffordance(afford(methodOn(AccountController::class.java).withdraw(rawAccountId, null)))
        if (affordances.contains(AccountAffordance.DEPOSIT))
            links = links.andAffordance(afford(methodOn(AccountController::class.java).deposit(rawAccountId, null)))

        dto.add(links)

        return dto
    }

    @PostMapping("/{rawAccountId}/deposit", produces = ["application/prs.hal-forms+json"])
    fun deposit(
            @PathVariable rawAccountId: String,
            @RequestBody body: DepositDto?): AccountDto {
        if (body == null) throw InvalidBody("DepositDto")

        accountFacade.deposit(AccountId(rawAccountId), body.amount)

        return getAccountById(rawAccountId)
    }

    @PostMapping("/{rawAccountId}/withdraw", produces = ["application/prs.hal-forms+json"])
    fun withdraw(
            @PathVariable rawAccountId: String,
            @RequestBody body: WithdrawDto?): AccountDto {
        if (body == null) throw InvalidBody("WithdrawDto")

        accountFacade.withdraw(AccountId(rawAccountId), body.amount)

        return getAccountById(rawAccountId)
    }
}