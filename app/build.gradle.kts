plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    signingConfigs {
        getByName("debug") {
            storeFile = file("D:\\AndroidStudioProjects\\Chat_Simple_Application\\my_release.jks")
            storePassword = "121001"
            keyAlias = "key0"
            keyPassword = "121001"
        }
    }
    namespace = "com.example.chat_simple_application"
    compileSdk = 34

    packagingOptions{
        exclude("META-INF/DEPENDENCIES")
    }

    defaultConfig {
        applicationId = "com.example.chat_simple_application"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.hbb20:ccp:2.7.3")
    implementation("com.google.firebase:firebase-firestore:25.0.0")
    implementation("com.google.firebase:firebase-storage:21.0.0")
    implementation("com.google.firebase:firebase-messaging:24.0.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-auth:23.0.0")
    implementation("com.firebaseui:firebase-ui-firestore:8.0.2")
    implementation ("com.github.dhaval2404:imagepicker:2.1")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
//    implementation ("com.google.auth:google-auth-library-oauth2-http:1.24.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}