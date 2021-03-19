package kz.adamant.domain.usecases

import kz.adamant.domain.models.ReadingBook
import kz.adamant.domain.models.RentInfo
import kz.adamant.domain.repository.RentRepository

class RentBookUseCase(
    private val rentRepository: RentRepository
) {
    suspend operator fun invoke(rentInfo: RentInfo): ReadingBook? {
        return rentRepository.rentBook(rentInfo)
    }
}