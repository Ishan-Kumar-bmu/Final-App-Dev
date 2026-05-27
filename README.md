# TuteDude E-Commerce App

A modern Android e-commerce application built with Kotlin and Jetpack Compose. 
This project demonstrates modern Android development practices including MVVM architecture, Dependency Injection with Hilt, Local Database caching with Room, and Cloud integrations with Firebase.

## Features

- **Authentication**: Register and Login using Firebase Authentication.
- **Home Screen**: View products uploaded by users (from Firestore) and recommended products (from FakeStore API using Retrofit).
- **Product Details**: Comprehensive view of a product, including uploader details and images.
- **Upload Product**: Upload new products with multiple images to Firebase Storage and Firestore.
- **Favorites**: Add products to your favorites list, stored locally using Room Database.
- **Modern UI**: Built entirely using Jetpack Compose with Material 3 design principles.

## Tech Stack

- **Language**: Kotlin
- **UI Toolkit**: Jetpack Compose
- **Architecture**: MVVM (Model-View-ViewModel)
- **Dependency Injection**: Hilt
- **Local Database**: Room
- **Network**: Retrofit & Moshi
- **Image Loading**: Coil
- **Backend/Cloud**: Firebase (Auth, Firestore, Storage)

## Setup Instructions

1. **Clone the repository**:
   Open the project in Android Studio.

2. **Configure Firebase**:
   - Go to the [Firebase Console](https://console.firebase.google.com/).
   - Create a new project and add an Android app with the package name `com.tutedude.ecommerce`.
   - Enable **Authentication** (Email/Password), **Firestore Database**, and **Storage**.
   - Download the `google-services.json` file.
   - Place the `google-services.json` file in the `app/` directory (replace the dummy file currently there).

3. **Build and Run**:
   - Sync the project with Gradle Files.
   - Run the application on an emulator or physical device.

## Note on APK and Screen Recording Submission

This repository contains the complete source code for the assignment. To generate the APK:
1. Open the project in Android Studio.
2. Go to `Build` -> `Build Bundle(s) / APK(s)` -> `Build APK(s)`.
3. The APK will be generated in `app/build/outputs/apk/debug/`.

To record the screen:
1. Run the app on an emulator.
2. Use the screen recording feature in Android Studio (Logcat window -> Screen Record icon).
