package de.richargh.sandbox.hypermedia.with.spring.features.account.web

import de.richargh.sandbox.hypermedia.with.spring.features.owners.domain.dto.OwnerDto
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.LinkRelation

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerTest {

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
        Assertions.assertThat(content.map { it["_id"] }).containsExactlyInAnyOrder("1", "2", "3")
        Assertions.assertThat(result.links.map { it.rel }).contains(LinkRelation.of("self"))
    }

    @Test
    fun shouldReturnPagedAccountCollectionDto() {
        // WHEN
        val result = restTemplate.getForObject(searchAccountsUrl(limit = 1, offset = 1), CollectionModel::class.java)
        // THEN
        val content = result.content as Collection<Map<*, *>>
        Assertions.assertThat(content.map { it["_id"] }).containsExactlyInAnyOrder("2")
        val linkRelations = result.links.map { it.rel }
        Assertions.assertThat(linkRelations).contains(LinkRelation.of("self"))
        Assertions.assertThat(linkRelations).contains(LinkRelation.of("previous"))
        Assertions.assertThat(linkRelations).contains(LinkRelation.of("next"))
    }

    @Test
    fun shouldReturnValidAccountDto() {
        // GIVEN
        val accountId = "1"
        // WHEN
        val result = restTemplate.getForObject(accountUrl(accountId), OwnerDto::class.java)
        // THEN
        Assertions.assertThat(result._id).isEqualTo(accountId)
        Assertions.assertThat(result.name).isNotBlank()
    }

    private fun accountsUrl() = "http://localhost:$port/accounts"
    private fun searchAccountsUrl(limit: Int, offset: Int) = "${accountsUrl()}?limit=$limit&offset=$offset"
    private fun accountUrl(accoundId: String) = "${accountsUrl()}/$accoundId"
}