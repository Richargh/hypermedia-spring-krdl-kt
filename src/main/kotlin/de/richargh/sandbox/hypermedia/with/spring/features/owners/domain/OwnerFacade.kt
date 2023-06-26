package de.richargh.sandbox.hypermedia.with.spring.features.owners.domain

import de.richargh.sandbox.hypermedia.with.spring.features.owners.domain.api.Owner
import de.richargh.sandbox.hypermedia.with.spring.features.owners.domain.api.OwnerAffordance
import de.richargh.sandbox.hypermedia.with.spring.features.owners.domain.api.OwnerId
import de.richargh.sandbox.hypermedia.with.spring.features.owners.domain.internal.InMemoryOwners

class OwnerFacade(
        private val owners: InMemoryOwners
) {

    fun all(): Sequence<Owner> {
        return owners.all()
    }

    operator fun get(id: OwnerId): Pair<Owner, List<OwnerAffordance>> {
        val account = owners[id]

        return Pair(account, emptyList())
    }
}