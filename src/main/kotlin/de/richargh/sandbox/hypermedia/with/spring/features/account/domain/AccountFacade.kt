package de.richargh.sandbox.hypermedia.with.spring.features.account.domain

import de.richargh.sandbox.hypermedia.with.spring.features.account.domain.api.Account
import de.richargh.sandbox.hypermedia.with.spring.features.account.domain.api.AccountId
import de.richargh.sandbox.hypermedia.with.spring.features.account.domain.internal.InMemoryAccounts

class AccountFacade(
        private val accounts: InMemoryAccounts
) {
    operator fun get(id: AccountId): Account {
        return accounts[id]
    }
}