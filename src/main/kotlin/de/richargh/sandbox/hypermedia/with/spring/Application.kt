package de.richargh.sandbox.hypermedia.with.spring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.hateoas.UriTemplate
import org.springframework.hateoas.mediatype.hal.CurieProvider
import org.springframework.hateoas.mediatype.hal.DefaultCurieProvider


@SpringBootApplication
class Application {
	@Bean
	fun curieProvider(): CurieProvider? {
		return DefaultCurieProvider("ra", UriTemplate.of("http://localhost:8080/rels/{rel}"))
	}
}

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}
