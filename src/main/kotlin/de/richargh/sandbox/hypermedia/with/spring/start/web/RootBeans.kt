package de.richargh.sandbox.hypermedia.with.spring.start.web

import de.richargh.sandbox.hypermedia.with.spring.features.account.domain.AccountFacade
import de.richargh.sandbox.hypermedia.with.spring.features.account.domain.internal.InMemoryAccounts
import de.richargh.sandbox.hypermedia.with.spring.features.owners.domain.OwnerFacade
import de.richargh.sandbox.hypermedia.with.spring.features.owners.domain.internal.InMemoryOwners
import org.springframework.context.support.beans

fun rootBeans() = beans {
    bean<StartupListener>()

    bean<AccountFacade>()
    bean<InMemoryAccounts>()

    bean<OwnerFacade>()
    bean<InMemoryOwners>()
}