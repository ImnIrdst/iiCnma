# iiCnma
A playground android app, showcasing the latest technologies and architecture using the [Movie Database](https://www.themoviedb.org/) APIs.

# Demo
<span><img src="https://github.com/ImnIrdst/iiCnma/blob/main/demo/iicnma-home-detail.gif" width="170" height="320"></span>
<span><img src="https://github.com/ImnIrdst/iiCnma/blob/main/demo/iicnma-search.gif" width="170" height="320"></span>
<span><img src="https://github.com/ImnIrdst/iiCnma/blob/main/demo/iicnma-favorites.gif" width="170" height="320"></span>

# Technologies

- Kotlin Coroutines, Flow, StateFlow
- Hilt
- Paging3
- Navigation Component
- LiveData
- ViewModel
- Room
- Retrofit
- OkHttp3
- Glide
- jUnit
- Mockk
- Coroutine Test

# Architecture
A custom architecture inspired by the [Google MVVM](https://developer.android.com/jetpack/guide) and the [Clean architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html).

This architecture allows app to be offline first. It gets data from the network if it doesn't exist in the local database and persists it. Local database is the single source of truth of the app and after its data changes, it notifies other layers using coroutine flows. 

# Build
Clone the repository and get an [API key](https://www.themoviedb.org/settings/api) from the Movie Database and put it in the `local.properties` file as below:

```JSON
apikey="YOUR_API_KEY"
```
