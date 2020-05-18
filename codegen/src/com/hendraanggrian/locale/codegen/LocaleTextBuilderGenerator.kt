package com.hendraanggrian.locale.codegen

import com.hendraanggrian.kotlinpoet.buildFileSpec
import com.hendraanggrian.kotlinpoet.classOf
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.NOTHING
import java.io.File
import kotlin.system.exitProcess
import kotlinx.coroutines.runBlocking

object LocaleTextBuilderGenerator {
    private const val PACKAGE_NAME = "com.hendraanggrian.locale"
    private const val CLASS_NAME = "LocaleTextBuilder"

    @JvmStatic fun main(args: Array<String>) {
        println("Fetching...")
        val locales = runBlocking { GitHubApi.getLocales() }

        println("Writing...")
        buildFileSpec(PACKAGE_NAME, CLASS_NAME) {
            indentSize = 4
            addImport("java.util", "Locale")
            types.addInterface(CLASS_NAME) {
                kdoc {
                    appendln("Locales listed below are based on [umpirsky's locale-list](https://github.com/umpirsky/locale-list).")
                    appendln()
                    appendln("This is a generated class from `codegen` module.")
                }
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
                            when (country) {
                                null -> appendln("return add(%S, value)", language)
                                else -> appendln("return add(%S, %S, value)", language, country)
                            }
                        }
                    }
                }
            }
        }.writeTo(File("locale-gradle-plugin/src"))

        println("Done!")
        exitProcess(0)
    }
}
