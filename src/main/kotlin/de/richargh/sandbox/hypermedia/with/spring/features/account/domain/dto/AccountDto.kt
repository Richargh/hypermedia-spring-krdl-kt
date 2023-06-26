package de.richargh.sandbox.hypermedia.with.spring.features.account.domain.dto

import org.springframework.hateoas.RepresentationModel

open class AccountDto(
        val _id: String,
        val _ownerid: String,
        val name: String,
        val balance: Int
) : RepresentationModel<AccountDto>() {

}