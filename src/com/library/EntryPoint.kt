package com.library

import com.library.core.Library
import com.library.db.DatabaseDao
import com.library.db.DatabaseManager
import com.library.db.repository.Repository
import com.library.humans.Librarian
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
    val databaseManager = DatabaseManager()
    val database = databaseManager.getDatabase()
    val dao = DatabaseDao(database)
    val repository = Repository(dao)

    println("Start ${database.ownership.size}")

    val library = Library(repository = repository)
    val librarians = listOf(
        Librarian(id = "LM-001", name = "Мотя", surname = "Букварёва", repository = repository),
        Librarian(id = "LM-002", name = "Марфа", surname = "Иванова", repository = repository)
    )

    library.start(librarians = librarians)

    val clients = repository.clients()

    val clientsThread = thread(name = "Client thread") {
        clients.forEach { client ->
            println("\t`${client.name} ${client.surname}` зашел в библиотеку.")
            library.clientVisit(client)
            Thread.sleep(Random.nextLong(10, 16))
        }
    }
    clientsThread.join()
    library.finish(clients)

    databaseManager.updateDatabase(database)

    println("Finish ${database.ownership.size}")

    println("FINISH")
}

