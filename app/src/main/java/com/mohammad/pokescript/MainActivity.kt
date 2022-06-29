package com.mohammad.pokescript

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.work.BackoffPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.mohammad.pokescript.utilities.BackgroundWorker
import dagger.hilt.android.AndroidEntryPoint
import androidx.work.*
import java.util.concurrent.TimeUnit

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_PokeScript)
        setContentView(R.layout.activity_main)

        if (!isNetworkConnected()) {
            Toast.makeText(this, "No internet access is detected!", Toast.LENGTH_LONG).show()
        }

        workerCheck()

        Log.d(TAG, "onCreate called")
    }

    // Setup background worker to periodically check for new pokemon and add it to my db
    @RequiresApi(Build.VERSION_CODES.M)
    private fun workerCheck() {
        val preferences = getSharedPreferences("worker", MODE_PRIVATE)

        // If worker value is not true then set the value and create the worker
        if (preferences.getString("worker", "") != "cancel") {
            val editPref = preferences.edit()
            editPref.putString("worker", "enabled")
            editPref.apply()
            Log.d("BackgroundWorker", "SharedPreferences Boolean Set")

            // Create worker constraints
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build()

            // Run every 15 minutes
            val backgroundWorker = PeriodicWorkRequest.Builder(BackgroundWorker::class.java, 15, TimeUnit.MINUTES)
                .addTag("new_pokemon_checker")
                .setConstraints(constraints)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30, TimeUnit.SECONDS) // Back off in the event of a worker job failing
                .build()

            WorkManager.getInstance(this.applicationContext).enqueue(backgroundWorker)
        } else {
            cancelWorkers()
        }
    }

    private fun cancelWorkers() {
        WorkManager.getInstance(this).cancelAllWorkByTag("new_pokemon_checker")
    }

    // Check internet
    private fun isNetworkConnected() : Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if(capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return true
            }
        }
        return false
    }
}