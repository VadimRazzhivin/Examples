package com

import java.security.cert.CertSelector
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.Comparator
import kotlin.concurrent.thread
import kotlin.random.Random

object Sort {

    @JvmStatic
    fun main(args: Array<String>) {

        val pers1 = Person(10, 10)
        val pers2 = Person(20, 20)

        pers1 > pers2

        println(
            List(10) {
                Person(
                    age = kotlin.random.Random.nextInt(40, 45),
                    weight = kotlin.random.Random.nextInt(60, 70),
                )
            }
                .sortedWith(object : Comparator<Person> {
                    override fun compare(o1: Person, o2: Person): Int {
                        if (o1.age < o2.age) return -1
                        if (o1.age == o2.age && o1.weight < o2.weight) return -1
                        return 0
                    }
                })
                .joinToString(separator = "\n")
                { it.toString() }
        )
    }

    data class Person(val age: Int, val weight: Int) : Comparable<Person> {
        override fun compareTo(other: Person): Int {
            TODO("Not yet implemented")
        }
    }
}

object Palindrome {
    @JvmStatic
    fun main(args: Array<String>) {
        val string = "A man, a plan, a canal: Panama"
        println(palindrome(string))
    }

    fun palindrome(string: String): Boolean {
        var palindrome = ""
        string.forEach {
            if (it.isLetterOrDigit()) {
                palindrome += it.toLowerCase()
            }
        }
        if (palindrome.isNotEmpty() || palindrome.length >1) {
            var lost = palindrome.lastIndex
            palindrome.forEach { originIt ->
                if (originIt != palindrome[lost]) {
                    return false
                } else lost--
            }
        }
        return true
    }

}

