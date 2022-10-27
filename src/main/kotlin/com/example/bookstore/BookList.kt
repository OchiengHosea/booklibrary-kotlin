package com.example.bookstore

import com.example.bookstore.models.Book
import com.example.bookstore.repositories.BookRepository
import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent
import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.component.Text
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route
import org.apache.commons.lang3.StringUtils

@Route(value = "/")
class BookList(
    private val bookRepo: BookRepository,
    private val bookEditor: BookEditor
): VerticalLayout() {
    private val textHeader: Text
    private val addBookBtn: Button
    private val totalBooks: Int = bookRepo.findAll().size
    private val grid: Grid<Book?> = Grid(Book::class.java)

    fun listBooks(filterText: String?) {
        if (StringUtils.isEmpty(filterText)) {
            grid.setItems(bookRepo.findAll())
        } else {
            grid.setItems(
                bookRepo.findByTitleContainingIgnoreCase(filterText)
            )
        }
    }

    init {

        textHeader = Text("$totalBooks books in library")
        addBookBtn = Button("Add Book", VaadinIcon.PLUS.create())

        val header = HorizontalLayout(textHeader, addBookBtn)
        setupBookForm()
        header.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER)
        val booksAndForm = HorizontalLayout(grid, bookEditor)
        grid.height = "300px"
        grid.width = "800px"
        grid.setColumns("title", "publishedOn")
        grid.asSingleSelect()
            .addValueChangeListener {
                e: ComponentValueChangeEvent<Grid<Book?>, Book?> -> bookEditor.editBook(e.value)
            }
        add(header, booksAndForm)

        addBookBtn.addClickListener {
            e: ClickEvent<Button?>? ->
            bookEditor.editBook(Book())
        }

        listBooks(null)
    }

    private fun setupBookForm() {

    }

}