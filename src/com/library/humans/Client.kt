package com.library.humans

import kotlin.random.Random

data class Client(
    val id: String,
    override val name: String,
    override val surname: String,
) : Human<Client.Result, Client.Task>(id, name, surname) {

    override val startMessage: String? = null
    override val endMessage: String = "`${this.name} ${this.surname}` покинул библиотеку."

    override fun onResult(result: Result) {
        when (result) {
            Result.TookTheBook -> Unit
        }
    }

    fun decisionToBorrowABook(librarian: Librarian) {
        if (Random.nextBoolean()) {
            addTask(Task.TakeTheOfferedBook(client = this, librarian = librarian))
        } else {
            println("`${this.name} ${this.surname}` отказался от предложения.")
            librarian.releaseTheClient(this)
        }
    }

    sealed class Task : Human.Task<Result> {
        class TakeTheOfferedBook(
            val client: Client,
            val librarian: Librarian,
        ) : Task() {
            override fun execute(): Result {
                println(
                    "${client.name} ${client.surname} берёт книгу " +
                            "предложенную библиотекарем ${librarian.name} ${librarian.surname}"
                )
                librarian.giveRandomBook(client)
                return Result.TookTheBook
            }
        }
    }

    sealed class Result : Human.Task.Result {
        object TookTheBook : Result()
    }
}
