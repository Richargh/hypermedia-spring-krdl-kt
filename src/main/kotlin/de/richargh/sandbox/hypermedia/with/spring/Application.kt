package de.richargh.sandbox.hypermedia.with.spring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.hateoas.UriTemplate
import org.springframework.hateoas.mediatype.hal.CurieProvider
import org.springframework.hateoas.mediatype.hal.DefaultCurieProvider
import org.springframework.web.filter.ForwardedHeaderFilter


@SpringBootApplication
class Application {
	@Bean
	fun curieProvider(): CurieProvider? {
		return DefaultCurieProvider("ra", UriTemplate.of("http://localhost:8080/rels/{rel}"))
	}

	@Bean
	fun forwardedHeaderFilter(): ForwardedHeaderFilter? {
		return ForwardedHeaderFilter()
	}
}

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}
