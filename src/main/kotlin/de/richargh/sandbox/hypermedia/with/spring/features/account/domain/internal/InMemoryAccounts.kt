package de.richargh.sandbox.hypermedia.with.spring.features.account.domain.internal

import de.richargh.sandbox.hypermedia.with.spring.commons.error.ItemNotFound
import de.richargh.sandbox.hypermedia.with.spring.commons.search.SearchParams
import de.richargh.sandbox.hypermedia.with.spring.commons.search.SearchResult
import de.richargh.sandbox.hypermedia.with.spring.features.account.domain.api.Account
import de.richargh.sandbox.hypermedia.with.spring.features.account.domain.api.AccountId
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap

class InMemoryAccounts {
    private val items = ConcurrentHashMap<AccountId, Account>()

    fun search(params: SearchParams): SearchResult<Account> {
        val subItems = items.values.asSequence()
                .filterIndexed { index, _ -> index >= params.offset }
                .take(params.limit)
                .toList()
        val hasNext = subItems.isNotEmpty() && items.size - subItems.size - params.offset > 0
        val hasPrevious = subItems.isNotEmpty() && params.offset > 0
        return SearchResult(subItems, hasNext = hasNext, hasPrevious = hasPrevious)
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
        val log: Logger = LoggerFactory.getLogger(InMemoryAccounts::class.java)
    }
}