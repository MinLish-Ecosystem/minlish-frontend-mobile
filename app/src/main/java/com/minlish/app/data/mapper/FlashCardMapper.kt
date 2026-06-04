package com.minlish.app.data.mapper

import com.minlish.app.R
import com.minlish.app.data.dto.FlashcardContentDto
import com.minlish.app.data.remote.LearningCardDto
import com.minlish.app.presentation.screens.learning.FlashcardData

object FlashCardMapper{
    fun mapToUiList(dtoList: List<FlashcardContentDto>): List<FlashcardData>{
        return dtoList.map{dto->
            FlashcardData(
                category = dto.category,
                word = dto.word,
                phonetic = dto.phonetic,
                partOfSpeech = dto.partOfSpeech,
                definition = dto.definition,
                example = dto.example,
                imageUrl = R.drawable.images,
            )
        }
    }

    fun mapLearningCardDtoListToUiList(dtoList: List<LearningCardDto>): List<FlashcardData>{
        return dtoList.map{dto->
            FlashcardData(
                category = dto.setContext?.setName ?: "General",
                word = dto.word,
                phonetic = dto.pronunciation ?: "",
                partOfSpeech = dto.partOfSpeech ?: "",
                definition = dto.meaning,
                example = dto.examples.firstOrNull() ?: "",
                imageUrl = R.drawable.images,
            )
        }
    }
}