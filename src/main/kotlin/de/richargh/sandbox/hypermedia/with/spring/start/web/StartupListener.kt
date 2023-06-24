package de.richargh.sandbox.hypermedia.with.spring.start.web

import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.context.support.GenericApplicationContext

class StartupListener(private val context: GenericApplicationContext) {
    @EventListener
    fun onApplicationEvent(event: ContextRefreshedEvent?) {
        addInitialData(context)
    }
}