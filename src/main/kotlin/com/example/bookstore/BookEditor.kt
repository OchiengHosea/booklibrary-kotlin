package com.example.bookstore

import com.example.bookstore.models.Book
import com.example.bookstore.repositories.BookRepository
import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.component.Key
import com.vaadin.flow.component.KeyNotifier
import com.vaadin.flow.component.KeyPressEvent
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.spring.annotation.SpringComponent
import com.vaadin.flow.spring.annotation.UIScope
import org.springframework.beans.factory.annotation.Autowired

@SpringComponent
@UIScope
class BookEditor @Autowired constructor
    (private val bookRepository: BookRepository): VerticalLayout(), KeyNotifier {
    var title = TextField("Title")
    var publishedOn = DatePicker("Select Date")
    var synopsis = TextArea("Enter Synopsis")

    /* Action Buttons */
    private final var save = Button("Save", VaadinIcon.CHECK.create())
    private final var cancel = Button("Cancel")
    private final var delete = Button("Delete", VaadinIcon.TRASH.create())

    var actions = HorizontalLayout(save, cancel, delete)
    var binder = Binder(
        Book::class.java
    )
    private var book: Book? = null
    private var changeHandler: ChangeHandler? = null

    fun setChangeHandler(h: ChangeHandler?) {
        changeHandler = h
    }

    interface ChangeHandler {
        fun onChange()
    }

    init {
        add(
            title,
            publishedOn,
            synopsis,
            actions
        )

        binder.bindInstanceFields(this)
        isSpacing = true

        save.element.themeList.add("primary")
        delete.element.themeList.add("error")

        addKeyPressListener(Key.ENTER, {
            e: KeyPressEvent? -> save()
        })

        save.addClickListener {
            e:ClickEvent<Button?>? -> save()
        }

        delete.addClickListener {
            e: ClickEvent<Button?>? -> delete()
        }

        cancel.addClickListener {
            e: ClickEvent<Button?>? -> editBook(book!!)
        }
    }

    fun save() {
        bookRepository.save<Book>(book!!)
        changeHandler!!.onChange()
    }

    fun delete() {
        bookRepository.delete(book!!)
        changeHandler!!.onChange()
    }

    fun editBook(book: Book?) {
        println("Updating...")
        if (book == null){
            isVisible = false
            return
        }

        val persisted = book.id != null
        this.book = if (persisted) {
            bookRepository.findById(book.id!!).get()
        } else {
            book
        }

        cancel.isVisible = persisted
        binder.bean = book
        isVisible = true
        title.focus()
    }
}