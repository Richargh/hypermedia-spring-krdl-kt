package de.richargh.sandbox.hypermedia.with.spring.features.account.domain.internal

import de.richargh.sandbox.hypermedia.with.spring.commons.error.ItemNotFound
import de.richargh.sandbox.hypermedia.with.spring.features.account.domain.api.Account
import de.richargh.sandbox.hypermedia.with.spring.features.account.domain.api.AccountId
import de.richargh.sandbox.hypermedia.with.spring.start.web.GlobalExceptionHandler
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap

class InMemoryAccounts {
    private val items = ConcurrentHashMap<AccountId, Account>()

    fun all(): Sequence<Account> {
        return items.values.asSequence()
    }

    fun count(): Int {
        return items.count()
    }

    operator fun get(id: AccountId): Account {
        val item = items[id]
        if (item != null)
            return item.copy()
        else {
            System.err.println("Could not find account with id " + id.rawValue)
            throw ItemNotFound(id.rawValue, "Account")
        }
    }

    operator fun set(id: AccountId, account: Account): Account {
        items[id] = account.copy()
        return account
    }

    operator fun plusAssign(account: Account) {
        items[account.id] = account.copy()
    }

    operator fun minusAssign(account: Account) {
        items.remove(account.id)
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)
    }
}