package de.richargh.sandbox.hypermedia.with.spring.features.owners.domain.internal

import de.richargh.sandbox.hypermedia.with.spring.commons.error.ItemNotFound
import de.richargh.sandbox.hypermedia.with.spring.features.owners.domain.api.Owner
import de.richargh.sandbox.hypermedia.with.spring.features.owners.domain.api.OwnerId
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap

class InMemoryOwners {
    private val items = ConcurrentHashMap<OwnerId, Owner>()

    fun all(): Sequence<Owner> {
        return items.values.asSequence()
    }

    fun count(): Int {
        return items.count()
    }

    operator fun get(id: OwnerId): Owner {
        val item = items[id]
        if (item != null)
            return item.copy()
        else {
            System.err.println("Could not find account with id " + id.rawValue)
            throw ItemNotFound(id.rawValue, "Account")
        }
    }

    operator fun set(id: OwnerId, owner: Owner): Owner {
        items[id] = owner.copy()
        return owner
    }

    operator fun plusAssign(owner: Owner) {
        items[owner.id] = owner.copy()
    }

    operator fun minusAssign(owner: Owner) {
        items.remove(owner.id)
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(InMemoryOwners::class.java)
    }
}