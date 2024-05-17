package com.matttax.drivebetter.voice

expect class MessageSpeller() {
    fun textToSpeech(text: String, language: Language)
}
