package com.library.tests

import com.library.db.DatabaseManager
import com.library.db.entities.BookEntity
import com.library.db.entities.ClientEntity
import com.library.db.entities.OwnershipEntity


object DatabaseManagerTest {

    @JvmStatic
    fun main(args: Array<String>) {
        test()
    }

    private fun test() {
        val path = "src/com/library/test.db"
        val databaseManager = DatabaseManager(path)
        val testDb = databaseManager.getDatabase()
        testDb.ownership.clear()
        testDb.ownership.add(
            OwnershipEntity(
                ClientEntity(
                    id = "testId",
                    name = "testName",
                    surname = "testSurname",
                ),
                mutableSetOf(
                    BookEntity(
                        id = "testId",
                        title = "testTitle",
                        year = "testYear",
                    ),
                ),
            ),
        )
        databaseManager.updateDatabase(testDb)
        val newDb = databaseManager.getDatabase()
        require(newDb.ownership == testDb.ownership) {
            "Different sizes"
        }
    }
}
