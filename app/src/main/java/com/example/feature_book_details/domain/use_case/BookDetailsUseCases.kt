package com.example.feature_book_details.domain.use_case

data class BookDetailsUseCases(
    val saveBook: SaveBookUseCase,
    val getBookUseCase: GetBookUseCase,
    val deleteBookUseCase: DeleteBookUseCase
) {
}