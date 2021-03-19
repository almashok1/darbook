package kz.adamant.domain.usecases

import kz.adamant.domain.repository.RentRepository

class GetBookRentingMarkUseCase(
    private val rentRepository: RentRepository
) {
    suspend operator fun invoke(bookId: Int, userId: String): Int {
        return rentRepository.getBookRentingMark(bookId, userId)
    }
}