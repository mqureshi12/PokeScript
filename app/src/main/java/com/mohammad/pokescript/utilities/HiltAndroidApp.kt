package com.mohammad.pokescript.utilities

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// Class for dependency injection
// First class to run in the app

@HiltAndroidApp
class HiltAndroidApp : Application() {
}