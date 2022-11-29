package com.example.feature_search_book.domain.useCase

data class BookListUseCases(
    val getNewBooksUseCase: GetNewBooksUseCase,
    val searchBookByNameUseCase: SearchBookByNameUseCase
) {
}