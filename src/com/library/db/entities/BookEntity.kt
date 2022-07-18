package com.library.db.entities

import java.io.Serializable

data class BookEntity(
    val id: String,
    val title: String,
    val year: String,
) : Serializable
