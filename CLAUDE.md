# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Broad Admin is a full-stack enterprise management system (version 2.2.0) with a Spring Boot backend and Vue 3 frontend. It uses a modular Maven architecture with comprehensive admin features including user management, permissions, scheduling, and system monitoring.

## Development Commands

### Backend (Java/Spring Boot)
```bash
# Build the entire project
mvn clean install

# Run the application (from project root - uses main class in broad-admin module)
mvn spring-boot:run

# Alternative: Run the main class directly
mvn exec:java -Dexec.mainClass="com.broad.web.BroadApplication"

# Run tests
mvn test

# Package for deployment
mvn clean package
```

### Frontend (Vue 3 + TypeScript)
```bash
# Navigate to frontend directory
cd broad-admin-ui

# Install dependencies (requires pnpm)
pnpm install

# Development server (runs on http://localhost:5173)
pnpm dev

# Build for production
pnpm build

# Preview production build
pnpm preview

# E2E Testing
pnpm test:e2e
pnpm test:e2e:ui
```

### Docker Deployment
```bash
# Build and run with Docker Compose
docker-compose up -d

# Build individual image
docker build -t broad-admin .
```

## Architecture Overview

### Multi-Module Maven Structure
- **broad-admin**: Main Spring Boot application module containing `BroadApplication.java`
- **broad-system**: Core business logic (users, roles, permissions, etc.)
- **broad-common**: Shared utilities, constants, and exceptions
- **broad-framework**: Infrastructure layer (security, caching, database config)
- **broad-quartz**: Scheduled task management with Quartz
- **broad-admin-ui**: Vue 3 frontend application

### Technology Stack

**Backend (Updated):**
- Spring Boot 3.5.3 with Java 17
- MyBatis Plus 3.5.9 ORM with MySQL 8.0+
- Sa-Token 1.44.0 for authentication and authorization
- Redis 6.0+ for caching and session management
- Druid 1.2.23 connection pooling
- SpringDoc 2.3.0 for API documentation
- FastJson2 2.0.45 for JSON processing
- Hutool 5.8.25 utility library

**Frontend (Updated):**
- Vue 3.5.0 with Composition API + TypeScript 5.9.2
- Naive UI 2.40.0 component library
- Pinia 2.1.7 for state management
- Vite 6.4.1 for build tooling
- Auto-import configuration for Vue APIs and Naive UI components
- ECharts 5.5.0 for data visualization

### Key Architectural Patterns

**Controller Layer:**
- All controllers extend `BaseController` for common functionality
- Use `@SaCheckPermission` for role-based authorization with format `module:function:action`
- Standardized response format with `ResultData<T>` and `TableDataInfo<T>`
- Built-in pagination with `startPage()` and `getDataTable()`
- Audit logging with `@Log` annotation

**Service Layer:**
- Business logic separation with `@Service` and `@Transactional`
- MyBatis Plus for database operations with automatic pagination
- Field-level encryption with `@Crypto` annotation
- Automatic timestamp filling with `@FieldFill`
- Service classes typically extend `ServiceImpl<I, T>`

**Frontend Components:**
- Auto-import setup in `vite.config.ts` for Vue 3 APIs and Naive UI components
- Advanced table components with integrated search and pagination functionality
- Role-based UI rendering with `v-auth` directive
- Composition API with `<script setup>` syntax
- Path alias `@/` points to `src/` directory

## Configuration Management

### Application Configuration
- **Server**: Runs on port 9010
- **Database**: MySQL 8.0+ with connection pooling via Druid
- **Cache**: Redis 6.0+ for sessions and data caching
- **Authentication**: Sa-Token with 30-day token expiration

### Key Configuration Files
- `pom.xml`: Maven parent configuration with version 2.2.0
- `broad-admin/src/main/resources/application.yml`: Main Spring Boot configuration
- `broad-admin/src/main/resources/application-druid.yml`: Database connection pool settings
- `broad-admin/src/main/resources/application-dev.yml`: Development environment settings
- `broad-admin-ui/vite.config.ts`: Frontend build configuration with auto-import and proxy setup
- `broad-admin-ui/package.json`: Frontend dependencies with E2E testing via Playwright

### Database Setup
1. Import SQL schemas from `/sql` directory
2. Configure MySQL connection in `application-druid.yml` (dev.moncn.cn for development)
3. Ensure Redis is running for caching and sessions

## Development Conventions

### Backend Patterns
- Component scanning: `@ComponentScan(basePackages = "com.broad.**")`
- Mapper scanning: `@MapperScan(basePackages = "com.broad.*.mapper")`
- Use Lombok annotations to reduce boilerplate
- Follow naming: `camelCase` for Java, `snake_case` for database fields
- Implement proper exception handling with custom exception classes
- Use JSR-303 validation for request parameters

### Frontend Patterns
- Auto-import resolvers for Naive UI components in `vite.config.ts`
- SCSS preprocessing with global variables import
- SVG icon system via `vite-plugin-svg-icons`
- TypeScript strict mode enabled
- Path aliases: `@/` for `src/` directory

## Security Features

- Sa-Token based stateless authentication with JWT-like tokens
- Role-based permissions with granular access control (`system:user:list`, etc.)
- XSS protection with configurable filtering
- SQL injection prevention through parameterized queries
- Field-level encryption for sensitive data using `@Crypto`
- Rate limiting with Redis-based counters

## API Documentation

- Swagger UI available at `http://localhost:9010/swagger-ui.html`
- OpenAPI 3 specification auto-generated from SpringDoc annotations
- Development proxy configured in Vite: `/api` -> `localhost:9010`

## Performance Optimization

- Redis caching with Spring Cache abstraction
- Database connection pooling with Druid monitoring
- MyBatis Plus query optimization and automatic pagination
- Frontend code splitting and lazy loading
- Vite build optimization with modern compiler