plugins {
    idea
    // TODO dokka
    // TODO nexus-publish
    kotlin("multiplatform") version "2.1.0" apply false
    // alias(libs.plugins.kotlinxBinaryCompatibilityValidator)
}

allprojects {
    repositories {
        mavenCentral()
    }
}

// apiValidation {
//     apiDumpDirectory = "api"
//     ignoredPackages.add("*.internal.*")
// }
