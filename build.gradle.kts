
allprojects {
    repositories {
        mavenCentral()
    }
}
    plugins {
        id("org.jetbrains.kotlin.jvm") version "1.5.31"
        id("com.jfrog.artifactory") version "4.28.4"
        `maven-publish`
    }
    apply(plugin="com.jfrog.artifactory")
    artifactory {
        setContextUrl("https://srisudarsan.jfrog.io/artifactory")
        publish(delegateClosureOf<org.jfrog.gradle.plugin.artifactory.dsl.PublisherConfig> {
            repository(delegateClosureOf<groovy.lang.GroovyObject> {
                setProperty("repoKey", "sample-repo-gradle-dev-local")
                setProperty("username", System.getenv("artifactory_user") ?: "nouser")
                setProperty("password", System.getenv("artifactory_password") ?: "nopass")
                setProperty("maven", true)
            })
            defaults(delegateClosureOf<groovy.lang.GroovyObject> {
                invokeMethod("publications", "myLibrary")
            })
        })
        resolve(delegateClosureOf<org.jfrog.gradle.plugin.artifactory.dsl.ResolverConfig> {
            setProperty("repoKey", "sample-repo-gradle-dev")
        })
    }
dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // This dependency is used by the application.
    implementation("com.google.guava:guava:30.1.1-jre")

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}
publishing {
    publications {
        create<MavenPublication>("myLibrary") {
            from(components["java"])
        }
    }
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "sri.spike.artifactory.AppKt"
    }
}

group =  "sri.spike"
version = "0.0.1"