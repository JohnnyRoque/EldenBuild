# Elden Build Android App

## A fully functional android app for creating and managing  character build for the game Elden Ring. 

This app was written in Kotlin, following the Material Design 3 guidelines, using Room data persistence library to store the builds, and REST API Retrofit to transmit data between app and server.

Main components:

* Android Jetpack Components (ViewModel, Flow, LiveData, Databinding, Navigation, Lifecycle, Paging 3)
* Software architecture - MVVM 
* TypeAdapters - Gson
* Dependency injection - Koin
* Unit testing with Mockito
* Image loading - COIL

## Kotlin

The app is entirely written in Kotlin and uses Jetpack's Android Ktx extensions.

Asynchronous tasks are handled with coroutines. Coroutines allow for simple and safe management of one-shot operations as well as building and consuming streams of data using Kotlin Flows.

## Architecture
The architecture is built around Android Architecture Components and follows the recommendations laid out in the Guide to App Architecture. Logic is kept away from Activities and Fragments and moved to ViewModels. Data is observed using Kotlin Flows and the Data Binding Library binds UI components in layouts to the app's data sources.

## Features
Elden build allows you to easily create and store your builds on your cell phone. In this app, game items are listed in categories and can be added to and removed from your build, as well as modifying your character's attributes.

<img src="https://github.com/JohnnyRoque/EldenBuild/assets/146282434/deb48d1b-550c-4a61-82dc-c2c7dba3385d" alt="Image"  />

## Special thanks

Shout out to @deliton, for creating the amazing Elden Ring Api that provides all the information about the items featured in the game. 
Link for the Elden Ring Api repository -> http://www.github.com/deliton/eldenring-api

## Developer's note
This is a work in progress and new features and adjustments to the UI will be implemented. If you find any problems or would like to submit an improvement to this project, feel free to use the issue tab above.
