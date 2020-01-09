# TheMovieDBApp
A simple app practicing the MVVM pattern with TMDB REST Api. It has MVVM implemented along with repository pattern,
to separate the logic from the view. The App uses LiveData in the ViewModel, which gets the data from the repository.
Depending if there is Connection available, the repo gets the data either from a Network Service or the RoomDatabase.
There is also an Observer in the Activity which subscribes to the LiveData.
