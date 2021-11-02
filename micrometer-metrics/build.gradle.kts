plugins {
    kotlin("jvm")
    `java-library`
    `maven-publish`
}

group = "dev.nanabell.jda.command.manager"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    api(project(":command-manager"))
    implementation("io.micrometer:micrometer-core:1.7.5")

    testRuntimeOnly("ch.qos.logback:logback-classic:1.2.6")
}

java {
    sourceCompatibility = JavaVersion.toVersion("11")
    withSourcesJar()
    withJavadocJar()
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "11"
        }
    }
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "11"
        }
    }

    test {
        useJUnitPlatform()
    }

     register("prepareKotlinBuildScriptModel") {}
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()

            from(components["java"])
        }
    }
}
