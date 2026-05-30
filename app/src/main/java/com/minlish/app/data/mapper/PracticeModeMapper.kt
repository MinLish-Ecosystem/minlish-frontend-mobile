package com.minlish.app.data.mapper

import com.minlish.app.R
import com.minlish.app.data.dto.FlashcardContentDto
import com.minlish.app.data.dto.ModeDto
import com.minlish.app.data.dto.PracticeModeDto
import com.minlish.app.presentation.screens.learning.FlashcardData
import com.minlish.app.presentation.screens.practice.ALL_PRACTICE_MODES
import com.minlish.app.presentation.screens.practice.PracticeMode
import com.minlish.app.ui.components.NavItem

object PracticeModeMapper {
    fun mapToUiList(backendModes: List<ModeDto>): List<PracticeMode>{
        val backendStatusMap = backendModes.associateBy { it.id }
        return ALL_PRACTICE_MODES
            .filter { hardcodedMode ->
                backendStatusMap.containsKey(hardcodedMode.id)
            }
            .map { hardcodedMode ->
                val backendData = backendStatusMap[hardcodedMode.id]!!
                hardcodedMode.copy(
                    isComingSoon = backendData.isComingSoon
                )
            }.sortedBy { it.isComingSoon }
    }
}