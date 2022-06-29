package com.mohammad.pokescript.utilities

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mohammad.pokescript.models.CustomPokemonListItem
import com.mohammad.pokescript.repository.MainRepositoryImplement
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TAG = "BackgroundWorker"

@HiltWorker
class BackgroundWorker @AssistedInject constructor(

    @Assisted var context: Context,
    @Assisted params: WorkerParameters,
    private var repository: MainRepositoryImplement
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {

        Log.d(TAG, "Worker has run")
        return withContext(Dispatchers.IO) {

            // Get id of last pokemon in local db
            val lastStoredPokemonObject = repository.getLastStored()

            // Implement hard stop at 100 pokemon
            if (lastStoredPokemonObject.id!! >= 100) {
                val pref = context.getSharedPreferences("worker", AppCompatActivity.MODE_PRIVATE)
                val editor = pref.edit()
                editor.putString("worker", "cancel")
                editor.commit()
                return@withContext Result.success()
            }

            // Check API for details on next pokemon
            val nextPokemon = lastStoredPokemonObject.apiId + 1
            Log.d(TAG, "searched pokemon: $nextPokemon")
            Log.d(TAG, "lastPokemon: $nextPokemon")
            val apiResult = repository.getPokemonDetails(nextPokemon)

            when (apiResult) {
                is Resource.Success -> {
                    if (apiResult.data != null) {
                        apiResult.data.let { newPokemon ->
                            // Create custom pokemon object save in db
                            val newPokemonObj = CustomPokemonListItem(
                                name = newPokemon.name,
                                Image = newPokemon.sprites.front_default,
                                type = newPokemon.types?.get(0)?.type?.name.toString(),
                                positionLeft = (0..1500).random(),
                                positionTop = (0..1500).random(),
                                apiId = newPokemon.id
                            )
                            repository.savePokemon(newPokemonObj)

                            Log.d("worker", "new pokemon found ${newPokemonObj.name}")
                            return@withContext Result.success()
                        }
                    } else {
                        return@withContext Result.failure()
                    }
                }
                is Resource.Error -> {
                    return@withContext Result.failure()
                }
                else -> return@withContext Result.failure()
            }
        }
    }
}