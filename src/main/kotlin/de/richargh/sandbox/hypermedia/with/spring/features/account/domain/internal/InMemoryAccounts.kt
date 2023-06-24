package de.richargh.sandbox.hypermedia.with.spring.features.account.domain.internal

import de.richargh.sandbox.hypermedia.with.spring.commons.error.ItemNotFound
import de.richargh.sandbox.hypermedia.with.spring.features.account.domain.api.Account
import de.richargh.sandbox.hypermedia.with.spring.features.account.domain.api.AccountId
import java.util.concurrent.ConcurrentHashMap

class InMemoryAccounts {
    private val items = ConcurrentHashMap<AccountId, Account>()

    fun all(): Sequence<Account> {
        return items.values.asSequence()
    }

    operator fun get(id: AccountId): Account {
        val item = items[id]
        if (item != null)
            return item
        else
            throw ItemNotFound(id.rawValue, "Account")
    }

    operator fun plusAssign(account: Account) {
        items[account.id] = account
    }

    operator fun minusAssign(account: Account) {
        items.remove(account.id)
    }
}