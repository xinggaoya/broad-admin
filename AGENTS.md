# Repository Guidelines

## Project Structure & Module Organization
This is a multi-module Maven monorepo (`pom.xml` at the root) plus a Vue 3 admin UI. `broad-admin` hosts REST entry points (`com.broad.web.controller`) and wires the platform modules. `broad-system` contains entities, mappers, and domain services, while `broad-common` collects shared helpers, constants, and cross-cutting tests. `broad-framework` carries security, exception, and config glue, and `broad-quartz` wraps scheduled jobs. The Vue client lives in `broad-admin-ui` (Vite-based; see `src/components`, `src/views`, and `src/router`). SQL migrations and seed data stay in `sql/`, and `docker-compose.yml` boots the packaged app for smoke testing.

## Build, Test, and Development Commands
- `mvn -T 1C clean verify`: builds every backend module with Java 17 and runs unit/integration tests. Use this before opening a PR.
- `mvn -pl broad-admin -am spring-boot:run -Dspring.profiles.active=dev`: spins up the API with dependent modules; exposes port 9010 and reloads config on code changes.
- `pnpm install --filter broad-admin-master`: installs the UI dependencies (pnpm is required because of the `pnpm-lock.yaml`).
- `pnpm dev --dir broad-admin-ui`: starts the Vite dev server with HMR; pair it with the dev backend.
- `pnpm build --dir broad-admin-ui && pnpm preview --dir broad-admin-ui`: produce the production bundle and exercise it via the Vite preview server.

## Coding Style & Naming Conventions
Backend code sticks to 4-space indentation, `package com.broad.<module>` namespaces, Lombok for boilerplate, and Conventional Spring patterns: controllers as `Sys*Controller` under `com.broad.web.controller`, services as `*Service` (interfaces) plus `*ServiceImpl`, and MyBatis mappers under `mapper`. Configuration, security, and AOP concerns belong in `broad-framework`; avoid duplicating config inside feature modules. Frontend code is strict TypeScript + `<script setup>` single-file components; keep components in PascalCase, route folders in kebab-case, and colocate feature-specific styles under `src/views/<feature>/index.scss`. Run Prettier/ESLint (configured via `@vue/eslint-config-*`) before committing.

## Testing Guidelines
Spring testing support comes from `spring-boot-starter-test` (see `broad-common/pom.xml`). Add `src/test/java` suites next to the module you touch, preferring `@SpringBootTest` or slice tests (`@WebMvcTest`, `@DataJpaTest`) plus MockMvc assertions for controllers. For scheduling logic (`broad-quartz`), cover cron parsing and locking helpers with plain JUnit tests. UI work currently lacks automated tests; when changing UI flows, provide reproducible steps and, where possible, Cypress or Playwright specs under `broad-admin-ui/tests` (create the folder if necessary). Never skip tests in CI; if a test must be disabled, reference the tracking issue in the `@Disabled` reason.

## Commit & Pull Request Guidelines
Follow the Conventional Commit style seen in history (`feat(permission): ...`, `build(broad-admin-ui): ...`). Scope names should match module folders or feature areas. Commits must be focused and reference issues (e.g., `feat(system): add todo export (#123)`). PRs need: goal-oriented description, testing notes (`mvn clean verify`, `pnpm dev` smoke), screenshots/GIFs for UI tweaks, and callouts for schema changes (`sql/`). Keep PRs small; split backend/UI work when possible.

## Environment & Security Notes
Secrets live outside the repo; use `application-*.yml` overrides in your workspace rather than committing them. Use `sql/` dumps to hydrate local databases, and run `docker-compose up -d` only after mounting a writable volume for logs (`/dev/broad-admin`). When working on auth/security layers, coordinate with maintainers before changing `broad-framework/security` to avoid regressions.
