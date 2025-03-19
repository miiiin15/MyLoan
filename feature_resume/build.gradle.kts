plugins {
    id("local.library")
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.miiii15.myloan.resume"
}

dependencies {
    implementation(projects.featureBase)

    ksp(libs.roomCompiler)

    // TODO : 테스트 모듈 작성 시
//    testImplementation(projects.libraryTestUtils)
    testImplementation(libs.bundles.test)

    testRuntimeOnly(libs.junitJupiterEngine)
}
