package com.library.core

import com.library.db.repository.Repository
import com.library.humans.Client
import com.library.humans.Librarian

class Library(val repository: Repository) {

    private var librarians: List<Librarian> = emptyList()

    fun start(librarians: List<Librarian>) {
        this.librarians = librarians
    }

    fun clientVisit(client: Client) {
        val firstAvailableLibrarian = firstAvailableLibrarian()
        when {
            repository.clientWithoutBooksAndWishes(client)-> {
                firstAvailableLibrarian.offerBook(client)
            }
            repository.clientsWithBooks(client) -> {
                firstAvailableLibrarian.confiscateBooks(client)
            }
            else -> {
                firstAvailableLibrarian.giveBookIfPossible(client)
            }
        }
    }

    fun finish(clients: List<Client>) {
        clients.forEach {
            it.await()
        }
        librarians.forEach {
            it.finish()
        }
    }

    private fun firstAvailableLibrarian(): Librarian {
        val libraryMan = librarians
            .filter { !it.workWithClient.get() }
            .shuffled()
            .minByOrNull { it.busyLevel }
        if (libraryMan == null) {
            Thread.sleep(10)
            return firstAvailableLibrarian()
        }
        return libraryMan
    }
}
