package de.richargh.sandbox.hypermedia.with.spring.commons.search

data class SearchResult<Item>(
        val items: List<Item>,
        val hasNext: Boolean,
        val hasPrevious: Boolean
)