object Lib {
    @JvmStatic
    fun main(args: Array<String>) {
        val library = Library()
        val libraryManes = listOf(
            LibraryMan(id = "LM-001", name = "Мотя", surname = "Букварёва", library = library),
            LibraryMan(id = "LM-002", name = "Марфа", surname = "Иванова", library = library)
        )

        val clients = listOf(
            Client(id = "LC-001", name = "Вася", surname = "Шлёпкин",
                booksInUse = mutableSetOf(),
                necessaryBooks = mutableSetOf()),
            Client(id = "LC-002", name = "Петя", surname = "Лупа",
                booksInUse = mutableSetOf(),
                necessaryBooks = mutableSetOf("ЖИ есть или нет?")),
            Client(id = "LC-003", name = "Ваня", surname = "Пупа",
                booksInUse = mutableSetOf(),
                necessaryBooks = mutableSetOf("ЖИ есть или нет?")),
            Client(id = "LC-004", name = "Вова", surname = "Рубликов",
                booksInUse = mutableSetOf(),
                necessaryBooks = mutableSetOf("Ку-Клукс-Книга")),
            Client(id = "LC-005", name = "Саша", surname = "Зябликов",
                booksInUse = mutableSetOf(Book(id = "LB-001", title = "Книга-Нига", year = "2018")),
                necessaryBooks = mutableSetOf()),
            Client(id = "LC-006", name = "Маша", surname = "Простоквашина",
                booksInUse = mutableSetOf(Book(id = "LB-005", title = "Ку-Клукс-Книга", year = "1488")),
                necessaryBooks = mutableSetOf()),
            Client(id = "LC-007", name = "Наташа", surname = "Хлебушкина",
                booksInUse = mutableSetOf(Book(id = "LB-006", title = "Ку-Клукс-Книга", year = "1488")),
                necessaryBooks = mutableSetOf()),
            Client(id = "LC-008", name = "Даша", surname = "Собачкина",
                booksInUse = mutableSetOf(Book(id = "LB-007", title = "Ку-Клукс-Книга", year = "1488")),
                necessaryBooks = mutableSetOf()),
            Client(id = "LC-009", name = "Ксюша", surname = "Магро",
                booksInUse = mutableSetOf(Book(id = "LB-002", title = "Книга-Нига", year = "2018")),
                necessaryBooks = mutableSetOf()),
            Client(id = "LC-010", name = "Максим", surname = "Родин",
                booksInUse = mutableSetOf(Book(id = "LB-003", title = "Книга-Нига", year = "2018")),
                necessaryBooks = mutableSetOf()),
            Client(id = "LC-011", name = "Константин", surname = "Евгеньев",
                booksInUse = mutableSetOf(Book(id = "LB-009", title = "ЖИ есть или нет?", year = "2002")),
                necessaryBooks = mutableSetOf()),
            Client(id = "LC-012", name = "Инокентий", surname = "Попугаев",
                booksInUse = mutableSetOf(Book(id = "LB-010", title = "ЖИ есть или нет?", year = "2002")),
                necessaryBooks = mutableSetOf()),
            Client(id = "LC-013", name = "Эдуард", surname = "Радзинский",
                booksInUse = mutableSetOf(Book(id = "LB-011", title = "ЖИ есть или нет?", year = "2002")),
                necessaryBooks = mutableSetOf()),
            Client(id = "LC-014", name = "Гена", surname = "Крокодилов",
                booksInUse = mutableSetOf(Book(id = "LB-013", title = "Срём не снимая штанов. Для чайниковю.", "1999")),
                necessaryBooks = mutableSetOf()),
            Client(id = "LC-015", name = "Изя", surname = "Ватман",
                booksInUse = mutableSetOf(Book(id = "LB-014", title = "Срём не снимая штанов. Для чайниковю.", "1999")),
                necessaryBooks = mutableSetOf()),
            Client(id = "LC-016", name = "Шлома", surname = "Битонман",
                booksInUse = mutableSetOf(),
                necessaryBooks = mutableSetOf("Программист за 3 месяца - ЛЕГКО")),
            Client(id = "LC-017", name = "Абрам", surname = "Голдман",
                booksInUse = mutableSetOf(),
                necessaryBooks = mutableSetOf("Программист за 3 месяца - ЛЕГКО")),
            Client(id = "LC-018", name = "Мойша", surname = "Брумбович",
                booksInUse = mutableSetOf(),
                necessaryBooks = mutableSetOf("Программист за 3 месяца - ЛЕГКО")),
            Client(id = "LC-019", name = "Вацлав", surname = "Цукерман",
                booksInUse = mutableSetOf(),
                necessaryBooks = mutableSetOf("Программист за 3 месяца - ЛЕГКО")),
            Client(id = "LC-020", name = "Коля", surname = "Шниерсон",
                booksInUse = mutableSetOf(
                    Book(id = "LB-015", title = "Срём не снимая штанов. Для чайниковю.", year = "1999"),
                    Book(id = "LB-017", title = "Программист за 3 месяца - ЛЕГКО", year = "2022")),
                necessaryBooks = mutableSetOf()),
        )

        library.databaseOfBooks = mutableMapOf(
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
            Book(id = "LB-013", title = "Срём не снимая штанов. Для чайниковю.", year = "1999") to "LC-014",
            Book(id = "LB-014", title = "Срём не снимая штанов. Для чайниковю.", year = "1999") to "LC-015",
            Book(id = "LB-015", title = "Срём не снимая штанов. Для чайниковю.", year = "1999") to "LC-020",
            Book(id = "LB-016", title = "Срём не снимая штанов. Для чайниковю.", year = "1999") to null,
            Book(id = "LB-017", title = "Программист за 3 месяца - ЛЕГКО", year = "2022") to "LC-020",
            Book(id = "LB-018", title = "Программист за 3 месяца - ЛЕГКО", year = "2022") to null,
            Book(id = "LB-019", title = "Программист за 3 месяца - ЛЕГКО", year = "2022") to null,
            Book(id = "LB-020", title = "Программист за 3 месяца - ЛЕГКО", year = "2022") to null,
        )

        library.start(libraryManes)

        val clientsThread = thread {
            clients.forEach { client ->
                library.clientVisit(client)
                Thread.sleep(Random.nextLong(10, 16))
            }
        }
        clientsThread.join()
        library.finish()
    }

