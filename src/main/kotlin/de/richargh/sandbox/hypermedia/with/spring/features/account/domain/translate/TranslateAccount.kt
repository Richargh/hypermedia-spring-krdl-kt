package de.richargh.sandbox.hypermedia.with.spring.features.account.domain.translate

import de.richargh.sandbox.hypermedia.with.spring.features.account.domain.api.Account
import de.richargh.sandbox.hypermedia.with.spring.features.account.domain.dto.AccountDto

fun Account.toDto() = AccountDto(
        this.id.rawValue,
        this.name)
