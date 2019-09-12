package com.hendraanggrian.locale

internal const val NO_GETTER: String = "Property does not have a getter."

internal fun noGetter(): Nothing = throw UnsupportedOperationException(NO_GETTER)

internal fun localeTableOf(): LocaleTable = LocaleTable.create(
    { o1, o2 -> o1.compareTo(o2) },
    { o1, o2 -> o1.language.compareTo(o2.language) })
