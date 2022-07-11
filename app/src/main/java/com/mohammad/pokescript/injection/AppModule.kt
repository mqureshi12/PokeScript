package com.mohammad.pokescript.injection

import android.content.Context
import com.mohammad.pokescript.api.ApiInterface
import com.mohammad.pokescript.database.PokemonDB
import com.mohammad.pokescript.database.PokemonDao
import com.mohammad.pokescript.repository.MainRepository
import com.mohammad.pokescript.repository.MainRepositoryImplement
import com.mohammad.pokescript.utilities.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

// Holds all dependencies
// Singleton - only one instance for dependencies throughout the app. Won't keep
// recreating the db and api instance when accessing them

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providePokeApi() : ApiInterface = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        // Convert json data from the api responses into my data classes
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiInterface::class.java)

    @Provides
    fun providePokemonDAO(@ApplicationContext applicationContext: Context) : PokemonDao {
        return PokemonDB.getDB(applicationContext).pokemonDAO()
    }

    // Main repository
    @Singleton
    @Provides
    fun providesMainRepository(api: ApiInterface, dao: PokemonDao) : MainRepository = MainRepositoryImplement(api, dao)
}