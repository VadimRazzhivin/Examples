package com.library.db.entities

import java.io.Serializable
import java.util.*

data class OwnershipEntity(
    val client: ClientEntity,
    val booksInUse: MutableList<BookEntity>,
) : Serializable {
    companion object {
        infix fun ClientEntity.assign(books: MutableList<BookEntity>): OwnershipEntity {
            return OwnershipEntity(
                client = this,
                booksInUse = Collections.synchronizedList(books),
            )
        }

        infix fun ClientEntity.assign(books: BookEntity): OwnershipEntity {
            return OwnershipEntity(
                client = this,
                booksInUse = Collections.synchronizedList(mutableListOf(books)),
            )
        }
    }
}
