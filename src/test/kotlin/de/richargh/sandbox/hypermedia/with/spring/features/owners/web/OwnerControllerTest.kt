package de.richargh.sandbox.hypermedia.with.spring.features.owners.web

import de.richargh.sandbox.hypermedia.with.spring.features.account.domain.dto.AccountDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.LinkRelation


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class OwnerControllerTest {

    @Value(value = "\${local.server.port}")
    private val port = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Test
    fun shouldReturnValidAccountCollectionDto() {
        // GIVEN
        val accountId = "1"
        // WHEN
        val result = restTemplate.getForObject(accountsUrl(), CollectionModel::class.java)
        // THEN
        val content = result.content as Collection<Map<*, *>>
        assertThat(content.map { it["_id"] }).containsExactlyInAnyOrder("1", "2", "3")
        assertThat(result.links.map { it.rel }).contains(LinkRelation.of("self"))
    }

    @Test
    fun shouldReturnValidAccountDto() {
        // GIVEN
        val accountId = "1"
        // WHEN
        val result = restTemplate.getForObject(accountUrl(accountId), AccountDto::class.java)
        // THEN
        assertThat(result._id).isEqualTo(accountId)
        assertThat(result._ownerid).isNotBlank()
        assertThat(result.name).isNotBlank()
    }

    private fun accountsUrl() = "http://localhost:$port/accounts"
    private fun accountUrl(accountId: String) = "${accountsUrl()}/$accountId"
}