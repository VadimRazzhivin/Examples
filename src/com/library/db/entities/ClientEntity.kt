package com.library.db.entities

import java.io.Serializable

data class ClientEntity(
    val id: String,
    val name: String,
    val surname: String,
) : Serializable
