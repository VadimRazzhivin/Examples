package com.android

import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread
import kotlin.random.Random

/**
 * Библиотека
 *
 * // Часть 1
 * Клиенты радномно посещают библиотеку  от 20 до 40 человек в день.
 * Клиентов обслуживает библиотекарь, одновременно только одного клиента.
 * Клиенты берут книги, если они есть. Если нет, то рандомно, либо уходят,
 * либо берут другие книги, которые предложит библиотекарь.
 * Клиенты могу вернуть книги.
 * Каждой книги в библиотеке несколько экземпляров.
 * Всего в библиотеке 10 видов книг.
 * На смене работают 2 библиотекаря.
 * База данных книг хранится в структурах данных в ОЗУ
 *
 * // Часть 2
 * База данных клиентов и база данных книг хранятся в файлах.
 * В файл сохранять в бинаром виде с помощью механизма сериализации/десериализации, либо в ввиде строк с рукописным парсингом.
 * Пример:
 * Файл libraryDatabase.txt
 * Создаем проект на github + VCS
 */
fun main() {
    val libraryDatabase = Collections.synchronizedMap(
        mapOf(
            Book(id = "LB-001", title = "Книга-Нига", year = "2018") to "LC-005",
            Book(id = "LB-002", title = "Книга-Нига", year = "2018") to "LC-009",
            Book(id = "LB-003", title = "Книга-Нига", year = "2018") to "LC-010",
            Book(id = "LB-004", title = "Книга-Нига", year = "2018") to null,
            Book(id = "LB-005", title = "Ку-Клукс-Книга", year = "1488") to "LC-006",
            Book(id = "LB-006", title = "Ку-Клукс-Книга", year = "1488") to "LC-007",
            Book(id = "LB-007", title = "Ку-Клукс-Книга", year = "1488") to "LC-008",
            Book(id = "LB-008", title = "Ку-Клукс-Книга", year = "1488") to null,
            Book(id = "LB-009", title = "ЖИ есть или нет?", year = "2002") to "LC-011",
            Book(id = "LB-010", title = "ЖИ есть или нет?", year = "2002") to "LC-012",
            Book(id = "LB-011", title = "ЖИ есть или нет?", year = "2002") to "LC-013",
            Book(id = "LB-012", title = "ЖИ есть или нет?", year = "2002") to null,
            Book(
                id = "LB-013",
                title = "Срём не снимая штанов. Для чайниковю.",
                year = "1999"
            ) to "LC-014",
            Book(
                id = "LB-014",
                title = "Срём не снимая штанов. Для чайниковю.",
                year = "1999"
            ) to "LC-015",
            Book(
                id = "LB-015",
                title = "Срём не снимая штанов. Для чайниковю.",
                year = "1999"
            ) to "LC-020",
            Book(
                id = "LB-016",
                title = "Срём не снимая штанов. Для чайниковю.",
                year = "1999"
            ) to null,
            Book(
                id = "LB-017",
                title = "Программист за 3 месяца - ЛЕГКО",
                year = "2022"
            ) to "LC-020",
            Book(
                id = "LB-018",
                title = "Программист за 3 месяца - ЛЕГКО",
                year = "2022"
            ) to null,
            Book(
                id = "LB-019",
                title = "Программист за 3 месяца - ЛЕГКО",
                year = "2022"
            ) to null,
            Book(
                id = "LB-020",
                title = "Программист за 3 месяца - ЛЕГКО",
                year = "2022"
            ) to null,
        )
    ) // Better to use ConcurrentHashMap
    val library = Library(libraryDatabase = libraryDatabase)
    val librarians = listOf(
        Librarian(id = "LM-001", name = "Мотя", surname = "Букварёва", library = library),
        Librarian(id = "LM-002", name = "Марфа", surname = "Иванова", library = library)
    )

    val clients = listOf(
        Client(
            id = "LC-001",
            name = "Вася",
            surname = "Шлёпкин",
        ),
        Client(
            id = "LC-002",
            name = "Петя",
            surname = "Лупа",
            _necessaryBooks = mutableSetOf("ЖИ есть или нет?"),
        ),
        Client(
            id = "LC-003",
            name = "Ваня",
            surname = "Пупа",
            _necessaryBooks = mutableSetOf("ЖИ есть или нет?"),
        ),
        Client(
            id = "LC-004",
            name = "Вова",
            surname = "Рубликов",
            _necessaryBooks = mutableSetOf("Ку-Клукс-Книга"),
        ),
        Client(
            id = "LC-005",
            name = "Саша",
            surname = "Зябликов",
            _booksInUse = mutableSetOf(Book(id = "LB-001", title = "Книга-Нига", year = "2018")),
        ),
        Client(
            id = "LC-006",
            name = "Маша",
            surname = "Простоквашина",
            _booksInUse = mutableSetOf(
                Book(
                    id = "LB-005",
                    title = "Ку-Клукс-Книга",
                    year = "1488"
                )
            ),
        ),
        Client(
            id = "LC-007",
            name = "Наташа",
            surname = "Хлебушкина",
            _booksInUse = mutableSetOf(
                Book(
                    id = "LB-006",
                    title = "Ку-Клукс-Книга",
                    year = "1488"
                )
            ),
        ),
        Client(
            id = "LC-008",
            name = "Даша",
            surname = "Собачкина",
            _booksInUse = mutableSetOf(
                Book(
                    id = "LB-007",
                    title = "Ку-Клукс-Книга",
                    year = "1488"
                )
            ),
        ),
        Client(
            id = "LC-009",
            name = "Ксюша",
            surname = "Магро",
            _booksInUse = mutableSetOf(Book(id = "LB-002", title = "Книга-Нига", year = "2018")),
        ),
        Client(
            id = "LC-010",
            name = "Максим",
            surname = "Родин",
            _booksInUse = mutableSetOf(Book(id = "LB-003", title = "Книга-Нига", year = "2018")),
        ),
        Client(
            id = "LC-011",
            name = "Константин",
            surname = "Евгеньев",
            _booksInUse = mutableSetOf(
                Book(
                    id = "LB-009",
                    title = "ЖИ есть или нет?",
                    year = "2002"
                )
            ),
        ),
        Client(
            id = "LC-012",
            name = "Инокентий",
            surname = "Попугаев",
            _booksInUse = mutableSetOf(
                Book(
                    id = "LB-010",
                    title = "ЖИ есть или нет?",
                    year = "2002"
                )
            ),
        ),
        Client(
            id = "LC-013",
            name = "Эдуард",
            surname = "Радзинский",
            _booksInUse = mutableSetOf(
                Book(
                    id = "LB-011",
                    title = "ЖИ есть или нет?",
                    year = "2002"
                )
            ),
        ),
        Client(
            id = "LC-014",
            name = "Гена",
            surname = "Крокодилов",
            _booksInUse = mutableSetOf(
                Book(
                    id = "LB-013",
                    title = "Срём не снимая штанов. Для чайниковю.",
                    "1999"
                )
            ),
        ),
        Client(
            id = "LC-015",
            name = "Изя",
            surname = "Ватман",
            _booksInUse = mutableSetOf(
                Book(
                    id = "LB-014",
                    title = "Срём не снимая штанов. Для чайниковю.",
                    "1999"
                )
            ),
        ),
        Client(
            id = "LC-016",
            name = "Шлома",
            surname = "Битонман",
            _necessaryBooks = mutableSetOf("Программист за 3 месяца - ЛЕГКО"),
        ),
        Client(
            id = "LC-017",
            name = "Абрам",
            surname = "Голдман",
            _necessaryBooks = mutableSetOf("Программист за 3 месяца - ЛЕГКО"),
        ),
        Client(
            id = "LC-018",
            name = "Мойша",
            surname = "Брумбович",
            _necessaryBooks = mutableSetOf("Программист за 3 месяца - ЛЕГКО"),
        ),
        Client(
            id = "LC-019",
            name = "Вацлав",
            surname = "Цукерман",
            _necessaryBooks = mutableSetOf("Программист за 3 месяца - ЛЕГКО")
        ),
        Client(
            id = "LC-020",
            name = "Коля",
            surname = "Шниерсон",
            _booksInUse = mutableSetOf(
                Book(id = "LB-015", title = "Срём не снимая штанов. Для чайниковю.", year = "1999"),
                Book(id = "LB-017", title = "Программист за 3 месяца - ЛЕГКО", year = "2022")
            ),
        ),
    )

    library.start(
        librarians = librarians,
    )

    val clientsThread = thread(name = "Client thread") {
        clients.forEach { client ->
            println("\t`${client.name} ${client.surname}` зашел в библиотеку.")
            library.clientVisit(client)
            Thread.sleep(Random.nextLong(10, 16))
        }
    }
    clientsThread.join()
    library.finish(clients)
    println("FINISH")
}

