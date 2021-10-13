package uz.gita.mytodoapp.app

import android.app.Application

class App : Application() {

    companion object {
        lateinit var instance : App
        private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}