plugins {
    id("io.freefair.lombok") version "6.3.0" apply(false)
    id("java-library")
}

allprojects {
    group = "me.kcra.acetylene"
    version = "0.0.1-SNAPSHOT"
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "io.freefair.lombok")

    repositories {
        mavenCentral()
    }

    dependencies {
        compileOnly("org.jetbrains:annotations:23.0.0")
    }
}