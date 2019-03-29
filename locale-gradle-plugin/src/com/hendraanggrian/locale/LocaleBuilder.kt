package com.hendraanggrian.locale

interface LocaleBuilder {

    private companion object {
        const val NO_GETTER: String = "Property does not have a getter."

        fun noGetter(): Nothing = throw UnsupportedOperationException(NO_GETTER)
    }

    fun en(value: String)

    var en: String
        @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR) get() = noGetter()
        set(value) = en(value)

    fun de(value: String)

    var de: String
        @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR) get() = noGetter()
        set(value) = de(value)
}