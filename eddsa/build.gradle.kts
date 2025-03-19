plugins {
    kotlin("multiplatform")
}

kotlin {
    explicitApi()
    applyDefaultHierarchyTemplate()


    jvmToolchain(11)
    jvm {
        withJava()
        compilerOptions {
            javaParameters = true
            freeCompilerArgs.addAll("-Xjvm-default=all", "-Xjsr305=strict")
        }
    }

    js {
        nodejs()
        binaries.library()
    }

    val optimizedNotAvailableTargets = setOf(
        "linuxX64",
        "linuxArm64",
        "mingwX64",
        "androidNativeArm32",
        "androidNativeArm64",
        "androidNativeX86",
        "androidNativeX64",
    )

    // see https://kotlinlang.org/docs/native-target-support.html
    arrayOf(
        // tier1
        macosX64(),
        macosArm64(),
        iosSimulatorArm64(),
        iosX64(),
        iosArm64(),
        // tier2
        linuxX64(),
        linuxArm64(),
        watchosSimulatorArm64(),
        watchosX64(),
        watchosArm32(),
        watchosArm64(),
        tvosSimulatorArm64(),
        tvosX64(),
        tvosArm64(),
        // tier3
        androidNativeArm32(),
        androidNativeArm64(),
        androidNativeX86(),
        androidNativeX64(),
        mingwX64(),
        watchosDeviceArm64(),
    ).forEach { target ->
        target.binaries {
            if (target.name !in optimizedNotAvailableTargets) {
                framework {
                    optimized = true
                }
            }
        }
    }
}

tasks.named("compileJava", JavaCompile::class.java) {
    val moduleName = "love.forte.net.i2p.crypto.eddsa"

    options.compilerArgumentProviders.add(CommandLineArgumentProvider {
        // Provide compiled Kotlin classes to javac – needed for Java/Kotlin mixed sources to work
        listOf("--patch-module", "$moduleName=${sourceSets["main"].output.asPath}")
    })
}
