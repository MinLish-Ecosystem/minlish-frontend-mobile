package com.minlish.app.data.mapper

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Work
import com.minlish.app.data.dto.response.VocabSetDto
import com.minlish.app.presentation.screens.learning.VocabSet
import kotlin.random.Random

object VocabSetMapper{
    fun mapToUiList(dtoList: List<VocabSetDto>): List<VocabSet>{
        return dtoList.map{dto->
            VocabSet(
                id=dto.id,
                title = dto.title,
                wordCount = dto.wordCount,
                iconName = Icons.Default.Work,
                colorHex=randomColorHex(),
                isDueToday = dto.isDueToday,
            )
        }
    }
    fun randomColorHex(): Long {
        while (true) {
            val r = Random.nextInt(256)
            val g = Random.nextInt(256)
            val b = Random.nextInt(256)
            val brightness =
                (r * 0.299) +
                        (g * 0.587) +
                        (b * 0.114)

            if (brightness < 200) {
                return (
                        0xFF000000 or
                                ((r shl 16).toLong()) or
                                ((g shl 8).toLong()) or
                                b.toLong()
                        )
            }
        }
    }
}