package spacemadness.com.lunarconsole.app

import android.app.Application
import spacemadness.com.lunarconsole.concurrent.createDefaultExecutorQueueFactory
import spacemadness.com.lunarconsole.di.DependencyProvider
import spacemadness.com.lunarconsole.log.createDefaultLogger

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        DependencyProvider.apply {
            register(createDefaultLogger())
            register(createDefaultExecutorQueueFactory())
        }
    }
}