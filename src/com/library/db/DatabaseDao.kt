package com.library.db

import com.library.db.entities.BookEntity
import com.library.db.entities.ClientEntity
import com.library.db.entities.OwnershipEntity
import com.library.db.entities.OwnershipEntity.Companion.assign

class DatabaseDao(private val database: Database) {

    fun getAllClients(): List<ClientEntity> {
        return database.clients
    }

    fun getAllBooks(): List<BookEntity> {
        return database.books
    }

    fun getOwnershipInfo(): List<OwnershipEntity> {
        return database.ownership
    }

    fun addBookToClient(clientId: String, bookId: String) {
        var ownership: OwnershipEntity? = database.ownership
            .firstOrNull { it.client.id == clientId }
        val book = database.books.first { it.id == bookId }
        if (ownership != null) {
            ownership.booksInUse.add(book)
        } else {
            val client = database.clients.first { it.id == clientId }
            ownership = client assign book
        }
        database.ownership.add(ownership)
    }

    fun confiscateBooks(clientId: String) {
        database.ownership.firstOrNull { it.client.id == clientId }?.let {
            database.ownership.remove(it)
        }
    }
}
