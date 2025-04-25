# Water Fountain Rater CLI

🚰 **Water Fountain Rater CLI** is a Kotlin-based Spring Boot command-line application that allows users to locate buildings, explore water stations, rate water fountains, and discover the best hydration spots on campus based on crowd-sourced ratings.

---

## 🧠 Project Structure

```
src/main/kotlin/com/kotlinswe/waterfountainrater
├── WaterfountainraterApplication.kt
├── cli
│   └── CliRunner.kt
├── config
│   └── DataInitializer.kt
├── model
│   ├── Building.kt
│   ├── WaterFountain.kt
│   └── WaterStation.kt
├── repository
│   ├── BuildingRepository.kt
│   ├── WaterFountainRepository.kt
│   └── WaterStationRepository.kt
├── service
│   ├── RatingService.kt
│   └── SearchService.kt
└── util
    └── DistanceCalculator.kt
```

---

## 🧾 Class Descriptions

### `Building.kt`
Represents a physical building on campus with name, latitude, and longitude. Each building can contain multiple `WaterStation` entries.

### `WaterStation.kt`
A water station located on a specific floor and location in a building. It can contain multiple `WaterFountain` instances.

### `WaterFountain.kt`
Represents a specific water fountain type (UPPER, LOWER, BOTTLE_FILLER) with individual ratings: taste, flow, temperature, ambience, and usability. It calculates an `overallRating` based on these.

### `BuildingRepository.kt`, `WaterStationRepository.kt`, `WaterFountainRepository.kt`
Spring Data JPA repositories for CRUD operations and custom queries such as proximity searches and entity lookups.

### `RatingService.kt`
Handles fountain rating logic. Allows rating updates and retrieval of top-rated fountains.

### `SearchService.kt`
Provides search functionality to find buildings near a geographic location and fetch water stations for a building.

### `DistanceCalculator.kt`
Calculates distance (in meters) between two geographic points using the Haversine formula.

### `DataInitializer.kt`
Populates the database with sample buildings, stations, and fountains when the application is run with an empty dataset.

### `CliRunner.kt`
Main entry point for the command-line interface. Provides interactive features such as:
- Listing buildings, stations, and fountains
- Rating fountains
- Viewing top-rated fountains
- Finding buildings near a given location
- Displaying detailed fountain info

---

## 🚀 How to Run

### 1. **Clone the Project**
```bash
git clone https://github.com/yourusername/water-fountain-rater.git
cd water-fountain-rater
```

### 2. **Build and Run**
```bash
./gradlew bootRun
```

### 3. **Use the CLI**
Once the app launches, you'll see the CLI prompt:
```bash
🚰 Water Fountain Rater CLI
Type 'help' for commands, 'exit' to quit
> 
```

### 4. **Available Commands**
```bash
BUILDINGS:
  list                        - List all buildings
  near [lat] [lon]           - Find nearby buildings

STATIONS:
  stations [buildingId]      - List stations in a building

FOUNTAINS:
  fountains [stationId]      - List fountains in a station
  rate [id] [taste] [flow] [temp] [amb] [usability] - Rate a fountain
  top [limit]                - Show top-rated fountains (default: 5)
  details [id]               - Show detailed info for a fountain

SYSTEM:
  help                       - Show help message
  exit                       - Exit CLI
```

---

## 🧪 Example Usage
```bash
> list
> near 34.05 -118.24
> stations 1
> fountains 2
> rate 3 4.5 4.0 3.8 4.2 4.6
> top 5
> details 3
```

---

## 🛠 Tech Stack
- Kotlin
- Spring Boot
- Spring Data JPA
- H2 In-Memory Database

---

## 📦 Future Improvements
- Persist ratings per user with login
- Web interface or mobile integration
- Enhanced station and building metadata (e.g., accessibility info)

---

## 🤝 Contributors
Made with ❤️ by KotlinSWE team.

---

## 📄 License
MIT License. See `LICENSE` file for details.
