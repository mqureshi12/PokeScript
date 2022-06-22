package com.mohammad.pokescript.utilities

// Class to be used as a wrapper for when I fetch data from the poke api

sealed class Resource<T>(val data: T?, val message: String?) { // Takes a generic type

    // When pokemon data gets delivered back to activity and fragments,
    // it will be wrapped in this resource wrapper and I can tell whether
    // it was successful (put in list view) or if there was an error (show toast)

    class Success<T>(data: T) : Resource<T>(data, null) // Returns resource with data and a null message
    class Error<T>(message: String) : Resource<T>(null, message)
    class Loading<T>(message: String) : Resource<T>(null, message)
    class Expired<T>(message: String, data: T) : Resource<T>(data, message) // Used if cache is expired
}
