# Project Summary

## Overview
This project is a microservices-based application consisting of two main services: a Catalog Service and a Payment Service. The application is built using Java and utilizes the Spring framework, specifically Spring Boot, for creating RESTful web services. The project is designed to manage a catalog of courses and handle payment processing for users.

### Languages, Frameworks, and Main Libraries Used
- **Languages**: Java
- **Frameworks**: Spring Boot
- **Main Libraries**: 
  - Spring Web
  - Spring Data MongoDB (implied by the presence of MongoDB-related classes)
  - Swagger (for API documentation)

## Purpose of the Project
The primary purpose of the project is to provide a service-oriented architecture for managing course catalogs and processing payments. The Catalog Service manages the course data, while the Payment Service handles payment transactions, allowing users to enroll in courses seamlessly.

## Build and Configuration Files
The following files are relevant for the configuration and building of the project:

1. `/catalog-service/Dockerfile`
2. `/catalog-service/pom.xml`
3. `/payment-service/Dockerfile`
4. `/payment-service/pom.xml`
5. `/docker-compose.yml`
6. `/mvnw`
7. `/mvnw.cmd`

## Source Files Location
Source files can be found in the following directories:

- Catalog Service Source Files: 
  - `/catalog-service/src/main/java/com/skillbox`
- Payment Service Source Files:
  - `/payment-service/src/main/java/com/skillbox`

## Documentation Files Location
Documentation files are located in the following directory:

- `/README.md` 

This file typically contains information about the project, setup instructions, and usage guidelines.