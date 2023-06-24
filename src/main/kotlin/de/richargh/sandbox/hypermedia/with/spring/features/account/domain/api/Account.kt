package de.richargh.sandbox.hypermedia.with.spring.features.account.domain.api

import org.springframework.hateoas.RepresentationModel

class Account(
        val id: AccountId,
        val name: String
): RepresentationModel<Account>() {

}