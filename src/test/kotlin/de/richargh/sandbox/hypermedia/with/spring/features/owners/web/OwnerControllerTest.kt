package de.richargh.sandbox.hypermedia.with.spring.features.owners.web

import de.richargh.sandbox.hypermedia.with.spring.features.owners.domain.dto.OwnerDto
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
        // WHEN
        val result = restTemplate.getForObject(ownersUrl(), CollectionModel::class.java)
        // THEN
        val content = result.content as Collection<Map<*, *>>
        assertThat(content.map { it["_id"] }).containsExactlyInAnyOrder("1", "2")
        val linkRelations = result.links.map { it.rel }
        assertThat(linkRelations).contains(LinkRelation.of("self"))
        assertThat(linkRelations).doesNotContain(LinkRelation.of("previous"))
        assertThat(linkRelations).doesNotContain(LinkRelation.of("next"))
    }

    @Test
    fun shouldReturnPagedAccountCollectionDto() {
        // WHEN
        val result = restTemplate.getForObject(searchOwnersUrl(limit = 1, offset = 1), CollectionModel::class.java)
        // THEN
        val content = result.content as Collection<Map<*, *>>
        assertThat(content.map { it["_id"] }).containsExactlyInAnyOrder("2")
        val linkRelations = result.links.map { it.rel }
        assertThat(linkRelations).contains(LinkRelation.of("self"))
        assertThat(linkRelations).contains(LinkRelation.of("previous"))
        assertThat(linkRelations).doesNotContain(LinkRelation.of("next"))
    }

    @Test
    fun shouldReturnValidOwnerDto() {
        // GIVEN
        val accountId = "1"
        // WHEN
        val result = restTemplate.getForObject(ownersUrl(accountId), OwnerDto::class.java)
        // THEN
        assertThat(result._id).isEqualTo(accountId)
        assertThat(result.name).isNotBlank()
    }

    private fun ownersUrl() = "http://localhost:$port/owners"
    private fun searchOwnersUrl(limit: Int, offset: Int) = "${ownersUrl()}?limit=$limit&offset=$offset"
    private fun ownersUrl(ownerId: String) = "${ownersUrl()}/$ownerId"
}