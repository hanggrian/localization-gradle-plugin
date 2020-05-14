package com.hendraanggrian.locale.codegen

import com.hendraanggrian.kotlinpoet.buildFileSpec
import com.hendraanggrian.kotlinpoet.classOf
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.NOTHING
import java.io.File
import kotlin.system.exitProcess
import kotlinx.coroutines.runBlocking

object LocaleTableColumnBuilderGenerator {
    private const val PACKAGE_NAME = "com.hendraanggrian.locale"
    private const val CLASS_NAME = "LocaleTableColumnBuilder"

    @JvmStatic fun main(args: Array<String>) {
        println("Fetching...")
        val locales = runBlocking { GitHubApi.getLocales() }

        println("Writing...")
        buildFileSpec(PACKAGE_NAME, CLASS_NAME) {
            indentSize = 4
            addComment("Generated file, do not modify manually.")
            addImport("java.util", "Locale")
            types.addInterface(CLASS_NAME) {
                kdoc += "Locales listed below are based on [umpirsky's locale-list](https://github.com/umpirsky/locale-list)."
                annotations.add<Suppress> {
                    addMember("%S", "unused")
                    addMember("%S", "PropertyName")
                }
                superinterfaces[PACKAGE_NAME.classOf("Base$CLASS_NAME")] = null
                types.addCompanionObject {
                    addModifiers(KModifier.PRIVATE)
                    properties.add<String>("NO_GETTER") {
                        initializer("%S", "Property does not have a getter.")
                        addModifiers(KModifier.CONST)
                    }
                    functions.add("noGetter") {
                        returns = NOTHING
                        appendln("return throw UnsupportedOperationException(NO_GETTER)")
                    }
                }
                locales.forEach { locale ->
                    val split = locale.split('_')
                    val language = split[0]
                    val country = split.getOrNull(1)
                    properties.add<String>(locale) {
                        isMutable = true
                        kdoc {
                            append("Set locale value with language `$language`")
                            if (country != null) append(" and country `$country`")
                            append(".")
                        }
                        getter {
                            annotations.add<Deprecated> {
                                addMember("NO_GETTER")
                                addMember("level = DeprecationLevel.ERROR")
                            }
                            appendln("return noGetter()")
                        }
                        setter {
                            parameters.add<String>("value")
                            append("return column(")
                            append(
                                when {
                                    language == "en" && country == null -> "Locale.ENGLISH"
                                    language == "fr" && country == null -> "Locale.FRENCH"
                                    language == "de" && country == null -> "Locale.GERMAN"
                                    language == "it" && country == null -> "Locale.ITALIAN"
                                    language == "ja" && country == null -> "Locale.JAPANESE"
                                    language == "ko" && country == null -> "Locale.KOREAN"
                                    language == "zh" && country == null -> "Locale.CHINESE"
                                    language == "zh" && country == "CN" -> "Locale.CHINA"
                                    language == "zh" && country == "TW" -> "Locale.TAIWAN"
                                    language == "fr" && country == "FR" -> "Locale.FRANCE"
                                    language == "de" && country == "DE" -> "Locale.GERMANY"
                                    language == "it" && country == "IT" -> "Locale.ITALY"
                                    language == "ja" && country == "JP" -> "Locale.JAPAN"
                                    language == "ko" && country == "KR" -> "Locale.KOREA"
                                    language == "en" && country == "GB" -> "Locale.UK"
                                    language == "en" && country == "US" -> "Locale.US"
                                    language == "en" && country == "CA" -> "Locale.CANADA"
                                    language == "fr" && country == "CA" -> "Locale.CANADA_FRENCH"
                                    country == null -> "\"$language\", null"
                                    else -> "\"$language\", \"$country\""
                                }
                            )
                            append(", value)\n")
                        }
                    }
                }
            }
        }.writeTo(File("locale-gradle-plugin/src"))

        println("Done!")
        exitProcess(0)
    }
}
