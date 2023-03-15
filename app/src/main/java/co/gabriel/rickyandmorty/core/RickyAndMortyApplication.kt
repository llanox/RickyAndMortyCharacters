package co.gabriel.rickyandmorty.core

import android.app.Application
import co.gabriel.rickyandmorty.di.RetrofitBuilder
import co.gabriel.rickyandmorty.data.repositories.RemoteCharacterRepository
import co.gabriel.rickyandmorty.domain.CharacterRepository
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RickyAndMortyApplication: Application() {
    val characterRepository : CharacterRepository by lazy {
        RemoteCharacterRepository(RetrofitBuilder.characterAPIService)
    }
}

