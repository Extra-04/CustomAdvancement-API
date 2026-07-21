// 공개 API 모듈.
//
// 이 모듈은 인터페이스와 이벤트만 담는다. 구현체(플러그인 본체)에는 절대 의존하지 않는다.
// 그래야 다른 개발자가 이 jar 하나만 받아서 컴파일할 수 있고, 본체 소스를 공개하지 않아도 된다.
//
// 언어를 Java 로 둔 것도 같은 이유다. Kotlin 으로 만들면 이 API 를 쓰는 쪽이
// kotlin-stdlib 를 함께 넣어야 하고, 널 가능성 메타데이터까지 신경 써야 한다.
plugins {
    `java-library`
    `maven-publish`
}

group = "io.github.extra04"
// 고정값으로 둔다. 이 폴더는 플러그인 본체의 하위 모듈로도, 단독 저장소로도 빌드되는데
// rootProject.version 을 참조하면 단독 빌드(예: JitPack)에서 "unspecified" 가 된다.
version = "1.0"

java {
    toolchain { languageVersion.set(JavaLanguageVersion.of(21)) }
    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    // Bukkit/Paper 외에는 아무것도 쓰지 않는다. 의존성이 늘면 API 로서의 가치가 떨어진다.
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    compileOnly("org.jetbrains:annotations:26.0.2")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc> {
    (options as StandardJavadocDocletOptions).encoding = "UTF-8"
    (options as StandardJavadocDocletOptions).charSet = "UTF-8"
    // 공개 API 라 문서가 곧 사용 설명서다. 경고를 숨기지 않는다.
    isFailOnError = false
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "customadvancement-api"
            from(components["java"])
        }
    }
}
