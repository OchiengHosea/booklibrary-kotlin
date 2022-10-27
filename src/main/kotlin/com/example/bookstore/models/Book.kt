package com.example.bookstore.models

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.EqualsAndHashCode
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter
import java.time.LocalDate
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter @Setter
@Builder
class Book {
    @Id
    @GeneratedValue

    var id: Long? = null
    var title: String? = null
    var publishedOn: LocalDate? = LocalDate.now()
    var synopsis: String? = ""
}