    class Library {

        var databaseOfClients = mutableListOf<Client>()
        var databaseOfBooks = mutableMapOf<Book, String?>()

        private var libraryManes: List<LibraryMan> = emptyList()

        fun start(libraryManes: List<LibraryMan>) {
            this.libraryManes = libraryManes
        }

        fun clientVisit(client: Client) {
            val firstAvailableLibrarian = firstAvailableLibraryManAndAsksForABook()
            when {
                client.necessaryBooks.isEmpty() && client.booksInUse.isEmpty() -> {
                    firstAvailableLibrarian.offerBook(client)
                }
                client.booksInUse.isNotEmpty() -> {
                    firstAvailableLibrarian.confiscateBooks(client)
                }
                client.necessaryBooks.isNotEmpty() && client.booksInUse.isEmpty() -> {
                    firstAvailableLibrarian.availabilityCheckAndIssuance(client)
                }
            }
        }

        fun finish() {
            libraryManes.forEach {
                it.finish()
            }
        }

        private fun firstAvailableLibraryManAndAsksForABook(): LibraryMan {
            val libraryMan = libraryManes
                .filter { !it.workWithClient.get() }
                .shuffled()
                .minByOrNull { it.busyLevel }
            if (libraryMan == null) {
                Thread.sleep(50)
                return firstAvailableLibraryManAndAsksForABook()
            }
            return libraryMan
        }
    }

    data class Client(
        override val id: String,
        override val name: String,
        override val surname: String,
        val booksInUse: MutableSet<Book>,
        val necessaryBooks: MutableSet<String>,
    ) : Human<Client.Result, Client.Task>(id, name, surname) {

        override val startMessage: String = "\t`${this.name} ${this.surname}` зашел в библиотеку."
        override val andMessage: String = "\t\t\t`${this.name} ${this.surname}` покинул библиотеку."

        override fun onResult(result: Result) {
            when (result) {
                Result.TookTheBook -> Unit
            }
        }

        fun decisionToBorrowABook(libraryMan: LibraryMan) {
            if (Random.nextBoolean()) {
                addTask(Task.TakeTheOfferedBook(client = this, libraryMan = libraryMan))
            } else {
                println("`${this.name} ${this.surname}` отказался от предложения.")
                libraryMan.releaseTheClient(this)
            }
        }

        sealed class Task : Human.Task<Result> {
            class TakeTheOfferedBook(
                val client: Client,
                val libraryMan: LibraryMan,
            ) : Task() {
                override fun execut(): Result {
                    println("${client.name} ${client.surname} берёт книгу " +
                            "предложенную библиотекарем ${libraryMan.name} ${libraryMan.surname}")
                    libraryMan.giveRandomBook(client)
                    Thread.sleep(10)
                    return Result.TookTheBook
                }
            }
        }

        sealed class Result : Human.Task.Result {
            object TookTheBook : Result()
        }
    }

