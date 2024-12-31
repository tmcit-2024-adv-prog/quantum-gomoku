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
    compileOnly("com.github.spotbugs:spotbugs-annotations:4.8.6")
    testImplementation(platform("org.junit:junit-bom:5.11.3"))
	testImplementation("org.junit.jupiter:junit-jupiter")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.mockito:mockito-core:5.14.2")
}

sourceSets {
    val main by getting {
        java {
            setSrcDirs(listOf("src/main/java"))
        }
    }
    val dev by creating {
        java {
            setSrcDirs(listOf("src/dev/java"))
        }
        compileClasspath += main.output
        runtimeClasspath += main.output
    }
    val testDev by creating {
        java {
            setSrcDirs(listOf("src/test_dev/java"))
        }
        compileClasspath += main.output + dev.output
        runtimeClasspath += main.output + dev.output
    }
}

configurations {
    val devImplementation by getting {
        extendsFrom(configurations["implementation"])
    }
    val devCompileOnly by getting {
        extendsFrom(configurations["compileOnly"])
    }
    val testDevImplementation by getting {
        extendsFrom(configurations["testImplementation"])
    }
    val testDevCompileOnly by getting {
        extendsFrom(configurations["testCompileOnly"])
    }
    val testDevRuntimeOnly by getting {
        extendsFrom(configurations["testRuntimeOnly"])
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
    val buildDev by creating(Jar::class) {
        dependsOn(distZip)
        dependsOn(distTar)
        archiveClassifier.set("dev")
        from(sourceSets["main"].output)
        from(sourceSets["dev"].output)
        manifest {
            attributes["Main-Class"] = application.mainClass
        }
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
    val buildProd by creating(Jar::class) {
        dependsOn(distZip)
        dependsOn(distTar)
        from(sourceSets["main"].output)
        manifest {
            attributes["Main-Class"] = application.mainClass
        }
    }
    build {
        group = "build"
        description = "Builds the specified environment (e.g., -Penv=dev or default to prod)"

        // 環境指定のプロパティを取得
        val env = project.findProperty("env") as? String ?: "prod"

        // 環境ごとにビルドタスクを設定
        when (env) {
            "dev" -> dependsOn(buildDev)
            "prod" -> dependsOn(buildProd)
            else -> throw GradleException("Invalid environment: $env. Use 'dev' or default to 'prod'.")
        }
    }
    named<JavaExec>("run") {
        group = "application"
        description = "Runs the specified environment (e.g., -Penv=dev or default to prod)"

        // 環境指定のプロパティを取得
        val env = project.findProperty("env") as? String ?: "prod"

        // 環境ごとにビルドタスクを設定
        when (env) {
            "dev" -> {
                dependsOn(buildDev)
                classpath = files(buildDev.archiveFile.get().asFile)
            }
            "prod" -> {
                dependsOn(buildProd)
                classpath = files(buildProd.archiveFile.get().asFile)
            }
            else -> throw GradleException("Invalid environment: $env. Use 'dev' or default to 'prod'.")
        }

        mainClass.set(application.mainClass)
    }
    val testDev by creating(Test::class) {
        group = "verification"
        description = "Runs the tests for the development environment"
        testClassesDirs = sourceSets["testDev"].output.classesDirs
        classpath = sourceSets["testDev"].runtimeClasspath
    }
    val testProd by creating(Test::class) {
        group = "verification"
        description = "Runs the tests for the production environment"
        testClassesDirs = sourceSets["main"].output.classesDirs
        classpath = sourceSets["main"].runtimeClasspath
    }
    test {
        group = "verification"
        description = "Runs the tests for the specified environment (e.g., -Penv=dev or default to prod)"

        // 環境指定のプロパティを取得
        val env = project.findProperty("env") as? String ?: "prod"

        // 環境ごとにビルドタスクを設定
        useJUnitPlatform()
        when (env) {
            "dev" -> dependsOn(testDev)
            "prod" -> dependsOn(testProd)
            else -> throw GradleException("Invalid environment: $env. Use 'dev' or default to 'prod'.")
        }

        finalizedBy(jacocoTestReport)
    }
    jacocoTestReport {
        dependsOn(test)
        reports {
            xml.required = true
        }
    }
}
