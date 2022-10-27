package com.example.bookstore.repositories

import com.example.bookstore.models.Book
import org.springframework.data.jpa.repository.JpaRepository

interface BookRepository : JpaRepository<Book, Long> {
    fun findByTitleContainingIgnoreCase(title: String?) : List<Book?>?
}