import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.1.5"
	id("io.spring.dependency-management") version "1.1.3"
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.spring") version "1.8.22"
	kotlin("plugin.jpa") version "1.8.22"
}

group = "seoultech"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-batch")
	implementation("org.springframework.boot:spring-boot-starter-jdbc")
//	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
//	implementation("org.springframework.boot:spring-boot-starter-quartz")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	runtimeOnly("com.mysql:mysql-connector-j")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.batch:spring-batch-test")
	implementation("io.github.oshai:kotlin-logging-jvm:5.1.0")
//	compileOnly("com.github.jojoldu.spring-batch-querydsl:spring-batch-querydsl-reader:2.4.8")



	// exposed
//	implementation ("org.jetbrains.exposed:exposed-core:0.44.1")
//	implementation ("org.jetbrains.exposed:exposed-crypt:0.44.1")
//	implementation ("org.jetbrains.exposed:exposed-dao:0.44.1")
//	implementation ("org.jetbrains.exposed:exposed-jdbc:0.44.1")
//	implementation ("org.jetbrains.exposed:exposed-kotlin-datetime:0.44.1")
//	implementation ("org.jetbrains.exposed:exposed-json:0.44.1")
//	implementation ("org.jetbrains.exposed:exposed-money:0.44.1")
//	implementation ("org.jetbrains.exposed:exposed-spring-boot-starter:0.44.1")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
