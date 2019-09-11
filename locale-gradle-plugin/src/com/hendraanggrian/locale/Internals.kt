package com.hendraanggrian.locale

internal const val NO_GETTER: String = "Property does not have a getter."

internal fun noGetter(): Nothing = throw UnsupportedOperationException(NO_GETTER)
