plugins {
    base
    application
    id("com.diffplug.spotless") version "7.0.0.BETA2"
    id("com.github.spotbugs") version "6.0.24"
    jacoco
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.guava)
    compileOnly("com.github.spotbugs:spotbugs-annotations:4.8.6")
    testImplementation(platform("org.junit:junit-bom:5.11.3"))
	testImplementation("org.junit.jupiter:junit-jupiter")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.mockito:mockito-core:5.14.2")
}

base.archivesName.set(rootProject.name)

val configFileDir = layout.buildDirectory.dir("generated/config")
val configFileName = "BuildConfig.java"

val env = project.findProperty("config.env") as? String ?: "prod"
val customProperties = project.properties
    .filterKeys { it.startsWith("config.") }
    .toMutableMap()
customProperties["config.env"] = env

tasks.register("generateBuildConfig") {
    val outputDir = configFileDir.get().asFile
    val outputFile = File(outputDir, configFileName)

    inputs.properties(customProperties)
    outputs.file(outputFile)

    doLast {
        outputDir.mkdirs()

        // プロパティをJavaコードとして出力
        val propertiesCode = customProperties.entries.joinToString("\n") { (key, value) ->
            val constantName = key.removePrefix("config.").uppercase()
            "    public static final String $constantName = \"$value\";"
        }

        outputFile.writeText(
            """
            package jp.ac.metro_cit.adv_prog_2024.gomoku;

            public class BuildConfig {
            $propertiesCode
            }
            """.trimIndent()
        )
    }
}

sourceSets {
    named("main") {
        java.srcDir(configFileDir)
    }
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter("5.10.3")
        }
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass = "jp.ac.metro_cit.adv_prog_2024.gomoku.App"
}

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Main-Class"] = application.mainClass
    }
}

spotless {
    java {
        target("src/**/*.java")
        cleanthat()
        formatAnnotations()
        googleJavaFormat()
        importOrder("java|javax", "com.acme", "", "\\#com.acme", "\\#")
    }
}

tasks {
    spotbugsMain {
        reports.create("sarif") {
            enabled = true
        }
        jvmArgs = listOf("-Duser.language=ja")
    }
    spotbugsTest {
        reports.create("sarif") {
            enabled = true
        }
        jvmArgs = listOf("-Duser.language=ja")
    }
    test {
        useJUnitPlatform()
        finalizedBy(jacocoTestReport)
        maxHeapSize = "10240m"
        jvmArgs = listOf("-Xmx8G")
    }
    jacocoTestReport {
//        dependsOn(test)
        reports {
            xml.required = true
        }
    }
    named("compileJava") {
        dependsOn("generateBuildConfig")
    }
    named("spotlessJava") {
        dependsOn("generateBuildConfig")
    }
}
 