# Tandur Mobile App
Tandur is a mobile application that help users especially farmers by recommending and predicting the best crops based on geospatial conditions.

# About the Android Project
This application is implementing MVVM architecture pattern where the view (activity/fragment) observe live data from the view model. 
While the data that observed in view model is obtained from data source layer (model and repository). 
Then in this project data source is the only layer that connecting response API to presentation layer (view and view model) so it can be viewed by the users.

# Tech Stack
- Kotlin Programming Language
- Jetpack Libraries
- Retrofit2
- Glide
- Koin Dependency Injection

# RestAPI
- Tandur RestAPI

# Project Installation
1. Clone the repository

   ```sh
   git clone https://github.com/Tandur-Team/tandur-android.git
   cd tandur-android
   ```
   
2. Setup google maps API
   - Add your secret Google Maps API Key at `local.properties`
     ```kotlin
     MAPS_API_KEY = <API_KEY>

   - Add your Maps API Key in `AndroidManifest.xml`
     ```kotlin
     <meta-data
       android:name="com.google.android.geo.API_KEY"
       android:value="${MAPS_API_KEY}" />
     ```
3. Run the app from emulator or physical device 
