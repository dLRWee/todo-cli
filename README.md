# 🚀 Todo CLI Application

A lightweight, robust, and interactive Command Line Interface (CLI) To-Do application built with modern **Java 25**. This project showcases clean code principles, strict domain encapsulation, layered architecture, and an extensive test suite.

---

## ✨ Features
- 📝 **Full Task CRUD:** Create, read, toggle completion status, and delete tasks seamlessly.
- 📊 **Flexible Sorting:** View your task list sorted dynamically by creation date, title, or description.
- 🛡️ **Fail-Safe Input Validation:** Strict business-logic and terminal-input sanitization preventing empty data leaks.
- 📦 **Automated Preview Samples:** Instantly spin up a realistic task board populated with randomized historical data loaded directly from structured CSV resources.
- 📋 **Polished UI:** An elegant, structured text-based interface featuring dynamic tabular layouts inside the terminal window.

---

## 🏗️ Architecture & Tech Stack
The application is strictly decoupled following corporate-grade software design patterns and advanced unit testing practices:
- **Language:** Java 25 (utilizing modern language patterns and type features).
- **Build System:** Apache Maven.
- **Design & Testing Patterns:**
    - *Layered Architecture:* Strict unidirectional data flow (`Main` ➔ `App` ➔ `TaskService` ➔ `TaskRepository` ➔ `Task`).
    - *Builder Pattern:* Nested static builder inside the `Task` class to enforce secure, valid, and atomic object creation.
    - *Data Mapper / Deep Copy:* Defensive copying (`Task::copy`) at the persistence layer boundary to ensure true entity isolation and protect internal state from leaking.
    - *Template Method Pattern (Testing):* Abstract generic test suite (`TaskRepositoryTest<R>`) enforcing a strict behavior contract. This allows seamless plug-and-play testing for future repository implementations (e.g., SQL/JPA) by simply implementing a factory method.
    - *Hierarchical Testing Structure:* Extensive use of JUnit 5 `@Nested` classes paired with `@DisplayName` to group test cases into clean, human-readable operational scopes (`Save`, `FindAll`, `FindById`, etc.).
    - *Mixin Pattern (Testing):* Advanced code reuse in testing via functional interfaces (`TaskTitleValidationTests`, `TaskDescriptionValidationTests`) with `default` methods. This creates modular mixins for reusable `@ParameterizedTest` suites driven by CSV files, allowing any test layer to inherit field validation behaviors by simply providing a `ThrowingCallable` lambda.

---

## 🚀 Getting Started

### Prerequisites
- **JDK 25** or higher installed on your system.
- **Apache Maven** installed.

### Installation & Run
1. Clone this repository to your local machine:
   ```bash
   git clone https://github.com/dLRWee/todo-cli
   cd todo-cli
   ```
2. Build the project and package it into an executable JAR:
   ```bash
   mvn clean package
   ```
3. Run the application:
   ```bash
   java -jar target/todo-cli-1.0-SNAPSHOT.jar
   ```

---

## 🧪 Testing Suite
The codebase is heavily armored against regressions with multiple test suites leveraging premium testing libraries:
- **Testing Engine:** JUnit 5 (`junit-jupiter`).
- **Assertions:** Fluent, readable validation via AssertJ (`assertj-core`).
- **Mocking:** Isolated boundary tests powered by Mockito (`mockito-core`).
- **Resource Testing:** Parameterized testing driven by file-based data sources (`invalid-titles.csv`, `valid-descriptions.csv`, etc.).

Execute the entire test framework via terminal:
```bash
mvn test
```
