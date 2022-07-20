package com.library.db.entities

import java.io.Serializable

data class OwnershipEntity(
    val client: ClientEntity,
    val booksInUse: MutableSet<BookEntity>,
) : Serializable

