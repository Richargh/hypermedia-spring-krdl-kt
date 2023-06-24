package de.richargh.sandbox.hypermedia.with.spring.start.web

import de.richargh.sandbox.hypermedia.with.spring.features.account.domain.api.Account
import de.richargh.sandbox.hypermedia.with.spring.features.account.domain.api.AccountId
import de.richargh.sandbox.hypermedia.with.spring.features.account.domain.internal.InMemoryAccounts
import org.springframework.context.support.GenericApplicationContext

fun addInitialData(context: GenericApplicationContext){
    addAccountData(context)
}

private fun addAccountData(context: GenericApplicationContext){
    val accounts = context.getBean(InMemoryAccounts::class.java)

    accounts += Account(AccountId("1"), "one", 10)
    accounts += Account(AccountId("2"), "two", 50)
    accounts += Account(AccountId("3"), "three", 100)
}