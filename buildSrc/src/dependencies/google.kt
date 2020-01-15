const val VERSION_GUAVA = "28.1-jre"
const val VERSION_TRUTH = "1.0"

fun Dependencies.google(module: String, version: String) = "com.google.$module:$module:$version"
