package com.example.mathgame.presentation.viewModels

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mathgame.R
import com.example.mathgame.data.GameRepositoryImpl
import com.example.mathgame.domain.entity.GameResult
import com.example.mathgame.domain.entity.GameSettings
import com.example.mathgame.domain.entity.Level
import com.example.mathgame.domain.entity.Question
import com.example.mathgame.domain.usecases.GenerateQuestionUseCase
import com.example.mathgame.domain.usecases.GetGameSettingsUseCase

class GameViewModel(
    private val context: Application,
    private val level: Level
): ViewModel() {

    private val repo = GameRepositoryImpl

    private val generateQuestionUseCase = GenerateQuestionUseCase(repo)
    private val getGameSettingsUseCase = GetGameSettingsUseCase(repo)

    private var timer: CountDownTimer? = null

    private lateinit var gameSettings: GameSettings

    //Ответы пользователя
    private val userAnswers = mutableListOf<Int>()
    //Количество правильных ответов
    private var cntCorrectAnswers = 0
    //Количество сыгранных уровней
    private var countFinishedLevels = 0

    //Таймер игры
    private val _timerLD = MutableLiveData<String>()
    val timerLD: LiveData<String>
        get() = _timerLD

    //Вопрос
    private val _questionValue = MutableLiveData<Question>()
    val questionValue:LiveData<Question>
        get() = _questionValue

    private val _isGameOver = MutableLiveData<Unit>()
    val isGameOver:LiveData<Unit>
        get() = _isGameOver

    //Количество правильных ответов строка
    private val _countCorrectAnswers = MutableLiveData<String>()
    val countCorrectAnswers: LiveData<String>
        get() = _countCorrectAnswers

    //Процент правильных ответов
    private val _percentCorrectAnswers = MutableLiveData<Int>(0)
    val percentCorrectAnswers: LiveData<Int>
        get() = _percentCorrectAnswers

    //LD для выделения цветом текса кол-ва ответов
    private val _isGoodCount = MutableLiveData<Boolean>()
    val isGoodCount: LiveData<Boolean>
        get() = _isGoodCount

    //LD для выделения цветом прогресс бара
    private val _isGoodPercent = MutableLiveData<Boolean>()
    val isGoodPercent: LiveData<Boolean>
        get() = _isGoodPercent

    //LD для отображения secondary прогресса
    // (на прогресс баре серым цветом отметка для минимального процента ответов)
    private val _secondaryProgress = MutableLiveData<Int>()
    val secondaryProgression: LiveData<Int>
        get() = _secondaryProgress

    private val _gameResultLD = MutableLiveData<GameResult>()
    val gameResultLD: LiveData<GameResult>
        get() = _gameResultLD

    init {
        startGame()
    }

    private fun startGame() {
        this.gameSettings = getGameSettingsUseCase(level)
        startTimer()
        updateProgress()
        generateLevel()
        _secondaryProgress.value = gameSettings.minPercentOfRightAnswers
    }

    //Метод генерации уровня (вопроса)
    private fun generateLevel() {
        _questionValue.value = generateQuestionUseCase(gameSettings.maxSumValue)
    }

    //Метод запуска таймера
    private fun startTimer() {
        timer = object : CountDownTimer(
            gameSettings.gameTimeInSeconds * MILLIS_IN_SECONDS,
            MILLIS_IN_SECONDS
        ) {
            override fun onFinish() {
                finishGame()
            }

            override fun onTick(millisUntilFinished: Long) {
                _timerLD.postValue(formatTime(millisUntilFinished))
            }
        }
        timer?.start()
    }

    //Вспомогательный метод форматирования времени для отображения таймера
    private fun formatTime(millisUntilFinished: Long): String {
        val seconds = millisUntilFinished / MILLIS_IN_SECONDS
        val minutes = seconds / SECONDS_IN_MINUTES
        val leftSeconds = seconds % SECONDS_IN_MINUTES
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun finishGame() {
        setGameResult()
        _isGameOver.postValue(Unit)
    }

    //Метод формирования результата игры
    private fun setGameResult() {
        _gameResultLD.value = GameResult(
            isVictory = isGoodCount.value == true && isGoodPercent.value == true,
            countRightAnswers = cntCorrectAnswers,
            countOfQuestions = countFinishedLevels,
            gameSettings = gameSettings
        )
    }

    //Метод получения ответа пользователя на задачу
    fun takeAnswer(answer: Int) {
        userAnswers.add(answer)
        countFinishedLevels++
        if (answer == questionValue.value?.correctAnswer){
            cntCorrectAnswers++
        }
        updateProgress()
//        if (gameSettings.minSumOfRightAnswers == cntCorrectAnswers){
//            finishGame()
//        }
        generateLevel()
    }

    private fun updateProgress() {
        val percent = calculatePercentOfProgress()
        _countCorrectAnswers.value = String.format(
            context.resources.getString(R.string.tv_progress_answers),
            cntCorrectAnswers.toString(),
            gameSettings.minSumOfRightAnswers.toString()
        )
        _percentCorrectAnswers.value = percent
        _isGoodCount.value = cntCorrectAnswers >= gameSettings.minSumOfRightAnswers
        _isGoodPercent.value = percent >= gameSettings.minPercentOfRightAnswers
    }

    private fun calculatePercentOfProgress(): Int {
        if (countFinishedLevels == 0) return 0
        return ((cntCorrectAnswers.toDouble() / countFinishedLevels) * 100).toInt()
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }

    companion object {
        const val MILLIS_IN_SECONDS = 1000L
        const val SECONDS_IN_MINUTES = 60
    }
}