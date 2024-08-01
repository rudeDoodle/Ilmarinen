plugins {
    id("java")
}

var lombokVersion = "1.18.34"
var jdaVersion = "5.0.1"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")

    implementation("net.dv8tion:JDA:$jdaVersion") { // replace $version with the latest version
        // Optionally disable audio natives to reduce jar size by excluding `opus-java`, we don't need it for now
        exclude(module = "opus-java")
    }
}