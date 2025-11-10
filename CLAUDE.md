# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Broad Admin is a full-stack enterprise management system with a Spring Boot backend and Vue 3 frontend. It uses a modular Maven architecture with comprehensive admin features including user management, permissions, scheduling, and system monitoring.

## Development Commands

### Backend (Java/Spring Boot)
```bash
# Build the entire project
mvn clean install

# Run the application (from broad-admin module)
cd broad-admin
mvn spring-boot:run

# Alternative: Run the main class directly
mvn exec:java -Dexec.mainClass="com.broad.BroadApplication"

# Run tests
mvn test

# Package for deployment
mvn clean package
```

### Frontend (Vue 3 + TypeScript)
```bash
# Navigate to frontend directory
cd broad-admin-ui

# Install dependencies (requires pnpm 8+)
pnpm install

# Development server
pnpm dev

# Build for production
pnpm build

# Preview production build
pnpm preview

# Type checking
pnpm tsc --noEmit
```

### Docker Deployment
```bash
# Build and run with Docker Compose
docker-compose up -d

# Build individual image
docker build -t broad-admin .
```

## Architecture Overview

### Multi-Module Structure
- **broad-admin**: Main Spring Boot application module
- **broad-system**: Core business logic (users, roles, permissions, etc.)
- **broad-common**: Shared utilities, constants, and exceptions
- **broad-framework**: Infrastructure layer (security, caching, database config)
- **broad-quartz**: Scheduled task management with Quartz
- **broad-admin-ui**: Vue 3 frontend application

### Technology Stack

**Backend:**
- Spring Boot 3.2.1 with Java 17
- MyBatis Plus 3.5.10.1 ORM with MySQL 8.0+
- Sa-Token 1.39.0 for authentication and authorization
- Redis 6.0+ for caching and session management
- Druid 1.2.20 connection pooling
- SpringDoc 2.3.0 for API documentation

**Frontend:**
- Vue 3.4.25 with Composition API + TypeScript 5.4.0
- Naive UI 2.38.1 component library
- Pinia 2.1.7 for state management
- Vite 5.2.8 for build tooling
- Auto-import configuration for Vue APIs and components

### Key Architectural Patterns

**Controller Layer:**
- All controllers extend `BaseController` for common functionality
- Use `@SaCheckPermission` for role-based authorization
- Standardized response format with `ResultData<T>` and `TableDataInfo<T>`
- Built-in pagination with `startPage()` and `getDataTable()`
- Audit logging with `@Log` annotation

**Service Layer:**
- Business logic separation with `@Service` and `@Transactional`
- MyBatis Plus for database operations with automatic pagination
- Field-level encryption with `@Crypto` annotation
- Automatic timestamp filling with `@FieldFill`

**Frontend Components:**
- Advanced table components with integrated search and pagination
- Auto-import of Vue 3 APIs and Naive UI components
- Role-based UI rendering with `v-auth` directive
- Responsive design with mobile optimization

## Configuration Management

### Environment Profiles
- **Development**: Active profile `dev` with local database/Redis
- **Production**: Uses Dockerized MySQL and Redis services
- Configuration files in `src/main/resources/`

### Key Configuration Files
- `application.yml`: Main Spring Boot configuration
- `application-druid.yml`: Database connection pool settings
- `application-dev.yml`: Development environment settings
- `vite.config.ts`: Frontend build configuration with auto-import setup

### Database Setup
1. Import SQL schemas from `/sql` directory
2. Configure MySQL connection in `application-druid.yml`
3. Ensure Redis is running for caching and sessions

## Development Conventions

### Backend Patterns
- Use Lombok annotations to reduce boilerplate
- Follow naming: `camelCase` for Java, `snake_case` for database fields
- Implement proper exception handling with custom exception classes
- Use JSR-303 validation for request parameters
- Apply `@Transactional` for data consistency

### Frontend Patterns
- Use Composition API with `<script setup>` syntax
- Implement proper TypeScript typing for all components
- Follow auto-import configuration (configured in `vite.config.ts`)
- Use Pinia stores for state management
- Implement responsive design principles

## Security Features

- Sa-Token based stateless authentication with JWT-like tokens
- Role-based permissions with granular access control
- XSS protection with configurable filtering
- SQL injection prevention through parameterized queries
- Field-level encryption for sensitive data
- Rate limiting with Redis-based counters

## API Documentation

- Swagger UI available at `http://localhost:9010/swagger-ui.html`
- OpenAPI 3 specification auto-generated from SpringDoc annotations
- Comprehensive API documentation for all endpoints

## Testing Strategy

- Unit tests for service layer business logic
- Integration tests for database operations
- API testing through Spring Boot Test framework
- Frontend component testing with Vue Test Utils

## Performance Optimization

- Redis caching with Spring Cache abstraction
- Database connection pooling with Druid
- MyBatis Plus query optimization and pagination
- Frontend code splitting and lazy loading
- Vite build optimization for production