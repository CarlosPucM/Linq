plugins {
    alias(libs.plugins.android.library)
    id("maven-publish")
}

android {
    namespace = "com.example.linq"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            // withJavadocJar() // opcional si configuras javadoc/dokka
        }
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

// Identidad del artefacto
group = "com.example"
version = "0.4.0"

publishing {
    publications {
        create<MavenPublication>("release") {
            afterEvaluate {
                from(components["release"])
            }
            groupId = project.group.toString()
            artifactId = "linq"
            version = project.version.toString()

            pom {
                name.set("Linq")
                description.set("Linq-like helpers for Java/Android using StreamSupport")
                url.set("https://example.com/linq")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("nsplatform")
                        name.set("NSPlatform")
                    }
                }
                scm {
                    url.set("https://example.com/linq/scm")
                }
            }
        }
    }
    repositories {
        mavenLocal()
        // Para publicar a un repositorio remoto, agrega un bloque maven { url = uri("https://...") }
    }
}