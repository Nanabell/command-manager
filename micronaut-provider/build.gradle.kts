plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("io.micronaut.library") version "2.0.8"
    `maven-publish`
}

version = "1.0.0"
group = "dev.nanabell.jda.command.manager"

repositories {
    mavenCentral()
    maven("https://m2.dv8tion.net/releases")
    maven("https://zoidberg.ukp.informatik.tu-darmstadt.de/artifactory/public-releases/")
}

kapt {
    arguments {
        arg("micronaut.processing.incremental", true)
        arg("micronaut.processing.annotations", "dev.nanabell.jda.command.manager.*")
        arg("micronaut.processing.group", "dev.nanabell.jda.command.manager")
        arg("micronaut.processing.module", "micronaut-provider")
    }
}

micronaut {
    version("3.1.3")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("dev.nanabell.jda.command.manager.*")
    }
}

dependencies {
    api(project(":command-manager"))

    kapt("io.micronaut:micronaut-inject-java:3.1.0")
    implementation("io.micronaut:micronaut-context:3.1.3")

    kaptTest("io.micronaut:micronaut-inject-java:3.1.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation("io.micronaut.test:micronaut-test-junit5:3.0.3")

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
