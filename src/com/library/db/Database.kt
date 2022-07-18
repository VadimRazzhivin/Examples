package com.library.db

import com.library.db.entities.BookEntity
import com.library.db.entities.ClientEntity
import com.library.db.entities.OwnershipEntity
import java.io.Serializable

data class Database(
    val books: MutableSet<BookEntity>,
    val clients: MutableSet<ClientEntity>,
    val ownership: MutableSet<OwnershipEntity>,
) : Serializable

