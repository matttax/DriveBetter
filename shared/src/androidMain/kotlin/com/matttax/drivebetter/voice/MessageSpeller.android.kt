package com.matttax.drivebetter.voice

import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import com.matttax.drivebetter.DriveBetterApp
import java.util.Locale

actual class MessageSpeller {

    @Volatile
    private var isSpeaking: Boolean = false

    private lateinit var termSpeech: TextToSpeech

    actual fun textToSpeech(text: String, language: Language) {
        if (isSpeaking) return
        isSpeaking = true
        termSpeech = TextToSpeech(DriveBetterApp.context) {
            if (it == TextToSpeech.SUCCESS) {
                termSpeech.language = if (language == Language.RU) Locale("ru") else Locale.US
                termSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null,"")
            }
        }
        termSpeech.setOnUtteranceProgressListener(
            object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String) = Unit
                override fun onError(utteranceId: String) = Unit
                override fun onDone(utteranceId: String) {
                    isSpeaking = false
                }
            }
        )
    }
}
