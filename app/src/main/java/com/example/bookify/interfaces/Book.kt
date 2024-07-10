package com.example.bookify.interfaces

data class Book(
    val title: String,
    val author: String,
    val description: String = "",
    val imageUrl: String
)
