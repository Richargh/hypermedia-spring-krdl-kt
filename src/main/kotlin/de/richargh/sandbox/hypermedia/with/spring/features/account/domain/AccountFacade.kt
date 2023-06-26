package de.richargh.sandbox.hypermedia.with.spring.features.account.domain

import de.richargh.sandbox.hypermedia.with.spring.commons.error.AffordanceNotAvailable
import de.richargh.sandbox.hypermedia.with.spring.features.account.domain.api.Account
import de.richargh.sandbox.hypermedia.with.spring.features.account.domain.api.AccountAffordance
import de.richargh.sandbox.hypermedia.with.spring.features.account.domain.api.AccountId
import de.richargh.sandbox.hypermedia.with.spring.features.account.domain.internal.InMemoryAccounts

class AccountFacade(
        private val accounts: InMemoryAccounts
) {

    fun all(): Sequence<Account> {
        return accounts.all()
    }

    operator fun get(id: AccountId): Pair<Account, List<AccountAffordance>> {
        val account = accounts[id]

        return Pair(account, affordances(account))
    }

    fun deposit(id: AccountId, amount: Int): Account {
        val account = accounts[id]
        if (!affordances(account).contains(AccountAffordance.DEPOSIT))
            throw AffordanceNotAvailable(id.rawValue, "Account", AccountAffordance.DEPOSIT.name)

        accounts[id] = account.copy(balance = account.balance + amount)

        return account
    }

    fun withdraw(id: AccountId, amount: Int): Account {
        val account = accounts[id]
        if (!affordances(account).contains(AccountAffordance.WITHDRAW))
            throw AffordanceNotAvailable(id.rawValue, "Account", AccountAffordance.WITHDRAW.name)

        accounts[id] = account.copy(balance = account.balance - amount)

        return account
    }

    private fun affordances(account: Account): List<AccountAffordance> {
        val affordances = mutableListOf<AccountAffordance>()
        affordances.add(AccountAffordance.DEPOSIT)
        if (account.balance > 0) {
            affordances.add(AccountAffordance.WITHDRAW)
        }
        return affordances
    }
}