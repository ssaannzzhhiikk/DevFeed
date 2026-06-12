package com.sanzh.devfeed

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.sanzh.devfeed.data.local.datastore.PreferencesManager
import com.sanzh.devfeed.data.local.db.AppDatabase
import com.sanzh.devfeed.worker.SyncWorker
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class DevFeedApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    lateinit var preferencesManager: PreferencesManager
        private set
    lateinit var database: AppDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        preferencesManager = PreferencesManager(this)
        database = AppDatabase.getInstance(this)
        // Start WorkManager sync
        SyncWorker.schedule(this)
    }
}
