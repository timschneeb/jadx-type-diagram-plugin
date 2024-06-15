import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    `java-library`

    id("com.github.johnrengelman.shadow") version "8.1.1"

	// auto update dependencies with 'useLatestVersions' task
	id("se.patrikerdes.use-latest-versions") version "0.2.18"
	id("com.github.ben-manes.versions") version "0.51.0"
}

dependencies {
	// use compile only scope to exclude jadx-core and its dependencies from result jar
    compileOnly("io.github.skylot:jadx-core:1.5.0-SNAPSHOT") {
        isChanging = true
    }

	implementation("org.slf4j:slf4j-api:2.0.13")

	implementation("org.netbeans.api:org-netbeans-api-visual:RELEASE220")
	implementation("org.netbeans.api:org-openide-util:RELEASE220")
	implementation("org.netbeans.api:org-openide-util-lookup:RELEASE220")

	implementation("com.formdev:flatlaf:3.4.1")
	implementation("com.formdev:flatlaf-extras:3.4.1")

	implementation("com.google.code.findbugs:jsr305:3.0.2")

	testImplementation("ch.qos.logback:logback-classic:1.4.14")
    testImplementation("org.assertj:assertj-core:3.24.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.1")

	testImplementation("io.github.skylot:jadx-smali-input:1.5.0-SNAPSHOT") {
        isChanging = true
    }
}

repositories {
    mavenCentral()
    maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots/")
    google()
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

version = System.getenv("VERSION") ?: "dev"

tasks {
    withType(Test::class) {
        useJUnitPlatform()
    }
    val shadowJar = withType(ShadowJar::class) {
        archiveClassifier.set("") // remove '-all' suffix
    }

    // copy result jar into "build/dist" directory
    register<Copy>("dist") {
        dependsOn(shadowJar)
        dependsOn(withType(Jar::class))

        from(shadowJar)
        into(layout.buildDirectory.dir("dist"))
    }
}
