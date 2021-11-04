plugins {
    kotlin("jvm")
    `java-library`
    `maven-publish`
}

group = "dev.nanabell.jda.command.manager"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://m2.dv8tion.net/releases")
    maven("https://zoidberg.ukp.informatik.tu-darmstadt.de/artifactory/public-releases/")
}

dependencies {
    implementation(kotlin("stdlib", "1.5.31"))
    implementation(kotlin("reflect", "1.5.31"))

    implementation("org.slf4j:slf4j-api:1.7.32")
    implementation("jakarta.inject:jakarta.inject-api:2.0.1.MR")

    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation("org.mockito:mockito-junit-jupiter:4.0.0")

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
