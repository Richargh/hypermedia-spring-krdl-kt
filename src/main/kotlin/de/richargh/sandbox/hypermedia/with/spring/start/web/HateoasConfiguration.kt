package de.richargh.sandbox.hypermedia.with.spring.start.web

import org.springframework.context.annotation.Configuration
import org.springframework.hateoas.config.EnableHypermediaSupport
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType

@Configuration
@EnableHypermediaSupport(type = [HypermediaType.HAL, HypermediaType.HAL_FORMS])
class HateoasConfiguration {

}