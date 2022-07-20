package com.library.db

import com.library.db.entities.BookEntity
import com.library.db.entities.ClientEntity
import com.library.db.entities.OwnershipEntity
import java.util.*

class DatabaseDao(private val database: Database) {

    private val clients = Collections.synchronizedSet(database.clients)
    private val books = Collections.synchronizedSet(database.books)
    private val ownership = Collections.synchronizedSet(database.ownership)

    fun getAllClients(): Set<ClientEntity> {
        return clients
    }

    fun getAllBooks(): Set<BookEntity> {
        return books
    }

    fun getOwnershipInfo(): Set<OwnershipEntity> {
        return ownership
    }

    fun addBookToClient(clientId: String, bookId: String) {
        var ownershipRefresh: OwnershipEntity? = ownership
            .firstOrNull { it.client.id == clientId }
        val book = books.first { it.id == bookId }
        if (ownershipRefresh != null) {
            ownershipRefresh.booksInUse.add(book)
        } else {
            val client = clients.first { it.id == clientId }
            ownershipRefresh = OwnershipEntity(
                client = client,
                booksInUse = mutableSetOf(book),
            )
        }
        ownership.add(ownershipRefresh)
    }

    fun confiscateBooks(clientId: String) {
        ownership.firstOrNull { it.client.id == clientId }?.let {
            ownership.remove(it)
        }
    }
}
