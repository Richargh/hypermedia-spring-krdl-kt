package de.richargh.sandbox.hypermedia.with.spring.features.account.domain.internal

import de.richargh.sandbox.hypermedia.with.spring.commons.search.SearchParams
import de.richargh.sandbox.hypermedia.with.spring.features.account.domain.api.Account
import de.richargh.sandbox.hypermedia.with.spring.features.account.domain.api.AccountId
import de.richargh.sandbox.hypermedia.with.spring.features.owners.domain.api.OwnerId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class InMemoryAccountsTest {

    val subject = InMemoryAccounts()

    @Test
    fun `should be initially empty`() {
        // THEN
        val result = subject.count()
        assertThat(result).isEqualTo(0)
    }

    @Nested
    inner class `With one Item` {

        val newItem = anAccount()

        @BeforeEach
        fun setup() {
            // GIVEN
            subject += newItem
        }


        @Test
        fun `count should by one`() {
            // WHEN
            val result = subject.count()
            // THEN
            assertThat(result).isEqualTo(1)
        }

        @Test
        fun `search should contain the item`() {
            // WHEN
            val result = subject.search(SearchParams.ofDefault())
            // THEN
            assertThat(result.items).containsExactly(newItem)
        }

        @Test
        fun `search should not have any previous`() {
            // WHEN
            val result = subject.search(SearchParams.ofDefault())
            // THEN
            assertThat(result.hasPrevious).isFalse()
        }

        @Test
        fun `search should not have any next`() {
            // WHEN
            val result = subject.search(SearchParams.ofDefault())
            // THEN
            assertThat(result.hasNext).isFalse()
        }
    }

    @Nested
    inner class `With 10 items` {
        @BeforeEach
        fun setup() {
            // GIVEN
            repeat(10) {
                subject += anAccount()
            }
        }

        @Test
        fun `count should be 10`() {
            // WHEN
            val result = subject.count()
            // THEN
            assertThat(result).isEqualTo(10)
        }

        @Test
        fun `search should return all 10 items`() {
            // WHEN
            val result = subject.search(SearchParams.ofDefault())
            // THEN
            assertThat(result.items).hasSize(10)
        }

        @Test
        fun `search with offset 8 should return last two items`() {
            // WHEN
            val result = subject.search(SearchParams.of(limit = 10, offset = 8))
            // THEN
            assertThat(result.items.map { it.id }).containsExactly(AccountId("9"), AccountId("10"))
        }

        @Test
        fun `search with offset 8 should have previous but no next`() {
            // WHEN
            val result = subject.search(SearchParams.of(limit = 10, offset = 8))
            // THEN
            assertThat(result.hasPrevious).isTrue()
            assertThat(result.hasNext).isFalse()
        }

        @Test
        fun `search with limit 2 should return first two items`() {
            // WHEN
            val result = subject.search(SearchParams.of(limit = 2, offset = 0))
            // THEN
            assertThat(result.items.map { it.id }).containsExactly(AccountId("1"), AccountId("2"))
        }

        @Test
        fun `search with limit 2 should have next but not previous`() {
            // WHEN
            val result = subject.search(SearchParams.of(limit = 2, offset = 0))
            // THEN
            assertThat(result.hasPrevious).isFalse()
            assertThat(result.hasNext).isTrue()
        }

        @Test
        fun `search with limit 1 and offset 4 should have only the middle item`() {
            // WHEN
            val result = subject.search(SearchParams.of(limit = 1, offset = 4))
            // THEN
            assertThat(result.items.map { it.id }).containsExactly(AccountId("5"))
        }

        @Test
        fun `search with limit 1 and offset 4 should have previous and next`() {
            // WHEN
            val result = subject.search(SearchParams.of(limit = 1, offset = 4))
            // THEN
            assertThat(result.hasPrevious).isTrue()
            assertThat(result.hasNext).isTrue()
        }
    }

    private fun anAccount(balance: Int = 10) = Account(
            AccountId((subject.count() + 1).toString()),
            OwnerId("1"),
            "Savings",
            balance)
}