# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

PixelStack is a full-stack image management system with a Spring Boot backend and Vue 3 frontend. The system provides image storage, album management, categorization, tagging, and social features (favorites/stars).

## Project Structure

```
PixelStack/
├── pixel-backend/          # Spring Boot 2.7.18 backend (Java 1.8)
│   ├── src/main/java/com/pixelstack/ims/
│   │   ├── controller/     # REST API endpoints
│   │   ├── service/        # Business logic layer
│   │   ├── mapper/         # MyBatis Plus data access
│   │   ├── entity/         # Database entity models
│   │   ├── dto/            # Data transfer objects
│   │   ├── config/         # Configuration (CORS, JWT interceptor, Redis, MyBatis)
│   │   ├── util/           # Utilities (JWT, File handling, Result wrapper)
│   │   └── exception/      # Global exception handling
│   ├── src/main/resources/
│   │   ├── application.yml           # Base configuration
│   │   ├── application-dev.yml       # Development profile
│   │   ├── application-prod.yml      # Production profile
│   │   └── mapper/                   # MyBatis XML mappers
│   ├── sql/ims.sql         # Database schema
│   └── pom.xml             # Maven dependencies
├── pixel-front/            # Vue 3 + TypeScript + Vite frontend
│   ├── src/
│   │   ├── api/            # API service modules (album, image, user, etc.)
│   │   ├── stores/         # Pinia state management
│   │   ├── router/         # Vue Router configuration
│   │   ├── views/          # Page components
│   │   ├── components/     # Reusable components
│   │   ├── types/          # TypeScript type definitions
│   │   ├── utils/          # Utility functions
│   │   └── i18n/           # Internationalization (vue-i18n)
│   ├── vite.config.ts      # Vite configuration with proxy
│   └── package.json        # NPM dependencies
└── pom.xml                 # Multi-module Maven parent POM
```

## Build and Development Commands

### Backend (from project root or pixel-backend/)

```bash
# Build backend
mvn clean compile

# Run backend (uses dev profile by default)
mvn spring-boot:run

# Run with specific profile
mvn spring-boot:run -Dspring-boot.run.profiles=prod

# Package JAR (skip tests)
mvn clean package -DskipTests

# Run tests
mvn test

# Run packaged JAR
java -jar pixel-backend/target/pixel-backend-0.0.1-SNAPSHOT.jar
```

Backend runs at: `http://localhost:8080/api`

### Frontend (from pixel-front/)

```bash
# Install dependencies
npm install

# Run development server
npm run dev

# Build for production
npm run build

# Type check without building
npm run type-check

# Build with type checking
npm run build:check

# Lint and fix
npm run lint

# Format code with Prettier
npm run format

# Preview production build
npm run preview
```

Frontend runs at: `http://localhost:3000` (configured in vite.config.ts)

## Architecture Overview

### Backend Architecture

**Layered Architecture**: Controller → Service → Mapper → Database

- **Controllers**: REST endpoints under `/api` context path. Return `Result<T>` wrapper objects.
- **Services**: Business logic. Service interfaces with `impl/` implementations.
- **Mappers**: MyBatis Plus interfaces for database operations. XML mappers in `resources/mapper/`.
- **Entities**: Database models with MyBatis Plus annotations. Use `@TableField(fill = FieldFill.INSERT)` for auto-populated fields.

**Authentication Flow**:
1. Login/Register endpoints are public (excluded in `WebConfig.java`)
2. JWT tokens issued on successful login (valid 7 days)
3. `JwtInterceptor` intercepts all requests (except `/user/login`, `/user/register`, `/files/**`)
4. Token validated and user info extracted to request attributes (`userId`, `username`)
5. Controllers retrieve user context via `@RequestAttribute Long userId`

**File Upload System**:
- MD5-based deduplication (images with same MD5 reuse existing file)
- Storage path: Configured in `application.yml` (`file.upload.path`)
- Access via: `/files/**` mapped to file system in `WebConfig`
- Image metadata stored in `t_image_info` table

### Frontend Architecture

**State Management**: Pinia stores for user, album, image, and category state

**API Layer**:
- Centralized Axios instance in `api/request.ts`
- Request interceptor adds JWT token from user store
- Response interceptor handles errors and 401 redirects
- API modules in `api/` directory organized by feature

**Routing**:
- Vue Router with navigation guards
- Auth check: Redirect to `/login` if not authenticated
- Token stored in localStorage via user store
- Layout wrapper: `AppLayout.vue` for authenticated pages

**Key Frontend Patterns**:
- TypeScript interfaces in `types/` directory
- Naive UI component library for UI elements
- Vue Router lazy loading for code splitting
- Environment-specific config via `.env` files
- Internationalization (i18n) with locale files for Chinese and English

## Configuration Notes

### Backend Configuration

**Profiles**: Set via `spring.profiles.active` in `application.yml`
- `dev`: Development (localhost, debug logging)
- `prod`: Production (adjust database and file paths)

