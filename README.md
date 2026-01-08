# Apartment Brokerage & Management System

## Project Overview
This system is a robust Java-based solution for real estate brokerage. It manages complex property hierarchies and multi-role user interactions (Seller, Buyer, Broker) while maintaining strict architectural standards.

## OOP & Architectural Implementation
The project serves as a showcase for advanced Object-Oriented Programming and software design principles:

### 1. SOLID Principles
* **Single Responsibility (SRP):** Complete decoupling between data models (`Apartment`), business logic (`ApartmentService`), and data persistence (`ApartmentCSVHandler`).
* **Open/Closed (OCP):** Use of the **Decorator Pattern** allows for adding new apartment services (Legal, Cleaning, etc.) without modifying existing classes.
* **Liskov Substitution & Interface Segregation:** Implementation of a strict `User` hierarchy and focused interfaces for search and notification logic.

### 2. Design Patterns
* **Composite Pattern:** Handles hierarchical property structures. It allows the system to treat a single apartment and a complex building (containing sub-units) uniformly.
* **Decorator Pattern:** Dynamically attaches additional responsibilities and costs (Moving, Interior Design) to an apartment object.
* **Strategy Pattern:** Encapsulates various searching and pricing algorithms, making them interchangeable at runtime.
* **Observer Pattern:** Implements a push-notification system where Brokers are automatically notified of inventory changes (e.g., property removal).
* **Factory Method:** Centralizes object creation logic for various apartment types and user roles.

## 🚀 Getting Started

### Prerequisites
* Java JDK 17 or higher.
* `apartments.csv` and `users.csv` files located in the project root.

### Installation & Execution
1. Clone the repository to your local machine.
2. Compile the source files:
   ```bash
   javac *.java
