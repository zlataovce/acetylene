plugins {
    id("io.freefair.lombok") version "6.3.0" apply(false)
    id("maven-publish")
    id("java-library")
}

allprojects {
    group = "me.kcra.acetylene"
    version = "0.0.1-SNAPSHOT"
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    apply(plugin = "io.freefair.lombok")

    repositories {
        mavenCentral()
        maven("https://repo.screamingsandals.org/public")
    }

    dependencies {
        compileOnly("org.jetbrains:annotations:23.0.0")
    }

    tasks.withType<Jar> {
        archiveBaseName.set("${rootProject.name}-${project.name}")
    }

    configure<PublishingExtension> {
        publications {
            create<MavenPublication>("mavenJava") {
                from(components["java"])
                pom {
                    name.set("acetylene")
                    description.set("An unopinionated Java obfuscation mapping library")
                    url.set("https://github.com/zlataovce/acetylene")
                    licenses {
                        license {
                            name.set("MIT License")
                            url.set("https://github.com/zlataovce/acetylene/blob/master/LICENSE")
                        }
                    }
                    developers {
                        developer {
                            id.set("zlataovce")
                            name.set("Matouš Kučera")
                            email.set("mk@kcra.me")
                        }
                    }
                    scm {
                        connection.set("scm:git:github.com/zlataovce/acetylene.git")
                        developerConnection.set("scm:git:ssh://github.com/zlataovce/acetylene.git")
                        url.set("https://github.com/zlataovce/acetylene/tree/master")
                    }
                }
            }
        }

        repositories {
            maven {
                url = if ((project.version as String).endsWith("-SNAPSHOT")) uri("https://repo.kcra.me/snapshots")
                    else uri("https://repo.kcra.me/releases")
                credentials {
                    username = System.getenv("REPO_USERNAME")
                    password = System.getenv("REPO_PASSWORD")
                }
            }
        }
    }
}