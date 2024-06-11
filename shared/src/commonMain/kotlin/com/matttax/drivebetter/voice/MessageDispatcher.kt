package com.matttax.drivebetter.voice

import com.matttax.drivebetter.voice.model.SpeedViolation

class MessageDispatcher(
    private val messageSpeller: MessageSpeller
) {

    fun onSpeedExceeded(speedViolation: SpeedViolation) {
        messageSpeller.textToSpeech(Russian.getSlowDownMessage(), DEFAULT_LANGUAGE)
    }

    fun onTooSlowDriving(speedViolation: SpeedViolation) {
        messageSpeller.textToSpeech(Russian.getSpeedUpText(), DEFAULT_LANGUAGE)
    }

    fun onWarning(text: String) {
        messageSpeller.textToSpeech(text, DEFAULT_LANGUAGE)
    }

    fun onRideStarted(km: Double) {
        messageSpeller.textToSpeech(Russian.getRideStartedText(km), DEFAULT_LANGUAGE)
    }

    fun onRideStarted() {
        messageSpeller.textToSpeech(Russian.getRideStartedTextShort(), DEFAULT_LANGUAGE)
    }

    fun onRideEnded() {
        messageSpeller.textToSpeech(Russian.getRideEndedText(), DEFAULT_LANGUAGE)
    }

    private object Russian {
        fun getRideStartedText(km: Double) = "Поездка началась. ${km.toInt()} километров осталось."
        fun getRideStartedTextShort() = "Поездка начата. Приятной дороги!."
        fun getRideEndedText() = "Поездка завершена."
        fun getSpeedUpText() = "Вы едете слишком медленно. Ускорьтесь!"
        fun getSlowDownMessage() = "Вы едете слишком быстро. Снизьте скорость!"
    }

    companion object {
        private val DEFAULT_LANGUAGE = Language.RU
    }

}
