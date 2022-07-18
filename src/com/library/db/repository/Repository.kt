package com.library.db.repository

import com.library.core.Book
import com.library.db.DatabaseDao
import com.library.db.entities.BookEntity
import com.library.db.entities.ClientEntity
import com.library.db.entities.OwnershipEntity
import com.library.humans.Client
import kotlin.random.Random

class Repository(private val dao: DatabaseDao) {

    /**
     * key = client.id
     * value = wish books
     */
    private val wishBooks: Map<String, MutableSet<BookEntity>> by lazy {
        dao.getAllClients().map { it.id }.associateWith {
            if (Random.nextBoolean()) {
                mutableSetOf(dao.getAllBooks().random())
            } else {
                mutableSetOf()
            }
        }
    }

    fun clients(): List<Client> {
        return dao.getAllClients().map { it.toClient() }
    }

    private fun BookEntity.toBook(): Book {
        return Book(
            id = id,
            title = title,
            year = year,
        )
    }

    private fun ClientEntity.toClient(): Client {
        return Client(
            id = id,
            surname = surname,
            name = name,
        )
    }

    fun clientWithoutBooksAndWishes(client: Client): Boolean {
        val clientsWithBooks = clientsWithBooks(client)
        val wishABook = wishBooks[client.id]?.isNotEmpty() == true
        return clientsWithBooks && wishABook
    }

    fun clientsWithBooks(client: Client): Boolean {
        val ownershipEntity: OwnershipEntity? = dao.getOwnershipInfo()
            .find { it.client.id == client.id }
        return ownershipEntity != null
    }

    fun randomBookOrNull(): Book? {
        val booksInUse = dao.getOwnershipInfo().flatMap { it.booksInUse }
        return dao.getAllBooks().firstOrNull {
            !booksInUse.contains(it)
        }?.toBook()
    }

    fun addBookToClient(client: Client, randomBook: Book) {
        dao.addBookToClient(client.id, randomBook.id)
    }

    fun confiscateBooks(client: Client) {
        dao.confiscateBooks(client.id)
    }

    fun wishBooks(client: Client): List<Book> {
        return wishBooks[client.id].orEmpty().map { it.toBook() }
    }

    fun isBookAvailable(book: Book): Boolean {
        val booksInUse = dao.getOwnershipInfo().flatMap { it.booksInUse }
        return !booksInUse.map { it.id }.contains(book.id)
    }

    fun removeFromWishes(client: Client, book: Book) {
        wishBooks[client.id]?.removeIf {
            it.id == book.id
        }
    }
}
