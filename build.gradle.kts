import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    `java-library`

    id("com.github.johnrengelman.shadow") version "8.1.1"

	// auto update dependencies with 'useLatestVersions' task
	id("se.patrikerdes.use-latest-versions") version "0.2.18"
	id("com.github.ben-manes.versions") version "0.52.0"
}

dependencies {
	// use compile only scope to exclude jadx-core and its dependencies from result jar
	val jadxVersion = "1.5.2-SNAPSHOT"
	val isJadxSnapshot = jadxVersion.endsWith("-SNAPSHOT")

	// use compile only scope to exclude jadx-core and its dependencies from result jar
	compileOnly("io.github.skylot:jadx-core:$jadxVersion") {
		isChanging = isJadxSnapshot
	}
	compileOnly("io.github.skylot:jadx-gui:$jadxVersion") {
		isChanging = isJadxSnapshot
	}

	implementation("org.slf4j:slf4j-api:2.1.0-alpha1")

	implementation("org.netbeans.api:org-netbeans-api-visual:RELEASE240")
	implementation("org.netbeans.api:org-openide-util:RELEASE240")
	implementation("org.netbeans.api:org-openide-util-lookup:RELEASE240")

	implementation("com.formdev:flatlaf:3.5.4")
	implementation("com.formdev:flatlaf-extras:3.5.4")

	implementation("com.google.code.findbugs:jsr305:3.0.2")

	testImplementation("ch.qos.logback:logback-classic:1.5.16")
    testImplementation("org.assertj:assertj-core:3.27.3")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.12.0-RC2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.12.0-RC2")
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
