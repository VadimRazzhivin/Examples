package com.library.db.entities

import java.io.Serializable
import java.util.*

data class OwnershipEntity(
    val client: ClientEntity,
    val booksInUse: MutableSet<BookEntity>,
) : Serializable {
    companion object {
        infix fun ClientEntity.assign(books: MutableSet<BookEntity>): OwnershipEntity {
            return OwnershipEntity(
                client = this,
                booksInUse = Collections.synchronizedSet(books),
            )
        }

        infix fun ClientEntity.assign(books: BookEntity): OwnershipEntity {
            return OwnershipEntity(
                client = this,
                booksInUse = Collections.synchronizedSet(mutableSetOf(books)),
            )
        }
    }
}
