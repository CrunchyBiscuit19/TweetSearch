## TweetSearch - Search Tweets From Screenshots

**Created by Sim Yue Yang**

TweetSearch is an Android app created in Jetpack Compose that allows its users to search for tweets based on screenshots of tweets.

Several different technologies were combined together to create this app.

* **Interacting with Twitter programmatically**
  * Twitter API
  * Twitter4J - Java library as an interface to the Twitter API. 

* **Android UI**
  * MVVM Architecture (Model-View-ViewModel)
  * Jetpack Compose
  * Navigation Compose
  * Accompanist Navigation Animations
  * Coil Image - Advanced image loading library for Kotlin.

* **Text Recognition**
  * Google's MLKit Text Recognition
  * FuzzyWuzzy - Library used to fuzzy match strings between screenshot and search results.

* **Miscellaneous**
  * Android Datastores - To store user's setting options.

### Installation

* Download the source code in this repository and open it in Android Studio. This will allow you to build the app from source.

### Demonstration

#### Successful Search
![Successful Search Demonstration GIF](https://media2.giphy.com/media/5VtwxoKbtpVFgeShAN/giphy.gif)

#### Unsuccessful Search
![Unsuccessful Search Demonstration GIF](https://media4.giphy.com/media/CI7BPjI0KsTYFTfwhV/giphy.webp)

### Future Work

* Create a database for each user to store their favorite search results. 