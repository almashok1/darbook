package kz.adamant.data.mappers

import kz.adamant.data.remote.models.RentInfoDto
import kz.adamant.domain.models.RentInfo

internal fun RentInfo.toDto(): RentInfoDto {
    return RentInfoDto(
        userId = userId,
        userName = userName,
        userContact = userContact,
        bookId = bookId,
        startDate = startDate
    )
}