package com.example.assignment6_parliament_room.ui.strings

import androidx.compose.runtime.compositionLocalOf

/**
 * Supported languages in the app.
 * Stored as a string in DataStore ("en", "fi", "sv").
 */
enum class AppLanguage(val code: String, val displayName: String) {
    ENGLISH("en", "English"),
    FINNISH("fi", "Suomi"),
    SWEDISH("sv", "Svenska")
}

fun appLanguageFromCode(code: String): AppLanguage =
    AppLanguage.entries.find { it.code == code } ?: AppLanguage.ENGLISH

/**
 * All UI strings in one place.
 * Add a new data class instance below for each language.
 *
 * Usage in any Composable:
 *   val strings = LocalStrings.current
 *   Text(strings.parliamentMembers)
 */
data class AppStrings(
    // TopAppBar
    val parliamentMembers: String,
    val myRatings: String,
    val settings: String,
    val toggleTheme: String,
    val refresh: String,

    // Filter chips
    val byConstituency: String,
    val byParty: String,
    val only: String,              // favorites filter button

    // List screen
    val members: String,           // "$count members"
    val yearsOld: String,          // "52 years old"
    val born: String,              // "born 1972"
    val addToFavorites: String,
    val removeFromFavorites: String,

    // Detail screen
    val ratings: String,           // "Ratings (3)"
    val noRatingsYet: String,
    val savedAsFavorite: String,
    val addRating: String,
    val back: String,

    // My Ratings screen
    val noRatingsMessage: String,

    // Settings screen
    val language: String,
    val darkTheme: String,

    // Rating dialog
    val positive: String,
    val negative: String,
    val comment: String,
    val submit: String,
    val cancel: String,

    // Bottom bar
    val parliamentMembersShort: String,
)

val EnglishStrings = AppStrings(
    parliamentMembers    = "Parliament Members",
    myRatings            = "My Ratings",
    settings             = "Settings",
    toggleTheme          = "Toggle theme",
    refresh              = "Refresh",
    byConstituency       = "By Constituency",
    byParty              = "By Party",
    only                 = "Only",
    members              = "members",
    yearsOld             = "years old",
    born                 = "born",
    addToFavorites       = "Add to favorites",
    removeFromFavorites  = "Remove from favorites",
    ratings              = "Ratings",
    noRatingsYet         = "No ratings yet. Be the first!",
    savedAsFavorite      = "Saved as favorite",
    addRating            = "Add Rating",
    back                 = "Back",
    noRatingsMessage     = "No ratings yet. Go rate some MPs!",
    language             = "Language",
    darkTheme            = "Dark Theme",
    positive             = "Positive",
    negative             = "Negative",
    comment              = "Comment",
    submit               = "Submit",
    cancel               = "Cancel",
    parliamentMembersShort = "PMs"
)

val FinnishStrings = AppStrings(
    parliamentMembers    = "Kansanedustajat",
    myRatings            = "Omat arviot",
    settings             = "Asetukset",
    toggleTheme          = "Vaihda teema",
    refresh              = "Päivitä",
    byConstituency       = "Vaalipiireittäin",
    byParty              = "Puolueittain",
    only                 = "Vain",
    members              = "jäsentä",
    yearsOld             = "vuotta",
    born                 = "syntynyt",
    addToFavorites       = "Lisää suosikkeihin",
    removeFromFavorites  = "Poista suosikeista",
    ratings              = "Arviot",
    noRatingsYet         = "Ei vielä arvioita. Ole ensimmäinen!",
    savedAsFavorite      = "Tallennettu suosikiksi",
    addRating            = "Lisää arvio",
    back                 = "Takaisin",
    noRatingsMessage     = "Ei vielä arvioita. Mene arvioimaan kansanedustajia!",
    language             = "Kieli",
    darkTheme            = "Tumma teema",
    positive             = "Positiivinen",
    negative             = "Negatiivinen",
    comment              = "Kommentti",
    submit               = "Lähetä",
    cancel               = "Peruuta",
    parliamentMembersShort = "Edustajat"
)

val SwedishStrings = AppStrings(
    parliamentMembers    = "Riksdagsledamöter",
    myRatings            = "Mina betyg",
    settings             = "Inställningar",
    toggleTheme          = "Byt tema",
    refresh              = "Uppdatera",
    byConstituency       = "Per valkrets",
    byParty              = "Per parti",
    only                 = "Bara",
    members              = "ledamöter",
    yearsOld             = "år",
    born                 = "född",
    addToFavorites       = "Lägg till favoriter",
    removeFromFavorites  = "Ta bort från favoriter",
    ratings              = "Betyg",
    noRatingsYet         = "Inga betyg ännu. Var den första!",
    savedAsFavorite      = "Sparad som favorit",
    addRating            = "Lägg till betyg",
    back                 = "Tillbaka",
    noRatingsMessage     = "Inga betyg ännu. Gå och betygsätt några ledamöter!",
    language             = "Språk",
    darkTheme            = "Mörkt tema",
    positive             = "Positiv",
    negative             = "Negativ",
    comment              = "Kommentar",
    submit               = "Skicka",
    cancel               = "Avbryt",
    parliamentMembersShort = "Ledamöter"

)

fun stringsForLanguage(language: AppLanguage): AppStrings = when (language) {
    AppLanguage.ENGLISH  -> EnglishStrings
    AppLanguage.FINNISH  -> FinnishStrings
    AppLanguage.SWEDISH  -> SwedishStrings
}

/**
 * CompositionLocal that provides the current AppStrings to any Composable in the tree.
 * Set at the top level in MainActivity, accessed anywhere with LocalStrings.current.
 */
val LocalStrings = compositionLocalOf<AppStrings> { EnglishStrings }