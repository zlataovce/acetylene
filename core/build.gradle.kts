dependencies {
    testImplementation(project(":srgutils"))
    testImplementation("com.fasterxml.jackson.core:jackson-databind:2.13.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}

tasks.withType<Test> {
    useJUnitPlatform()

    minHeapSize = "128M"
    maxHeapSize = "1024M"
}

java {
    withSourcesJar()
    withJavadocJar()
}