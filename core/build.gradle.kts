dependencies {
    testImplementation(project(":srgutils"))
    testImplementation("net.minecraftforge:srgutils:0.4.11-SNAPSHOT") // need to have this here
    testImplementation("com.fasterxml.jackson.core:jackson-databind:2.13.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}

tasks.withType<Test> {
    useJUnitPlatform()
}