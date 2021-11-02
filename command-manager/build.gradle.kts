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
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.5.31")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.5.31")

    compileOnly("io.micronaut:micronaut-context:3.1.3")
    implementation("io.micrometer:micrometer-core:1.7.5")

    compileOnly("net.dv8tion:JDA:4.3.0_277") {
        exclude("opus-java")
    }

    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation("org.mockito:mockito-core:4.0.0")
    testImplementation("org.mockito:mockito-junit-jupiter:4.0.0")
    testImplementation("gnu.trove:trove:3.0.3")
    testImplementation("net.dv8tion:JDA:4.3.0_277") {
        exclude("opus-java")
    }

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
