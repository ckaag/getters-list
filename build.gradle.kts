plugins {
    id("java")
}

group = "com.github.ckaag"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        url = uri("https://repo.karuslabs.com/repository/elementary-releases")
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.+"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    compileOnly("com.google.auto.service:auto-service:1.+")
    compileOnly("org.projectlombok:lombok"){
        version {
            strictly("[1.18, 1.19[")
            prefer("1.18.26")
        }
    }
    testImplementation("com.karuslabs:elementary:1.1.3")
    testImplementation("org.projectlombok:lombok:1.18.26")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.26")
}

tasks.test {
    useJUnitPlatform()
}