package com.example.mathgame.domain.usecases

import com.example.mathgame.domain.entity.GameSettings
import com.example.mathgame.domain.entity.Level
import com.example.mathgame.domain.repository.GameRepository

class GetGameSettingsUseCase(
    private val repo: GameRepository
) {

    operator fun invoke(level: Level): GameSettings {
        return repo.getGameSettings(level)
    }
}