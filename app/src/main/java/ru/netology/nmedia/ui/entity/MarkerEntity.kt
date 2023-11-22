package ru.netology.nmedia.ui.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.ui.dto.MyMarker

@Entity
data class MarkerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String = "",
    val position: String = "",
    val tag: String = "",
//    val googleId: String
) {
    fun toDto() =
        MyMarker(
            id = id,
            title = title,
            position = position,
            tag = tag,
//            googleId = googleId
        )

    companion object {
        fun fromDto(dto: MyMarker) =
            MarkerEntity(
                id = dto.id,
                title = dto.title,
                position = dto.position,
                tag = dto.tag,
//                googleId = dto.googleId
            )
    }
}
fun List<MarkerEntity>.toDto(): List<MyMarker> = map(MarkerEntity::toDto)
fun List<MyMarker>.toEntity(): List<MarkerEntity> = map(MarkerEntity::fromDto)