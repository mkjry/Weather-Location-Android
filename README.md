Coding procedure (Retrofit + live data + Kotlin)

1. Dependency : Retrofit, LiveData and ViewModel

https://github.com/mkjry/Weather-Location-Android/blob/main/app/build.gradle.kts
   
2. Data class : Data model for API response

https://github.com/mkjry/Weather-Location-Android/blob/main/app/src/main/java/com/ssj/weather_location_android/model/Weather.kt

![image](https://github.com/mkjry/Weather-Location-Android/assets/132794460/b98c4f2e-fd13-4fed-8fc1-98c8286d88a9)

3. response data type : Interface of Retrofit for return data type, name

https://github.com/mkjry/Weather-Location-Android/blob/main/app/src/main/java/com/ssj/weather_location_android/network/WeatherDataRequest.kt
   
![image](https://github.com/mkjry/Weather-Location-Android/assets/132794460/2fbf3667-a6ad-4217-bf6c-1d6b8cbd77c8)

5. Set Up Retrofit : Retrofit instance, initialize

https://github.com/mkjry/Weather-Location-Android/blob/main/app/src/main/java/com/ssj/weather_location_android/network/RetrofitClient.kt

![image](https://github.com/mkjry/Weather-Location-Android/assets/132794460/affefba1-e8ae-471a-85c8-53764716e415)

5. Repository : extract data from API response

 https://github.com/mkjry/Weather-Location-Android/blob/main/app/src/main/java/com/ssj/weather_location_android/network/WeatherRepository.kt
 
 ![image](https://github.com/mkjry/Weather-Location-Android/assets/132794460/807a2701-b03f-4297-9568-0a7a9d5ae207)

6. ViewModel class with live data

https://github.com/mkjry/Weather-Location-Android/blob/main/app/src/main/java/com/ssj/weather_location_android/viewmodel/WeatherViewModel.kt

![image](https://github.com/mkjry/Weather-Location-Android/assets/132794460/0824be73-5d96-48f6-bb8a-066539360b07)

7. Observe in UI class (activity/fragment)

 https://github.com/mkjry/Weather-Location-Android/blob/main/app/src/main/java/com/ssj/weather_location_android/view/MainActivity.kt
 
![image](https://github.com/mkjry/Weather-Location-Android/assets/132794460/373d78b5-5a12-41c7-b7d2-57c61e47f53d)


8. layout_.xml
