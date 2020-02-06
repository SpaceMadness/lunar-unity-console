package spacemadness.com.lunarconsole.app

import android.app.Application
import spacemadness.com.lunarconsole.concurrent.createExecutorQueueFactory
import spacemadness.com.lunarconsole.di.DependencyProvider

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        DependencyProvider.register(createExecutorQueueFactory())
    }
}