class Library(val libraryDatabase: MutableMap<Book, String?>) {

    private var librarians: List<Librarian> = emptyList()

    fun start(librarians: List<Librarian>) {
        this.librarians = librarians
    }

    fun clientVisit(client: Client) {
        val firstAvailableLibrarian = firstAvailableLibrarian()
        when {
            client.necessaryBooks.isEmpty() && client.booksInUse.isEmpty() -> {
                firstAvailableLibrarian.offerBook(client)
            }
            client.booksInUse.isNotEmpty() -> {
                firstAvailableLibrarian.confiscateBooks(client)
            }
            client.necessaryBooks.isNotEmpty() && client.booksInUse.isEmpty() -> {
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

data class Client(
    override val id: String,
    override val name: String,
    override val surname: String,
    private val _booksInUse: MutableSet<Book> = mutableSetOf(),
    private val _necessaryBooks: MutableSet<String> = mutableSetOf(),
) : Human<Client.Result, Client.Task>(id, name, surname) {

    val necessaryBooks: MutableSet<String> = Collections.synchronizedSet(_necessaryBooks)
    val booksInUse: MutableSet<Book> = Collections.synchronizedSet(_booksInUse)

    override val startMessage: String? = null
    override val endMessage: String = "\t\t\t`${this.name} ${this.surname}` покинул библиотеку."

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

class Librarian(
    id: String,
    name: String,
    surname: String,
    private val library: Library,
) : Human<Librarian.Result, Librarian.Task>(id, name, surname) {

    val workWithClient = AtomicBoolean(false)

    override val startMessage: String = "\t`${this.name} ${this.surname}` на рабочем месте"
    override val endMessage: String = "\t\t\t`${this.name} ${this.surname}` окончила работу"

    fun confiscateBooks(client: Client) {
        this.workWithClient.set(true)
        addTask(Task.ConfiscateBooks(client, this))
    }

    fun giveBookIfPossible(client: Client) {
        this.workWithClient.set(true)
        addTask(Task.GiveABookIfYouHaveOne(client = client, librarian = this))
    }

    fun offerBook(client: Client) {
        this.workWithClient.set(true)
        addTask(Task.OfferABook(client, this))
    }

    fun giveRandomBook(client: Client) {
        addTask(Task.GiveRandomBook(client, this))
    }

    fun releaseTheClient(client: Client) {
        client.finish()
        workWithClient.set(false)
    }

    override fun onResult(result: Result) {
        when (result) {
            is Result.BookInTheLibrary -> {
                if (result.client.necessaryBooks.isNotEmpty()) {
                    giveBookIfPossible(result.client)
                } else {
                    offerBook(result.client)
                }
            }
            Result.OfferABook -> Unit
            is Result.BookHanded -> {
                if (result.client.necessaryBooks.isNotEmpty()) {
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
        ) : Task() {
            override fun execute(): Result {
                client.booksInUse.forEach { clientsBook: Book ->
                    println("`${client.name} ${client.surname}` возвращает книгу: `${clientsBook.title}`.")
                    if (librarian.library.libraryDatabase.containsKey(clientsBook)) {
                        librarian.library.libraryDatabase[clientsBook] = null
                    }
                }
                client.booksInUse.clear()
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
        ) : Task() {
            override fun execute(): Result {
                client.necessaryBooks.forEach { necessaryBook: String ->
                    println("`${client.name} ${client.surname}` хочет взять книгу `$necessaryBook`.")
                    val freeBook: Book? = librarian.library.libraryDatabase
                        .toList()
                        .firstOrNull {
                            it.first.title == necessaryBook && it.second == null
                        }
                        ?.first

                    if (freeBook != null) {
                        client.booksInUse.add(freeBook)
                        librarian.library.libraryDatabase[freeBook] = client.id
                        println(
                            "Библиотекарь `${librarian.name} ${librarian.surname}` выдал книгу `${freeBook.title}` " +
                                    "посетителю `${client.name} ${client.surname}`"
                        )
                        client.necessaryBooks.remove(necessaryBook)
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
        ) : Task() {
            override fun execute(): Result {
                val randomBook: Book? = librarian.library.libraryDatabase
                    .filter { it.value == null }
                    .toList()
                    .randomOrNull()
                    ?.first

                if (randomBook == null) {
                    println("К сожалению книг нет.")
                    librarian.releaseTheClient(client)
                } else {
                    client.booksInUse.add(randomBook)
                    librarian.library.libraryDatabase[randomBook] = client.id
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

data class Book(
    val id: String,
    val title: String,
    val year: String,
)

// region HUMAN
abstract class Human<R : Human.Task.Result, T : Human.Task<R>>(
    open val id: String,
    open val name: String,
    open val surname: String,
) {
    open val startMessage: String? = null
    open val endMessage: String? = null

    private val commandQueue = ConcurrentLinkedQueue<T>()
    val busyLevel: Int get() = commandQueue.size

    private val work = AtomicBoolean(true)

    private val thread: Thread

    init {
        thread = thread(name = "Thread human {$id}") {
            startMessage?.let { println(it) }
            while (work.get()) {
                val task: T? = commandQueue.poll()
                task?.execute()?.let(::onResult)
                Thread.sleep(10)
            }
        }
    }

    fun finish() {
        work.set(false)
        thread.join()
        endMessage?.let { println(it) }
    }

    fun await() {
        thread.join()
    }

    fun addTask(task: T) {
        commandQueue.add(task)
    }

    abstract fun onResult(result: R)

    interface Task<out R : Task.Result> {
        fun execute(): R
        interface Result
    }
}
// endregion
