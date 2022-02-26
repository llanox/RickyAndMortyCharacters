package co.gabriel.rickyandmorty.core

import android.content.Context
import co.gabriel.rickyandmorty.domain.CharacterRepository

fun Context.provideCharacterRepository(): CharacterRepository {
    val poCApplication = applicationContext as RickyAndMortyApplication
    return poCApplication.characterRepository
}