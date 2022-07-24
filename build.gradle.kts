
allprojects {
    repositories {
        mavenCentral()
    }
}
    plugins {
        id("org.jetbrains.kotlin.jvm") version "1.5.31"
        `maven-publish`
    }
dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.google.guava:guava:30.1.1-jre")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "sri.spike.artifactory.AppKt"
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "11"
    }
}

group =  "sri.spike"
version = "0.0.1"
