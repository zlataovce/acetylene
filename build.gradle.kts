plugins {
    id("io.freefair.lombok") version "6.3.0" apply(false)
    java
}

allprojects {
    group = "me.kcra.acetylene"
    version = "0.0.1-SNAPSHOT"
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "io.freefair.lombok")

    repositories {
        mavenCentral()
    }
}