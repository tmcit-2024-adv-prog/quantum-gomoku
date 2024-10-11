plugins {
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
    mainClass = "org.example.App"
}

val jar by tasks.getting(Jar::class) {
  manifest {
     attributes["Main-Class"] = application.mainClass
  }
}

spotless {
    java {
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
        finalizedBy(jacocoTestReport)
    }
    jacocoTestReport {
        dependsOn(test)
        reports {
            xml.required = true
        }
    }
}
