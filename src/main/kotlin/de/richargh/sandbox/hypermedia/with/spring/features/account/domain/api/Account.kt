package de.richargh.sandbox.hypermedia.with.spring.features.account.domain.api

import de.richargh.sandbox.hypermedia.with.spring.features.owners.domain.api.OwnerId

data class Account(
        val id: AccountId,
        val owner: OwnerId,
        val name: String,
        val balance: Int
)