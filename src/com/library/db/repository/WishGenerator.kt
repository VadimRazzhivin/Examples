package com.library.db.repository

import com.library.db.DatabaseDao
import com.library.db.entities.BookEntity
import java.util.*
import kotlin.random.Random

class WishGenerator(private val dao: DatabaseDao) {

    /**
     * key = client.id
     * value = wish books
     */
    val wishBooks: Map<String, MutableList<BookEntity>> by lazy {
        dao.getAllClients().map { it.id }.associateWith {
            Collections.synchronizedList(
                if (Random.nextBoolean()) {
                    mutableListOf(dao.getAllBooks().random())
                } else {
                    mutableListOf()
                }
            )
        }
    }
}
