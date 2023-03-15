package co.gabriel.rickyandmorty.core

import android.content.Context
import co.gabriel.rickyandmorty.domain.CharacterRepository
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

fun Context.provideCharacterRepository(): CharacterRepository {
    val poCApplication = applicationContext as RickyAndMortyApplication
    return poCApplication.characterRepository
}

fun doubleToCurrency(value: Double): String {
    val formatter = DecimalFormat("#,###.##", DecimalFormatSymbols(Locale("es", "ES")))
    return "$" + formatter.format(value)
}