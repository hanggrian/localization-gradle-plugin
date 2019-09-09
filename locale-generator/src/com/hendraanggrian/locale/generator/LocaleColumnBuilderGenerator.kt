package com.hendraanggrian.locale.generator

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import kotlinx.coroutines.runBlocking
import java.io.File

object LocaleColumnBuilderGenerator {
    private const val PACKAGE_NAME = "com.hendraanggrian.locale"
    private const val CLASS_NAME = "LocaleColumnBuilder"

    @JvmStatic
    fun main(@Suppress("UnusedMainParameter") args: Array<String>) {
        println("fetching...")
        val locales = runBlocking { GitHubApi.getLocales() }

        println("writing...")
        FileSpec.builder(PACKAGE_NAME, CLASS_NAME)
            .indent("\t")
            .addType(TypeSpec.interfaceBuilder(CLASS_NAME)
                .addKdoc("Generated interface based on `https://github.com/umpirsky/locale-list`.")
                .addAnnotation(
                    AnnotationSpec.builder(Suppress::class)
                        .addMember("%S", "unused")
                        .addMember("%S", "PropertyName")
                        .build()
                )
                .addSuperinterface(ClassName(PACKAGE_NAME, "Base$CLASS_NAME"))
                .addType(
                    TypeSpec.companionObjectBuilder()
                        .addModifiers(KModifier.PRIVATE)
                        .addProperty(
                            PropertySpec.builder("NO_GETTER", String::class)
                                .initializer("%S", "Property does not have a getter.")
                                .addModifiers(KModifier.CONST)
                                .build()
                        )
                        .addFunction(
                            FunSpec.builder("noGetter")
                                .returns(ClassName("kotlin", "Nothing"))
                                .addStatement("return throw UnsupportedOperationException(NO_GETTER)")
                                .build()
                        )
                        .build()
                )
                .apply {
                    locales.forEach { locale ->
                        val split = locale.split('_')
                        val language = split[0]
                        val country = split.getOrNull(1)
                        addProperty(
                            PropertySpec.builder(locale, String::class)
                                .addKdoc(buildString {
                                    append("Set locale value with language `$language`")
                                    when (country) {
                                        null -> append('.')
                                        else -> append(" and country `$country`.")
                                    }
                                })
                                .mutable()
                                .getter(
                                    FunSpec.getterBuilder()
                                        .addAnnotation(
                                            AnnotationSpec.builder(Deprecated::class)
                                                .addMember("NO_GETTER")
                                                .addMember("level = DeprecationLevel.ERROR")
                                                .build()
                                        )
                                        .addStatement("return noGetter()")
                                        .build()
                                )
                                .setter(
                                    FunSpec.setterBuilder()
                                        .addParameter("value", String::class)
                                        .addStatement(buildString {
                                            append("return add(")
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
                                        .build()
                                )
                                .build()
                        )
                    }
                }
                .build())
            .build()
            .writeTo(File("locale-gradle-plugin/src"))

        println("done!")
    }
}
