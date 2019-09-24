package com.hendraanggrian.locale.generator

import com.hendraanggrian.kotlinpoet.buildFile
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.NOTHING
import java.io.File
import kotlinx.coroutines.runBlocking

object LocaleTableColumnBuilderGenerator {
    private const val PACKAGE_NAME = "com.hendraanggrian.locale"
    private const val CLASS_NAME = "LocaleTableColumnBuilder"

    @JvmStatic
    fun main(@Suppress("UnusedMainParameter") args: Array<String>) {
        println("fetching...")
        val locales = runBlocking { GitHubApi.getLocales() }

        println("writing...")
        buildFile(PACKAGE_NAME, CLASS_NAME) {
            indentCount = 4
            types.addInterface(CLASS_NAME) {
                kdoc.append("Generated interface based on `https://github.com/umpirsky/locale-list`.")
                annotations.add<Suppress> {
                    addMember("%S", "unused")
                    addMember("%S", "PropertyName")
                }
                addSuperInterface(ClassName(PACKAGE_NAME, "Base$CLASS_NAME"))
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
                        kdoc.append(buildString {
                            append("Set locale value with language `$language`")
                            when (country) {
                                null -> append('.')
                                else -> append(" and country `$country`.")
                            }
                        })
                        getter {
                            annotations.add<Deprecated> {
                                addMember("NO_GETTER")
                                addMember("level = DeprecationLevel.ERROR")
                            }
                            appendln("return noGetter()")
                        }
                        setter {
                            parameters.add<String>("value")
                            appendln(buildString {
                                append("return column(")
                                append(
                                    when {
                                        language == "en" && country == null -> "java.util.Locale.ENGLISH"
                                        language == "fr" && country == null -> "java.util.Locale.FRENCH"
                                        language == "de" && country == null -> "java.util.Locale.GERMAN"
                                        language == "it" && country == null -> "java.util.Locale.ITALIAN"
                                        language == "ja" && country == null -> "java.util.Locale.JAPANESE"
                                        language == "ko" && country == null -> "java.util.Locale.KOREAN"
                                        language == "zh" && country == null -> "java.util.Locale.CHINESE"
                                        language == "zh" && country == "CN" -> "java.util.Locale.CHINA"
                                        language == "zh" && country == "TW" -> "java.util.Locale.TAIWAN"
                                        language == "fr" && country == "FR" -> "java.util.Locale.FRANCE"
                                        language == "de" && country == "DE" -> "java.util.Locale.GERMANY"
                                        language == "it" && country == "IT" -> "java.util.Locale.ITALY"
                                        language == "ja" && country == "JP" -> "java.util.Locale.JAPAN"
                                        language == "ko" && country == "KR" -> "java.util.Locale.KOREA"
                                        language == "en" && country == "GB" -> "java.util.Locale.UK"
                                        language == "en" && country == "US" -> "java.util.Locale.US"
                                        language == "en" && country == "CA" -> "java.util.Locale.CANADA"
                                        language == "fr" && country == "CA" -> "java.util.Locale.CANADA_FRENCH"
                                        country == null -> "\"$language\", null"
                                        else -> "\"$language\", \"$country\""
                                    }
                                )
                                append(", value)")
                            })
                        }
                    }
                }
            }
        }.writeTo(File("locale-gradle-plugin/src"))

        println("done!")
    }
}
