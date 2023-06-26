package de.richargh.sandbox.hypermedia.with.spring.commons.search

data class SearchParams(
        val limit: Int,
        val offset: Int
) {
    fun previous() = SearchParams(limit = limit, offset = Math.max(offset - limit, 0))

    fun next() = SearchParams(limit = limit, offset = offset + limit)

    companion object {
        fun of(limit: Int?, offset: Int?) = SearchParams(
                limit = limit ?: DEFAULT_LIMIT,
                offset = offset ?: DEFAULT_OFFSET)

        fun ofDefault() = SearchParams(
                limit = DEFAULT_LIMIT,
                offset = DEFAULT_OFFSET)

        private const val DEFAULT_LIMIT = 10
        private const val DEFAULT_OFFSET = 0
    }
}
