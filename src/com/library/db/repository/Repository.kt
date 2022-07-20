package com.library.db.repository

import com.library.core.Book
import com.library.db.DatabaseDao
import com.library.db.entities.BookEntity
import com.library.db.entities.ClientEntity
import com.library.db.entities.OwnershipEntity
import com.library.humans.Client

class Repository(
    private val dao: DatabaseDao,
    private val wishGenerator: WishGenerator,
) {

    fun clients(): List<Client> {
        return dao.getAllClients().map { it.toClient() }
    }

    fun clientWithoutBooksAndWishes(client: Client): Boolean {
        val clientsWithBooks = clientsWithBooks(client)
        val wishABook = wishGenerator.wishBooks[client.id]?.isNotEmpty() == true
        return !clientsWithBooks && wishABook
    }

    fun clientsWithBooks(client: Client): Boolean {
        val ownershipEntity: OwnershipEntity? = dao.getOwnershipInfo()
            .find { it.client.id == client.id }
        return ownershipEntity != null
    }

    fun randomAvailableBookOrNull(): Book? {
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
        return wishGenerator.wishBooks[client.id].orEmpty().map { it.toBook() }
    }

    fun isBookAvailable(book: Book): Boolean {
        val booksInUse = dao.getOwnershipInfo().flatMap { it.booksInUse }
        return !booksInUse.map { it.id }.contains(book.id)
    }

    fun removeFromWishes(client: Client, book: Book) {
        wishGenerator.wishBooks[client.id]?.removeIf {
            it.id == book.id
        }
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
}
