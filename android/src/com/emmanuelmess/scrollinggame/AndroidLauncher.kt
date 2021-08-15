package com.emmanuelmess.scrollinggame

import com.badlogic.gdx.backends.android.AndroidApplication
import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration

class AndroidLauncher : AndroidApplication() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config = AndroidApplicationConfiguration().also {
            it.useImmersiveMode = true
            it.useAccelerometer = false
            it.useCompass = false
            it.numSamples = 2
        }

        initialize(ScrollingGame(), config)
    }
}