    class LibraryMan(
        id: String,
        name: String,
        surname: String,
        private val library: Library,
    ) : Human<LibraryMan.Result, LibraryMan.Task>(id, name, surname) {

        val workWithClient = AtomicBoolean(false)

        override val startMessage: String = "\t`${this.name} ${this.surname}` на рабочем месте"
        override val andMessage: String = "\t\t\t`${this.name} ${this.surname}` окончила работу"

        fun confiscateBooks(client: Client) {
            this.workWithClient.set(true)
            addTask(Task.ConfiscateBook(client, this))
        }

        fun availabilityCheckAndIssuance(client: Client) {
            this.workWithClient.set(true)
            addTask(Task.GiveABookIfYouHaveOne(client, this))
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
                Result.BookInTheLibrary -> Unit
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
            class ConfiscateBook(
                private val client: Client,
                private val libraryMan: LibraryMan,
            ) : Task() {
                override fun execut(): Result {
                    client.booksInUse.forEach { clientesBook: Book ->
                        println("`${client.name} ${client.surname}` возвращает книгу: `${clientesBook.title}`.")
                        synchronized(libraryMan.library.databaseOfBooks) {
                            libraryMan.library.databaseOfBooks.forEach { (book, clientLib) ->
                                if (clientLib != null && client.id == clientLib && clientesBook.id == book.id) {
                                    libraryMan.library.databaseOfBooks[book] = null
                                }
                            }
                        }
                    }
                    client.booksInUse.clear()
                    println("Библиотекарь `${libraryMan.name} ${libraryMan.surname}` " +
                            "забрала книги у `${client.name} ${client.surname}`")
                    if (client.necessaryBooks.isNotEmpty()) {
                        libraryMan.availabilityCheckAndIssuance(client)
                    } else {
                        libraryMan.offerBook(client)
                    }
                    return Result.BookInTheLibrary
                }
            }

            class GiveABookIfYouHaveOne(
                private val client: Client,
                private val libraryMan: LibraryMan,
            ) : Task() {
                override fun execut(): Result {
                    synchronized(libraryMan.library.databaseOfBooks) {
                        client.necessaryBooks.forEach { necessaryBook: String ->
                            println("`${client.name} ${client.surname}` хочет взять книгу `$necessaryBook`.")
                            val freeBooks = libraryMan.library.databaseOfBooks
                                .filterKeys { it.title == necessaryBook }
                                .filterValues { it == null }
                            if (freeBooks.isNotEmpty()) {
                                client.booksInUse.add(freeBooks.keys.first())
                                libraryMan.library.databaseOfBooks[freeBooks.keys.first()] = client.id
                                println("Библиотекарь `${libraryMan.name} ${libraryMan.surname}` выдал книгу `${freeBooks.keys.first().title}` " +
                                        "посетителю `${client.name} ${client.surname}`")
                                client.necessaryBooks.remove(necessaryBook)
                            } else println("Книга `$necessaryBook` отсутствует.")
                        }
                    }
                    return Result.BookHanded(client)
                }
            }

            class OfferABook(
                private val client: Client,
                private val libraryMan: LibraryMan,
            ) : Task() {
                override fun execut(): Result {
                    println("Библиотекарь `${libraryMan.name} ${libraryMan.surname}` предлагает `${client.name} ${client.surname}` взять другую книгу")
                    client.decisionToBorrowABook(libraryMan)
                    return Result.OfferABook
                }
            }

            class GiveRandomBook(
                private val client: Client,
                private val libraryMan: LibraryMan,
            ) : Task() {
                override fun execut(): Result {
                    synchronized(libraryMan.library.databaseOfBooks) {
                        val randomBook = libraryMan.library.databaseOfBooks
                            .filterValues { it == null }
                        if (randomBook.isEmpty()) {
                            println("К сожалению книг нет.")
                        } else {
                            randomBook.keys.shuffled()
                            client.booksInUse.add(randomBook.keys.first())
                            client.booksInUse.forEach { bookInUse ->
                                libraryMan.library.databaseOfBooks.forEach { (book, _) ->
                                    if (bookInUse.id == book.id) {
                                        libraryMan.library.databaseOfBooks[book] = client.id
                                    }
                                }
                            }
                            println("`${client.name} ${client.surname}` взял предложенную книгу `${randomBook.keys.first().title}`")
                        }
                    }
                    return Result.BookHanded(client)
                }
            }
        }

        sealed class Result : Human.Task.Result {
            object BookInTheLibrary : Result()
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
        open val andMessage: String? = null

        private val commandQueue = ConcurrentLinkedQueue<T>()
        val busyLevel: Int get() = commandQueue.size


        private val work = AtomicBoolean(true)

        private val thread: Thread

        init {
            thread = thread {
                startMessage?.let { println(it) }
                while (work.get()) {
                    val task: T? = commandQueue.poll()
                    task?.execut()?.let(::onResult)
                    Thread.sleep(10)
                }
            }
        }

        fun finish() {
            work.set(false)
            thread.join()
            andMessage?.let { println(it) }
        }

        fun addTask(task: T) {
            commandQueue.add(task)
        }

        abstract fun onResult(result: R)

        interface Task<out R : Task.Result> {
            fun execut(): R
            interface Result
        }
    }
// endregion
}


fun main() {

    val randomNumberToString = setOf(Random.nextInt(0, 2000000001).toString().toCharArray())
    val listInt = List(Random.nextInt(1, 11)) { Random.nextInt(0, 10) }.toMutableList()
    val listLong = List(Random.nextInt(1, 11)) { Random.nextLong(0, 10) }
    val listBooleans = List(Random.nextInt(1, 11)) { Random.nextBoolean() }

    val nameAndListInt = ("Лист Интов" to listInt)
    val nameAndListLong = ("Лист Лонгов" to listLong)
    val nameAndListBoolean = ("Лист Булеанов" to listBooleans)
    val nameAndSet = ("Сэт" to randomNumberToString)


    println(
        "Сравнив ${nameAndListInt.first} и ${nameAndListLong.first} " +
                "оказалось что большим по размеру является:" +
                " ${sizeObj(nameAndListInt, nameAndListLong).first} " +
                "[${sizeObj(nameAndListInt, nameAndListLong).second.size}]"
    )

    println(
        "Сравнив ${nameAndListLong.first} и ${nameAndListBoolean.first} " +
                "оказалось что большим по размеру является:" +
                " ${sizeObj(nameAndListLong, nameAndListBoolean).first} " +
                "[${sizeObj(nameAndListLong, nameAndListBoolean).second.size}]"
    )

    println(
        "Сравнив ${nameAndListBoolean.first} и ${nameAndSet.first} " +
                "оказалось что большим по размеру является:" +
                " ${sizeObj(nameAndListBoolean, nameAndSet).first} " +
                "[${sizeObj(nameAndListBoolean, nameAndSet).second.size}]"
    )

    println(listInt)
    println(listInt.sortByCondition { itElement, nextElement -> itElement > nextElement })


}

fun <T> List<T>.sortByCondition(comparator: (T, T) -> Boolean): List<T> {
    val list = this.toMutableList()
    list.forEach { _ ->
        list.forEachIndexed { index: Int, itElement: T ->
            if (index == list.lastIndex) return@forEachIndexed
            val nextElement = list[index + 1]
            if (comparator(itElement, nextElement)) {
                list[index] = nextElement
                list[index + 1] = itElement
            }
        }
    }
    return list
}

fun <T, C : Collection<T>, M : Pair<String, C>> sizeObj(l1: M, l2: M): M {
    return if (l1.second.size > l2.second.size) l1 else l2
}

fun <T> Iterable<T>.findFirstOrNull(predicate: (T) -> Boolean): T? {
    for (it in this) {
        if (predicate(it)) return it
    }
    return null
}
//fun <T, I : Iterable<T>> Collection<T>.maxNumberCollection(predicate: (T) -> Boolean): T {
//    var value = this.first()
//    when (predicate) {
//        max -> {
//            this.forEach {
//                if (value < it)
//                    value = it
//            }
//            return value
//        }
//        min -> {
//            this.forEach {
//                if (value > it)
//                    value = it
//            }
//            return value
//        }
//        average -> {
//            value = 0
//            this.forEach {
//                    if (value > it)
//                        value = it
//                }
//                return value
//            }
//    }
//
//}
//
//}

//public inline fun <T> Iterable<T>.filter(predicate: (T) -> Boolean): List<T> {
//    return filterTo(ArrayList<T>(), predicate)
//}
//
//public inline fun <T, C : MutableCollection<in T>> Iterable<T>.filterTo(destination: C, predicate: (T) -> Boolean): C {
//    for (element in this) if (predicate(element)) destination.add(element)
//    return destination
//}