package com.hanggrian.localization

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
public annotation class LocalizationConfigurationDsl
