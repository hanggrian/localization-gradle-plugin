package com.hendraanggrian.locale

import java.util.Locale

interface LocaleBuilder {

    operator fun Locale.invoke(value: String)
}