package com.location.location

import android.app.Application
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.work.Configuration.Provider
import androidx.work.WorkManager
import com.birbit.android.jobqueue.JobManager
import com.birbit.android.jobqueue.config.Configuration
import com.birbit.android.jobqueue.log.CustomLogger
import com.google.android.libraries.places.api.Places
import com.location.location.constants.Const
import com.location.location.utils.LogHelper
import com.location.location.utils.PreferenceManager
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@HiltAndroidApp
class App : Application(), LifecycleObserver,Provider  {

    public val scope = CoroutineScope(GlobalScope.coroutineContext)

    lateinit var mPreferenceManager: PreferenceManager

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    private val workManager: WorkManager by lazy { WorkManager.getInstance(this) }

    override fun onCreate() {
        super.onCreate()
        instance = this
        appContext = this
        mPreferenceManager= PreferenceManager(this)

        Places.initialize(this, Const.PLACES_API_KEY)
        FirebaseApp.initializeApp(this)
        connectSocket()
        configureJobManager()
    }

    override val workManagerConfiguration: androidx.work.Configuration
        get() = androidx.work.Configuration.Builder().setWorkerFactory(workerFactory).build()

    private fun connectSocket(){
        val lifecycleEventObserver = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_STOP -> {
                    //your code here
                    LogHelper.e(TAG, "ON_STOP")
                    //disconnect()
                }

                Lifecycle.Event.ON_START -> {
                    //your code here
                    LogHelper.e(TAG, "ON_START")
                }
                Lifecycle.Event.ON_DESTROY -> {
                    //your code here
                    LogHelper.e(TAG, "ON_DESTROY")
                }
                Lifecycle.Event.ON_CREATE -> {
                    //your code here
                    LogHelper.e(TAG, "ON_CREATE")
                    //syncData()

                    removeTempCaseFileImage()
                }
                else -> {

                }
            }
        }
        ProcessLifecycleOwner.get().lifecycle.addObserver(lifecycleEventObserver)
    }

    private fun configureJobManager() {
        val configuration = Configuration.Builder(this)
            .customLogger(object : CustomLogger {
                private val TAG = "JOBS"

                override fun isDebugEnabled(): Boolean {
                    return true
                }

                override fun d(text: String, vararg args: Any) {
                    LogHelper.d(TAG, String.format(text, *args))
                }

                override fun e(t: Throwable, text: String, vararg args: Any) {
                    LogHelper.e(TAG, String.format(text, *args))
                }

                override fun e(text: String, vararg args: Any) {
                    LogHelper.e(TAG, String.format(text, *args))
                }

                override fun v(text: String, vararg args: Any) {
                    LogHelper.v(TAG, String.format(text, *args))
                }
            })
            .minConsumerCount(1)//always keep at least one consumer alive
            .maxConsumerCount(3)//up to 3 consumers at a time
            .loadFactor(3)//3 jobs per consumer
            .consumerKeepAlive(120)//wait 2 minute
            .build()

        jobManager = JobManager(configuration)
    }

    private fun removeTempCaseFileImage() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val mediaStorageDir = File(
                    externalCacheDir,
                    File.separator + Const.DIR_TEMP_IMAGE_FILE
                )
                if (mediaStorageDir.exists()) {
                    deleteRecursively(mediaStorageDir)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    LogHelper.printStackTrace(e)  // Log exception on main thread
                }
            }
        }
    }

    private fun deleteRecursively(fileOrDirectory: File) {
        if (fileOrDirectory.isDirectory) {
            fileOrDirectory.listFiles()?.forEach { child ->
                deleteRecursively(child)
            }
        }
        fileOrDirectory.delete() // Delete the file or empty directory
    }

    companion object {
        var jobManager: JobManager? = null
        val TAG: String = App::class.java.simpleName
        var appContext: Context? = null
        @get:Synchronized
        var instance: App? = null
        var mCurrentConvId=""
        var isFromRequestActivity = false
    }
}