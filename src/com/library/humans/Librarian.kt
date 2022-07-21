package com.library.humans

import com.library.core.Book
import com.library.db.repository.Repository
import java.util.concurrent.atomic.AtomicBoolean

class Librarian(
    id: String,
    name: String,
    surname: String,
    private val repository: Repository,
) : Human<Librarian.Result, Librarian.Task>(id, name, surname) {

    val workWithClient = AtomicBoolean(false)

    override val startMessage: String = "`${this.name} ${this.surname}` на рабочем месте"
    override val endMessage: String = "`${this.name} ${this.surname}` окончила работу"

    override fun onResult(result: Result) {
        when (result) {
            is Result.BookIsConfiscated -> {
                if (repository.wishBooks(result.client).isNotEmpty()) {
                    giveBooksIfPossible(result.client)
                } else {
                    offerBook(result.client)
                }
            }
            Result.BookOffered -> Unit
            is Result.ReleaseTheClient -> releaseTheClient(result.client)
        }
    }

    fun confiscateBooks(client: Client) {
        this.workWithClient.set(true)
        addTask(Task.ConfiscateBooks(client = client, librarian = this, repository = repository))
    }

    fun giveBooksIfPossible(client: Client) {
        this.workWithClient.set(true)
        addTask(Task.GiveABookIfYouHaveOne(
            client = client,
            librarian = this,
            repository = repository,
        ))
    }

    fun offerBook(client: Client) {
        this.workWithClient.set(true)
        addTask(Task.OfferABook(client = client, librarian = this))
    }

    fun giveRandomBook(client: Client) {
        addTask(Task.GiveRandomBook(client = client, repository = repository))
    }

    private fun releaseTheClient(client: Client) {
        client.finish()
        workWithClient.set(false)
    }

    sealed class Task : Human.Task<Result> {
        class ConfiscateBooks(
            private val client: Client,
            private val librarian: Librarian,
            private val repository: Repository,
        ) : Task() {
            override fun execute(): Result {
                println("`${client.name} ${client.surname}` возвращает книги.")
                repository.confiscateBooks(client)
                println(
                    "Библиотекарь `${librarian.name} ${librarian.surname}` " +
                            "забрала книги у `${client.name} ${client.surname}`"
                )
                return Result.BookIsConfiscated(client)
            }
        }

        class GiveABookIfYouHaveOne(
            private val client: Client,
            private val librarian: Librarian,
            private val repository: Repository,
        ) : Task() {
            override fun execute(): Result {
                val wishBooks = repository.wishBooks(client)
                if (wishBooks.isEmpty()) {
                    println("Список желаемых книг пуст.")
                    return Result.ReleaseTheClient(client)
                } else {
                    wishBooks.forEach { necessaryBook: Book ->
                        println("`${client.name} ${client.surname}` хочет взять книгу `$necessaryBook`.")
                        val isAvailable = repository.isBookAvailable(necessaryBook)
                        if (isAvailable) {
                            repository.addBookToClient(client, necessaryBook)
                            repository.removeFromWishes(client, necessaryBook)
                            println(
                                "Библиотекарь `${librarian.name} ${librarian.surname}`" +
                                        "посетителю `${client.name} ${client.surname}`"
                            )
                        } else {
                            println("Книга `$necessaryBook` отсутствует.")
                        }
                    }
                    return Result.ReleaseTheClient(client)
                }
            }
        }

        class OfferABook(
            private val client: Client,
            private val librarian: Librarian,
        ) : Task() {
            override fun execute(): Result {
                println("Библиотекарь `${librarian.name} ${librarian.surname}` предлагает `${client.name} ${client.surname}` взять другую книгу")
                val clientAgreed = client.decisionToBorrowABook(librarian)
                return if (clientAgreed) {
                    Result.BookOffered
                } else {
                    Result.ReleaseTheClient(client)
                }
            }
        }

        class GiveRandomBook(
            private val client: Client,
            private val repository: Repository,
        ) : Task() {
            override fun execute(): Result {
                val randomBook: Book? = repository.randomAvailableBookOrNull()
                if (randomBook == null) {
                    println("К сожалению книг нет.")
                } else {
                    repository.addBookToClient(client, randomBook)
                    println("`${client.name} ${client.surname}` взял предложенную книгу `${randomBook.title}`")
                }
                return Result.ReleaseTheClient(client)
            }
        }
    }

    sealed class Result : Human.Task.Result {
        class BookIsConfiscated(val client: Client) : Result()
        class ReleaseTheClient(val client: Client) : Result()
        object BookOffered : Result()
    }
}
