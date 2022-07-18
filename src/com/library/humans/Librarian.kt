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

    override val startMessage: String = "\t`${this.name} ${this.surname}` на рабочем месте"
    override val endMessage: String = "\t\t\t`${this.name} ${this.surname}` окончила работу"

    fun confiscateBooks(client: Client) {
        this.workWithClient.set(true)
        addTask(Task.ConfiscateBooks(client = client, librarian = this, repository = repository))
    }

    fun giveBookIfPossible(client: Client) {
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
        addTask(Task.GiveRandomBook(client = client, librarian = this, repository = repository))
    }

    fun releaseTheClient(client: Client) {
        client.finish()
        workWithClient.set(false)
    }

    override fun onResult(result: Result) {
        when (result) {
            is Result.BookInTheLibrary -> {
                if (repository.wishBooks(result.client).isNotEmpty()) {
                    giveBookIfPossible(result.client)
                } else {
                    offerBook(result.client)
                }
            }
            Result.OfferABook -> Unit
            is Result.BookHanded -> {
                if (repository.wishBooks(result.client).isNotEmpty()) {
                    offerBook(result.client)
                } else {
                    releaseTheClient(result.client)
                }
            }
        }
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
                return Result.BookInTheLibrary(client)
            }
        }

        class GiveABookIfYouHaveOne(
            private val client: Client,
            private val librarian: Librarian,
            private val repository: Repository,
        ) : Task() {
            override fun execute(): Result {
                repository.wishBooks(client).forEach { necessaryBook: Book ->
                    println("`${client.name} ${client.surname}` хочет взять книгу `$necessaryBook`.")
                    val isAvailable = repository.isBookAvailable(necessaryBook)
                    if (isAvailable) {
                        repository.addBookToClient(client, necessaryBook)
                        repository.removeFromWishes(client, necessaryBook)
                        println(
                            "Библиотекарь `${librarian.name} ${librarian.surname}`" +
                                    "посетителю `${client.name} ${client.surname}`"
                        )
                    } else println("Книга `$necessaryBook` отсутствует.")
                }

                return Result.BookHanded(client)
            }
        }

        class OfferABook(
            private val client: Client,
            private val librarian: Librarian,
        ) : Task() {
            override fun execute(): Result {
                println("Библиотекарь `${librarian.name} ${librarian.surname}` предлагает `${client.name} ${client.surname}` взять другую книгу")
                client.decisionToBorrowABook(librarian)
                return Result.OfferABook
            }
        }

        class GiveRandomBook(
            private val client: Client,
            private val librarian: Librarian,
            private val repository: Repository,
        ) : Task() {
            override fun execute(): Result {
                val randomBook: Book? = repository.randomBookOrNull()

                if (randomBook == null) {
                    println("К сожалению книг нет.")
                    librarian.releaseTheClient(client)
                } else {
                    repository.addBookToClient(client, randomBook)
                    println("`${client.name} ${client.surname}` взял предложенную книгу `${randomBook.title}`")
                }
                return Result.BookHanded(client)
            }
        }
    }

    sealed class Result : Human.Task.Result {
        class BookInTheLibrary(val client: Client) : Result()
        class BookHanded(val client: Client) : Result()
        object OfferABook : Result()
    }
}
