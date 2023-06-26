package de.richargh.sandbox.hypermedia.with.spring.start.web

import de.richargh.sandbox.hypermedia.with.spring.features.account.domain.api.Account
import de.richargh.sandbox.hypermedia.with.spring.features.account.domain.api.AccountId
import de.richargh.sandbox.hypermedia.with.spring.features.account.domain.internal.InMemoryAccounts
import de.richargh.sandbox.hypermedia.with.spring.features.owners.domain.api.Owner
import de.richargh.sandbox.hypermedia.with.spring.features.owners.domain.api.OwnerId
import de.richargh.sandbox.hypermedia.with.spring.features.owners.domain.internal.InMemoryOwners
import org.springframework.context.support.GenericApplicationContext

fun addInitialData(context: GenericApplicationContext) {
    addOwnerData(context)
    addAccountData(context)
}

private fun addOwnerData(context: GenericApplicationContext) {
    val owners = context.getBean(InMemoryOwners::class.java)

    owners += Owner(OwnerId("1"), "Alex")
    owners += Owner(OwnerId("2"), "Taylor")
}

private fun addAccountData(context: GenericApplicationContext) {
    val accounts = context.getBean(InMemoryAccounts::class.java)

    accounts += Account(AccountId("1"), OwnerId("1"), "monthly spendings", 10)
    accounts += Account(AccountId("2"), OwnerId("2"), "monthly spendings", 50)
    accounts += Account(AccountId("3"), OwnerId("2"), "rainy-day", 100)
}
