package com.hendraanggrian.lokkal.codegen

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.takeFrom

object GitHubApi {
    private const val endPoint = "https://api.github.com"
    private val client: HttpClient = HttpClient(OkHttp) {
        install(JsonFeature) {
            serializer = GsonSerializer()
        }
    }

    suspend fun getLocales(): List<String> = client
        .get<List<Entry>> { apiUrl("repos/umpirsky/locale-list/contents/data") }
        .map { entry ->
            val name = entry.name
            val split = name.split('_')
            val suffix = split.drop(1)
            when {
                suffix.any { s -> s.any { it.isLowerCase() } } -> buildString {
                    append(split[0])
                    suffix.forEach { s ->
                        if (s.none { it.isLowerCase() }) {
                            append("_$s")
                        }
                    }
                }
                else -> name
            }
        }
        .distinct()

    private fun HttpRequestBuilder.apiUrl(path: String) {
        header(HttpHeaders.CacheControl, "no-cache")
        url {
            takeFrom(endPoint)
            encodedPath = path
        }
    }

    data class Entry(val name: String)
}
