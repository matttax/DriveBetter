package com.matttax.drivebetter.voice

import com.matttax.drivebetter.voice.model.SpeedViolation

class MessageDispatcher(
    private val messageSpeller: MessageSpeller
) {

    fun onSpeedExceeded(speedViolation: SpeedViolation) {
        messageSpeller.textToSpeech(English.getSlowDownMessage(), DEFAULT_LANGUAGE)
    }

    fun onTooSlowDriving(speedViolation: SpeedViolation) {
        messageSpeller.textToSpeech(English.getSpeedUpText(), DEFAULT_LANGUAGE)
    }

    fun onWarning(text: String) {
        messageSpeller.textToSpeech(text, DEFAULT_LANGUAGE)
    }

    fun onRideStarted(km: Double) {
        messageSpeller.textToSpeech(English.getRideStartedText(km), DEFAULT_LANGUAGE)
    }

    fun onRideStarted() {
        messageSpeller.textToSpeech(English.getRideStartedTextShort(), DEFAULT_LANGUAGE)
    }

    fun onRideEnded() {
        messageSpeller.textToSpeech(English.getRideEndedText(), DEFAULT_LANGUAGE)
    }

    private object English {
        fun getRideStartedText(km: Double) = "Ride started. ${km.toInt()} kilometers left."
        fun getRideStartedTextShort() = "Ride started. Be careful!"
        fun getRideEndedText() = "Ride ended."
        fun getSpeedUpText() = "You are driving too slow. Please speed up."
        fun getSlowDownMessage() = "You are driving too fast. Please slow down."
    }

    companion object {
        private val DEFAULT_LANGUAGE = Language.EN
    }

}
