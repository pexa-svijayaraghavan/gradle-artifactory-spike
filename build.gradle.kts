
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
        val isRelease = isReleaseBuild()
        val repoName = if(isRelease) "sample-repo-gradle-release-local" else "sample-repo-gradle-dev-local"
        setContextUrl("https://srisudarsan.jfrog.io/artifactory")
        publish(delegateClosureOf<org.jfrog.gradle.plugin.artifactory.dsl.PublisherConfig> {
            repository(delegateClosureOf<groovy.lang.GroovyObject> {
                setProperty("repoKey", repoName)
                setProperty("username", System.getenv("artifactory_user"))
                setProperty("password", System.getenv("artifactory_password"))
                setProperty("maven", true)
            })
            defaults(delegateClosureOf<groovy.lang.GroovyObject> {
                invokeMethod("publications", "myLibrary")
            })
        })
        resolve(delegateClosureOf<org.jfrog.gradle.plugin.artifactory.dsl.ResolverConfig> {
            setProperty("repoKey", repoName)
        })
        clientConfig.info.buildNumber = System.getenv("build_number")
        clientConfig.info.buildName = "sample-build"
    }
dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.google.guava:guava:30.1.1-jre")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}
publishing {
    publications {
        create<MavenPublication>("myLibrary") {
            from(components["java"])
        }
    }
}
fun isReleaseBuild(): Boolean{
   return  System.getProperty("isRelease") == "true"
}

fun getVersionBasedOnBuild(): String {
    val isRelease = isReleaseBuild()
    val artifactVersion = "0.0.1"
    return if (isRelease) artifactVersion else "${artifactVersion}-SNAPSHOT"
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
version = getVersionBasedOnBuild()
