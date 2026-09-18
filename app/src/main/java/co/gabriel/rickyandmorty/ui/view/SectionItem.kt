package co.gabriel.rickyandmorty.ui.view

import co.gabriel.rickyandmorty.data.model.Character

/**
 * Representa un ítem en la lista: puede ser un encabezado de sección o un personaje.
 */

sealed class SectionItem {

    data class Header(val letter: Char) : SectionItem()
    data class CharacterEntry(val character: Character) : SectionItem()
}
