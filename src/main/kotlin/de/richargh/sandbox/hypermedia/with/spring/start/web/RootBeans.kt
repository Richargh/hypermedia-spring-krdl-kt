package de.richargh.sandbox.hypermedia.with.spring.start.web

import de.richargh.sandbox.hypermedia.with.spring.features.account.domain.AccountFacade
import de.richargh.sandbox.hypermedia.with.spring.features.account.domain.internal.InMemoryAccounts
import de.richargh.sandbox.hypermedia.with.spring.features.owners.domain.OwnerFacade
import de.richargh.sandbox.hypermedia.with.spring.features.owners.domain.internal.InMemoryOwners
import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.context.support.beans
import org.springframework.hateoas.UriTemplate
import org.springframework.hateoas.mediatype.hal.CurieProvider
import org.springframework.hateoas.mediatype.hal.DefaultCurieProvider
import org.springframework.web.filter.ForwardedHeaderFilter

fun rootBeans() = beans {
    bean<StartupListener>()

    webBeans()

    accountBeans()

    ownerBeans()
}

private fun BeanDefinitionDsl.webBeans() {
    bean<CurieProvider> {
        DefaultCurieProvider("ra", UriTemplate.of("http://localhost:8080/rels/{rel}"))
    }
    bean<ForwardedHeaderFilter>()
}

private fun BeanDefinitionDsl.accountBeans() {
    bean<AccountFacade>()
    bean<InMemoryAccounts>()
}

private fun BeanDefinitionDsl.ownerBeans() {
    bean<OwnerFacade>()
    bean<InMemoryOwners>()
}