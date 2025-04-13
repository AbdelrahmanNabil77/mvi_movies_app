# Movie Explorer 🎬

A modern Android app for discovering movies, managing favorites, and exploring detailed movie information using the TMDB API. Built with best practices for maintainability and future scalability.

## Features ✨

- **Discover Movies**  
  Browse popular movies with infinite pagination
- **Favorite Management**  
  Mark/unmark movies as favorites with persistent storage
- **Movie Details**  
  View comprehensive movie information including:
  - Title, overview, and release date
  - User ratings and popularity scores
  - High-quality movie posters
- **Offline Support**  
  Access recently viewed content without internet connection
- **Modern Architecture**  
  Implemented using MVI pattern for predictable state management
- **Smooth Navigation**  
  Intuitive transitions between screens with Android Navigation Component

## Tech Stack 🛠️

- **MVI Architecture**  
  Model-View-Intent pattern for clean separation of concerns
- **Dependency Injection**  
  Dagger Hilt for scalable and maintainable dependency management
- **Network Layer**  
  - Retrofit for REST API communication
  - OkHttp with logging interceptor
- **Persistence**  
  - Room Database for local storage
  - Paging 3 for efficient data loading
- **Image Loading**  
  Glide for smooth image handling with caching
- **UI Components**  
  - Material Design components
  - View Binding for type-safe view access
- **Navigation**  
  Android Navigation Component for fragment management

## Architecture 🏛️

## Installation ⚙️

1. Get TMDB API key from [TMDB Website](https://www.themoviedb.org/)
2. Create `local.properties` in root directory
3. Add your API key:
   ```properties
   TMDB_API_KEY="your_api_key_here"
