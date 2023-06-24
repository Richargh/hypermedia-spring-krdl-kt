package de.richargh.sandbox.hypermedia.with.spring

import de.richargh.sandbox.hypermedia.with.spring.start.web.rootBeans
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext

class BeansInitializer : ApplicationContextInitializer<GenericApplicationContext> {

    override fun initialize(context: GenericApplicationContext) {
        rootBeans().initialize(context)
    }

}
