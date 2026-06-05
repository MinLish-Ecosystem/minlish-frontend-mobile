package com.minlish.app.data.mapper

import com.minlish.app.data.dto.response.ModeDto
import com.minlish.app.presentation.screens.practice.ALL_PRACTICE_MODES
import com.minlish.app.presentation.screens.practice.PracticeMode

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