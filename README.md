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
- **Scope:** A functioning app will be manageable to complete. Challenges may arise when attempting to implement the planned periodic background searches to the API while the app is not open and 3rd party sign on. If all stories and other stretch goals are met, the most difficult final story would be to implement the Augmented Reality stretch goal, strongly increasing the scope of the project. Utilizing new topics, technologies and processes like Kotlin, MVVM, dependency injection, and coroutines will also increase the complexity and difficulty in reaching the app plans.

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* [X] User can scroll through a list of Pokemon from the Pokemon API
* [X] Users can search for a specific Pokemon by name
* [X] User can click on a Pokemon in the list view to see more details
  * [X] Statistics, class rating (calculated by getting the average of all statistics), abilities, type, and location
* [X] User can access a game-based map from the main view with Pokemon plotted onto certain, different parts of the map 
* [X] User can sign up with a new profile
* [X] User can log in / log out of the app
* [X] User can save / delete Pokemon to and from their party with the information stored
* [X] The app has multiple views
* [ ] The app uses at least one gesture (e.g. double tap to like, e.g. pinch to scale) 
* [ ] The app incorporates at least one external library to add visual polish
* [ ] The app uses at least one animation (e.g. fade in / out, e.g. animating a view growing and shrinking)

**Optional Nice-to-have Stories**

* [X] User can filter Pokemon by type
* [X] The app periodically searches the Pokemon API in the background to find new Pokemon and add them to the internal database even while the app is not open
* [X] User can enable / disable the background api search in the app
* [ ] Different audio plays when navigating to certain fragments
* [X] The app checks for needed device internet connectivity
* [X] User can pull down to refresh the list of Pokemon gathered from the API
* [X] Sign up and log in can be done with a Facebook account
* [X] Display a default placeholder graphic for each image during loading
* [X] Apply the View Binding library to reduce view boilerplate
* [X] Show progress bar loading icon when loading in key areas throughout the app

### 2. Screen Archetypes

* [list first screen here]
   * [list associated required story here]
   * ...
* [list second screen here]
   * [list associated required story here]
   * ...

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* [fill out your first tab]
* [fill out your second tab]
* [fill out your third tab]

**Flow Navigation** (Screen to Screen)

* [list first screen here]
   * [list screen navigation here]
   * ...
* [list second screen here]
   * [list screen navigation here]
   * ...

## Wireframes
[Add picture of your hand sketched wireframes in this section]

<img src=/loginWireframe.jpeg width=400 />
<img src=/wireframe.jpg width=600 />

## Schema 
### Models
[Still being worked on; will update throughout the development process]
   
### Networking
- [Add list of network requests by screen ]
- [Create basic snippets for each Parse network request]
- [OPTIONAL: List endpoints if using existing API such as Yelp]
