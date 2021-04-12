group = RELEASE_GROUP
version = RELEASE_VERSION

plugins {
    application
    kotlin("jvm")
}

application {
    mainClass.set("$RELEASE_GROUP.$RELEASE_ARTIFACT.codegen.LokkalTextBuilderGenerator")
}

sourceSets {
    main {
        java.srcDir("src")
    }
    test {
        java.srcDir("tests/src")
    }
}

dependencies {
    implementation(kotlin("stdlib", VERSION_KOTLIN))
    implementation(kotlinx("coroutines-core", VERSION_COROUTINES))
    implementation(ktor("client-okhttp"))
    implementation(ktor("client-gson"))
    implementation(hendraanggrian("kotlinpoet-ktx", VERSION_KOTLINPOET_KTX))
    testImplementation(kotlin("test-junit"))
}

ktlint()