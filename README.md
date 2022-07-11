# PokeScript

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
PokeScript is an Android app that allows users to search through a list of Pokemon (creatures from the popular gaming and film / TV franchise of the same name) and view their statistics, class rating, abilities, type and location. It also allows users to save Pokemon into their personal party to mimic their in-game roster.

To go beyond CodePath and increase complexity, I have learned and utilized advanced topics, processes, and technologies like Kotlin, the MVVM architecture style, dependency injection, coroutines, pagination and more. I have also used utilized layout types not used in the course curriculum and developed some xml files on my own as well as a custom app icon.

### App Evaluation
[Evaluation of your app across the following attributes]
- **Category:** Entertainment / Gaming / Informational
- **Mobile:** This app would be engineered for mobile as Pokemon games are primarily developed for small movable devices allowing for users to easily have an information bank alongside their handheld gaming device.
- **Story:** Gamers and Pokemon fans are given a chance to improve their knowledge in the area and gain access to useful in-game Pokemon information quickly, in a fun way. As the app fetches Pokemon from the API, it can also be used to quickly see new Pokemon announced when a new game is releasing assuming the API is updated with information in a timely manner.
- **Market:** Users include gamers and Pokemon fans who need to get quick information while playing a Pokemon game or planning out their party. Other applications for this exists, but many are bloated with too much information or views and are designed poorly without the user or UI/UX in mind.
- **Habit:** They may use this app many days in a row, for short bursts, perhaps weeks at time while playing a game, followed by a pause.
- **Scope:** A functioning app will be manageable to complete. Challenges may arise when attempting to implement 3rd party sign on and the planned periodic background searches to the API while the app is not open, with that goal specifically greatly increasing the scope of the app. Utilizing planned new topics, technologies and processes like Kotlin, the Facebook Login SDK, MVVM, dependency injection, and coroutines will also increase the complexity and difficulty in reaching the app plans.

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* [X] User can scroll through a list of Pokemon from the Pokemon API
* [X] Users can search for a specific Pokemon by name
* [X] User can click on a Pokemon in the list view to see more details
  * Statistics, class rating (calculated by getting the average of all statistics), abilities, type, and location
* [X] User can access a game-based map from the main view with Pokemon plotted onto certain, different parts of the map 
* [X] User can sign up with a new profile
* [X] User can log in / log out of the app
* [X] User can save / delete Pokemon to and from their party with the information stored
* [X] The app has multiple views
* [X] The app uses at least one gesture (user can double tap the individual Pokemon map in the details fragment to go to the map view and see all Pokemon together with linked locations)
* [X] The app incorporates at least one external library to add visual polish ([Android Ripple Background](https://github.com/skyfishjy/android-ripple-background), [CircularImageView](https://github.com/lopspower/CircularImageView))
* [X] The app uses at least one animation (from Pokemon list fragment: fade in / fade out to map and saved Pokemon fragments, slide in / slide out to details fragment, ripple animation when clicking on floating action buttons)

**Optional Nice-to-have Stories**

* [X] User can filter Pokemon by type
* [X] The app periodically searches the Pokemon API in the background to find new Pokemon and add them to the internal database even while the app is not open
* [X] User can enable / disable the background api search in the app
* [ ] Different audio plays when navigating to certain fragments
* [X] The app checks for needed device internet connectivity
* [X] User can pull down to refresh the list of Pokemon gathered from the API
* [X] Sign up and log in can be done with a Facebook account
* [X] Display a default placeholder graphic for each image during loading
* [X] Use a custom app icon and splash screen
* [ ] Implement text localization for automatic translation depending on the user's device-language
* [X] Apply the View Binding library to reduce view boilerplate
* [X] Show progress bar loading icon when loading in key areas throughout the app

### 2. Screen Archetypes

* Login / Sign Up
* Pokemon List
* Pokemon Details
* Type Dialog
* Map
* Saved Pokemon

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Pokemon List
* Pokemon Map
* Saved Pokemon

**Flow Navigation** (Screen to Screen)

* Login
   * Pokemon List
* Pokemon List
  * Pokemon Details
  * Type Dialog
  * Map
  * Saved Pokemon
* Pokemon Details
  * Pokemon List
  * Map
* Type Dialog
  * Pokemon List
* Map
  * Pokemon List
* Saved Pokemon
  * Pokemon List
  * Pokemon Details

## Wireframes
[Add picture of your hand sketched wireframes in this section]

<img src=/loginWireframe.jpeg width=400 />
<img src=/wireframe.jpg width=600 />

## Schema 
### Models

**Custom Pokemon List Item**
| Property      | Type     | Description                                         |
| ------------- | -------- | --------------------------------------------------- |
| id            | Int?     | unique id for Pokemon in the db                     |
| api           | Int      | id for api used to query the api                    |
| image         | String?  | Pokemon image                                       |
| name          | String   | Pokemon name                                        |
| type          | String   | main Pokemon type                                   |
| positionLeft  | Int?     | left position of where a Pokemon is on the map      |
| positionTop   | Int?     | top position of where a Pokemon is on the map       |
| isSaved       | String   | whether or not a Pokemon is saved to a user's party |

**Pokemon Detail Item**
| Property      | Type                  | Description                                         |
| ------------- | --------------------- | --------------------------------------------------- |
| id            | Int                   | unique id for Pokemon in the db                     |
| sprites       | Sprites               | Pokemon sprite                                      |
| name          | String                | Pokemon name                                        |
| timestamp     | String?               | Pokemon access time needed to invalidate cache      |                           
| abilities     | List<PokemonAbility>  | Pokemon abilities                                   |
| stats         | List<PokemonStat>     | Pokemon stats                                       |
| types         | List<PokemonType>     | All Pokemon types of a specific Pokemon             |
   
### Networking
- ```kotlin
  @GET("/api/v2/pokemon/{id}")
  suspend fun getPokemonDetails(@Path("id") id: Int) : Response<PokemonDetailItem>
  ```
- ```kotlin
  callbackManager = CallbackManager.Factory.create()
  binding.loginButton.setReadPermissions(listOf("email", "public_profile"))
  if(AccessToken.getCurrentAccessToken() != null) {
     findNavController().navigate(R.id.action_authFragment_to_listFragment)
  }
  ```
