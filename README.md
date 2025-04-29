# Water Fountain Rater CLI

🚰 **Water Fountain Rater CLI** is a Kotlin-based Spring Boot command-line application that allows users to locate buildings, explore water stations, rate water fountains, and discover the best hydration spots on campus based on crowd-sourced ratings.

---

## 🧠 Project Structure

```
src/main/kotlin/com/kotlinswe/waterfountainrater
├── WaterfountainraterApplication.kt
├── cli
│ ├── CliConfig.kt # CLI Spring configuration
│ ├── CliRunner.kt # Main CLI entry point
│ ├── command/ # All CLI command implementations
│ ├── formatter/ # Output formatting classes
│ └── util/ # CLI utility classes
├── config
│ └── DataInitializer.kt # Database initialization
├── controller/ # Web controllers
├── dto/ # Data transfer objects
├── model/ # Domain models
├── repository/ # Data repositories
├── service/ # Business logic services
└── util/ # Utility classes
```

---

## 🧾 Key Components

### CLI Components
- **CliRunner**: Main CLI entry point with command routing
- **Command Classes**: Handlers for each command group (Building, Fountain, Report, Review, Station, Stats)
- **Formatters**: Specialized output formatting for each entity type
- **InputParser**: Utility for parsing and validating user input

### Core Components
- **Models**: Building, WaterStation, WaterFountain, WaterFountainReview, WaterFountainReport
- **Repositories**: JPA repositories for all entities with custom queries
- **Services**: Business logic layer for all operations

### Web Components
- **Controllers**: REST endpoints for web interface
- **DTOs**: Data transfer objects for API requests/responses
- **Web Interface**: Accessible at `/terminal` after startup

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
CLI: Interact directly with the command prompt in your terminal

Web: Open http://localhost:8080/admin/terminal in your browser
```bash
🚰 Water Fountain Rater CLI
Type 'help' for commands, 'exit' to quit
> 
```

### 4. **Available Commands**
```bash
BUILDINGS:
  buildings list                     - List all buildings
  buildings near [lat] [lon]        - Find nearby buildings
  buildings info [id]               - Get building details

STATIONS:
  stations list [buildingId]        - List stations in a building
  stations info [id]                - Get station details

FOUNTAINS:
  fountains list [stationId]        - List fountains in a station
  fountains rate [id] [ratings...] - Rate a fountain
  fountains top [limit]             - Show top-rated fountains
  fountains info [id]               - Show fountain details

REPORTS:
  reports create [fountainId] [type] - Create a maintenance report
  reports list [status]             - List reports (all or by status)

REVIEWS:
  reviews list [fountainId]         - List reviews for a fountain
  reviews info [id]                 - Get review details

STATS:
  stats buildings                   - Show building statistics
  stats fountains                   - Show fountain statistics

SYSTEM:
  help                              - Show help message
  exit                              - Exit CLI
```
## Web Endpoints
REST API available at /api/ endpoints (see controller class for URI links)
Interactive web terminal at /admin/terminal
Visit http://localhost:8080/terminal for interactive web interface

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
- Kotlin 1.8+
- Spring Boot 3.1+
- Spring Data JPA
- H2 Database (in-memory)
- Gradle
- Picocli (for CLI)
- Thymeleaf (for web views)

---

## 📦 Future Improvements
- Persist ratings per user with login
- Web interface or mobile integration
- Enhanced station and building metadata (e.g., accessibility info)

---

## 🤝 Contributors
Made with love by Ryan Pitasky and Shankar Choudhury.

---

## 📄 License
MIT License. See `LICENSE` file for details.
