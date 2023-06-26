package de.richargh.sandbox.hypermedia.with.spring.features.owners.domain.translate

import de.richargh.sandbox.hypermedia.with.spring.features.owners.domain.api.Owner
import de.richargh.sandbox.hypermedia.with.spring.features.owners.domain.dto.OwnerDto

fun Owner.toDto() = OwnerDto(
        id.rawValue,
        name
)