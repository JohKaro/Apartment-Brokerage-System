# Apartment Brokerage & Management System

## 🏢 Project Overview
[cite_start]This Java-based system provides a comprehensive platform for real estate brokerage, managing apartment listings, user roles, and property transactions[cite: 71]. [cite_start]The application features a robust role-based access control system (RBAC) with tailored menus for **Sellers**, **Buyers**, and **Brokers**[cite: 74, 75].

## 🏗️ Architectural Excellence (SOLID Principles)
[cite_start]The project is built on a foundation of clean code and architectural best practices[cite: 92]:
* [cite_start]**Single Responsibility (SRP):** Distinct classes handle data models (`Apartment`), user logic (`Broker`, `Buyer`), and data persistence (`ApartmentCSVHandler`)[cite: 94, 95, 96, 97].
* [cite_start]**Open/Closed (OCP):** Additional services (e.g., Legal, Cleaning) are implemented using the **Decorator Pattern**, allowing system expansion without modifying existing code[cite: 100].
* [cite_start]**Liskov Substitution (LSP):** A unified `User` base class ensures consistent handling of all role-specific logic[cite: 102].
* [cite_start]**Interface Segregation (ISP):** Clear, focused interfaces ensure modularity and maintainability[cite: 104, 105].
* [cite_start]**Dependency Inversion (DIP):** Decoupled permissions ensure that high-level brokerage logic remains independent of low-level data structures[cite: 106, 107].

## 🧩 Design Patterns Implemented
[cite_start]This project showcases a high level of software design proficiency[cite: 108]:
* [cite_start]**Factory Method:** Standardizes the creation of complex apartment objects[cite: 109, 110].
* [cite_start]**Strategy Pattern:** Provides flexible apartment searching logic that can be swapped at runtime[cite: 111, 112].
* [cite_start]**Observer Pattern:** Facilitates real-time notifications, such as alerting brokers when a property is removed by a seller[cite: 81, 113].
* [cite_start]**Composite Pattern:** Enables a hierarchical property structure, allowing the system to treat individual units and multi-unit buildings uniformly[cite: 114, 115].
* [cite_start]**Decorator Pattern:** Dynamically adds value-added services (Moving, Legal Review, Interior Design) to property purchases[cite: 118].

## 🚀 Key Features
* [cite_start]**Role-Based Menus:** Intuitive command-line interface for different user types[cite: 72, 75].
* [cite_start]**Hierarchical Property Management:** Add sub-units to existing listings via an address-based hierarchy[cite: 89, 117].
* [cite_start]**Advanced Searching:** Search by radius and specific criteria like price per square meter[cite: 84].
* [cite_start]**Data Persistence:** Automatic synchronization with `apartments.csv` for every update[cite: 119].

## 💻 Technologies Used
* [cite_start]**Language:** Java (OOP) [cite: 69]
* [cite_start]**Data Storage:** CSV (File-based database) [cite: 119]
* **Testing:** Comprehensive test suite in `ApartmentTest.java`

## 🛠️ How to Run
1. Ensure Java JDK is installed.
2. [cite_start]Run `Main.java` to launch the application[cite: 73].
3. [cite_start]Select your user role (1 for Seller, 2 for Buyer, 3 for Broker) and follow the intuitive menu prompts[cite: 74, 120].