**Key Properties**:
- `server.port`: 8080
- `server.servlet.context-path`: /api
- `jwt.secret`: Signing key for JWT tokens
- `jwt.expiration`: 604800000 (7 days in ms)
- `file.upload.path`: File storage location
- `file.upload.url-prefix`: Public URL prefix for file access

**Database**: MySQL 8.0+, schema in `sql/ims.sql`

**Redis**: Used for caching (port 6379, database 0)

### Frontend Configuration

**Vite Dev Server**:
- Port: 3000 (configured in `vite.config.ts`)
- API proxy: `/api` → `http://localhost:8080` (removes `/api` prefix)

**Environment Variables**:
- `.env`: Shared variables
- `.env.development`: Dev-specific (used by `npm run dev`)
- `.env.production`: Prod-specific (used by `npm run build`)
- Access via `import.meta.env.VITE_*`

## Database Schema

**Core Tables**:
- `t_user_info`: User accounts (userId, username, password, email, avatar)
- `t_image_info`: Image metadata (imageId, userId, title, fileName, filePath, md5, size, createdAt)
- `t_album`: Albums (albumId, userId, name, description, coverImageId, createdAt)
- `t_category`: Hierarchical categories (categoryId, name, parentId)
- `t_tag`: Tags (tagId, name)

**Relation Tables**:
- `t_album_image_relation`: Album ↔ Image many-to-many
- `t_album_tag_relation`: Album ↔ Tag many-to-many
- `t_image_star_relation`: User favorites for images
- `t_album_star_relation`: User favorites for albums

**MyBatis Plus Features**:
- Auto-fill: `createdAt`, `updatedAt` fields
- Logical delete: `isDelete` field (0 = active, 1 = deleted)
- ID generation: Auto-increment primary keys

## Common Development Workflows

### Adding a New API Endpoint

1. Create/update Entity in `entity/` (if new table)
2. Create Mapper interface in `mapper/` (extends `BaseMapper<Entity>`)
3. Create DTO classes in `dto/` for request/response
4. Create/update Service interface and implementation in `service/` and `service/impl/`
5. Create Controller method in `controller/` with `@GetMapping`, `@PostMapping`, etc.
6. Test endpoint with curl or frontend

### Adding a New Frontend Page

1. Create Vue component in `views/` directory
2. Add route to `router/index.ts` (within `AppLayout` children if auth required)
3. Create API service methods in `api/` if needed
4. Create TypeScript types in `types/` if needed
5. Update Pinia store if state management required
6. Add navigation link in `AppLayout.vue` if needed

### Working with JWT Authentication

- **Backend**: Controllers can access userId via `@RequestAttribute Long userId`
- **Frontend**: Token stored in Pinia user store and localStorage
- **401 Handling**: Frontend automatically redirects to login, clears token
- **Testing**: Use `/user/login` to get token, then include `Authorization: Bearer <token>` header

## Testing API Endpoints

```bash
# Register user
curl -X POST http://localhost:8080/api/user/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"123456","email":"test@example.com"}'

# Login
curl -X POST http://localhost:8080/api/user/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"123456"}'

# Get current user (requires token)
curl http://localhost:8080/api/user/current \
  -H "Authorization: Bearer <your-token>"

# Upload image
curl -X POST http://localhost:8080/api/image/upload \
  -H "Authorization: Bearer <your-token>" \
  -F "file=@/path/to/image.jpg" \
  -F "title=My Image"
```

## Important Implementation Details

### Image Upload and Storage

- Images are hashed with MD5 before storage
- Duplicate MD5 → Reuse existing file, create new database record
- File naming: `{timestamp}_{originalFilename}`
- Storage location: `file.upload.path` from config
- Database stores: filename, file path, MD5, size, dimensions

### CORS Configuration

- Configured in `WebConfig.java`
- Allows all origins with credentials (`allowCredentials(true)`)
- Methods: GET, POST, PUT, DELETE, OPTIONS
- For production, restrict `allowedOriginPatterns` to specific domains

### Frontend-Backend Integration

- Vite proxy rewrites `/api/*` → `http://localhost:8080/*` (removes `/api` prefix)
- Backend context-path is `/api`, so actual endpoint: `http://localhost:8080/api/user/login`
- From frontend: `axios.post('/api/user/login')` → proxied to `http://localhost:8080/api/user/login`

### Error Handling

- **Backend**: `GlobalExceptionHandler` catches exceptions, returns `Result` with error code/message
- **Frontend**: Axios response interceptor handles HTTP errors, shows notifications (via Naive UI)
- **Business Errors**: Custom `BusinessException` with error codes

## Troubleshooting

**Backend won't start**:
- Check MySQL is running and database `ims` exists
- Check Redis is running (`redis-cli ping`)
- Verify database credentials in `application.yml`
- Ensure file upload directory exists and is writable

**Frontend API errors**:
- Verify backend is running at `http://localhost:8080`
- Check browser console for CORS errors
- Verify Vite proxy configuration in `vite.config.ts`
- Clear localStorage and retry if token issues persist

**File upload fails**:
- Check `file.upload.path` directory exists and has write permissions
- Verify file size is under 10MB limit (configurable)
- Check disk space availability
