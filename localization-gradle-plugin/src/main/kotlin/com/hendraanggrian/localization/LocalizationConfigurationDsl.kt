package com.hendraanggrian.localization

/**
 * Forces localizations configurations to be on the same level, such as:
 *
 * ```
 * localization {
 *     text { }
 *     "" { }
 * }
 * ```
 */
@DslMarker
@Target(AnnotationTarget.CLASS)
annotation class LocalizationConfigurationDsl
