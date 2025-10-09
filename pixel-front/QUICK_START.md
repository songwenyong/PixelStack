# Quick Start Guide

## Project Overview

This is a modern Vue 3 frontend project with full TypeScript support, configured with best practices for development and production.

## What's Included

### Core Technologies
- **Vue 3.4+** with Composition API
- **TypeScript 5.3+** for type safety
- **Vite 5** for fast development and optimized builds
- **Naive UI 2.38+** for beautiful UI components

### State & Routing
- **Pinia** for state management (see `src/stores/`)
- **Vue Router 4** for routing (see `src/router/`)

### API & Utils
- **Axios** with interceptors configured (see `src/api/request.ts`)
- **API services** organized by domain (see `src/api/user.ts`)
- **Utility functions** for common tasks (see `src/utils/`)

### Code Quality
- **ESLint** configured for Vue 3 + TypeScript
- **Prettier** for consistent code formatting
- **TypeScript** strict mode enabled

## Getting Started

### 1. Install Dependencies
```bash
npm install
```

### 2. Start Development Server
```bash
npm run dev
```

The app will be available at `http://localhost:3000`

### 3. Build for Production
```bash
npm run build
```

### 4. Preview Production Build
```bash
npm run preview
```

## Available Scripts

| Command | Description |
|---------|-------------|
| `npm run dev` | Start development server with HMR |
| `npm run build` | Build for production |
| `npm run build:check` | Build with TypeScript type checking |
| `npm run preview` | Preview production build locally |
| `npm run type-check` | Run TypeScript type checking |
| `npm run lint` | Run ESLint and auto-fix issues |
| `npm run format` | Format code with Prettier |

## Project Structure

```
pixel-front/
├── src/
│   ├── api/              # API services and request configuration
│   │   ├── request.ts    # Axios instance with interceptors
│   │   └── user.ts       # User API endpoints
│   ├── assets/           # Static assets (images, styles)
│   │   └── styles/
│   │       └── main.css  # Global styles
│   ├── components/       # Reusable Vue components
│   │   └── HelloWorld.vue
│   ├── layouts/          # Layout components
│   ├── router/           # Vue Router configuration
│   │   └── index.ts      # Route definitions
│   ├── stores/           # Pinia stores
│   │   ├── app.ts        # App-level state (theme, etc.)
│   │   └── user.ts       # User state
│   ├── types/            # TypeScript type definitions
│   │   ├── index.ts      # Type exports
│   │   └── user.ts       # User types
│   ├── utils/            # Utility functions
│   │   ├── format.ts     # Formatting utilities
│   │   └── storage.ts    # Local/session storage helpers
│   ├── views/            # Page components
│   │   ├── Home.vue      # Home page
│   │   ├── About.vue     # About page
│   │   └── NotFound.vue  # 404 page
│   ├── App.vue           # Root component
│   ├── main.ts           # Application entry point
│   └── env.d.ts          # TypeScript environment declarations
├── .env                  # Environment variables (all modes)
├── .env.development      # Development environment variables
├── .env.production       # Production environment variables
├── .eslintrc.cjs         # ESLint configuration
├── .prettierrc.json      # Prettier configuration
├── index.html            # HTML entry point
├── tsconfig.json         # TypeScript configuration
├── vite.config.ts        # Vite configuration
└── package.json          # Project dependencies and scripts
```

## Key Features

### 1. API Configuration
- Axios instance with request/response interceptors
- Automatic token injection from Pinia store
- Error handling for common HTTP status codes
- Base URL configuration via environment variables

Example usage:
```typescript
import request from '@/api/request'

// GET request
const response = await request.get<User>('/user/info')

// POST request
await request.post('/user/login', { email, password })
```

### 2. State Management
Two example stores are provided:

**App Store** (`src/stores/app.ts`):
- Theme management (light/dark mode)
- UI state (sidebar collapse, etc.)

**User Store** (`src/stores/user.ts`):
- User authentication state
- Token management with localStorage
- User info persistence

### 3. Routing
Configured with:
- Hash mode routing
- Navigation guards for page titles
- Lazy-loaded route components
- 404 fallback route

### 4. Environment Variables
Configure API endpoints and other environment-specific values:

```env
VITE_API_BASE_URL=/api
```

Access in code:
```typescript
import.meta.env.VITE_API_BASE_URL
```

### 5. Theme Support
Dark/light theme toggle is built-in:
```vue
<script setup>
import { useAppStore } from '@/stores/app'

const appStore = useAppStore()
appStore.toggleTheme()
</script>
```

## Development Tips

### Path Aliases
Use `@/` to import from `src/`:
```typescript
import { useUserStore } from '@/stores/user'
import request from '@/api/request'
```

### API Proxy
Development server proxies `/api` requests to `http://localhost:8080`. Configure in `vite.config.ts`:
```typescript
server: {
  proxy: {
    '/api': {
      target: 'http://localhost:8080',
      changeOrigin: true,
      rewrite: (path) => path.replace(/^\/api/, '')
    }
  }
}
```

### Type Safety
All Vue components use `<script setup lang="ts">` for full TypeScript support. Define props with types:
```vue
<script setup lang="ts">
interface Props {
  title: string
  count?: number
}

const props = withDefaults(defineProps<Props>(), {
  count: 0
})
</script>
```

### Naive UI Components
All Naive UI components are globally registered. Use them directly:
```vue
<template>
  <n-button type="primary">Click me</n-button>
  <n-card title="My Card">
    <n-text>Content here</n-text>
  </n-card>
</template>
```

## Next Steps

1. **Customize API endpoints**: Edit `src/api/user.ts` or create new API files
2. **Add new routes**: Update `src/router/index.ts`
3. **Create new stores**: Add files to `src/stores/`
4. **Build components**: Add reusable components to `src/components/`
5. **Add pages**: Create new views in `src/views/`
6. **Update environment**: Configure `.env.development` and `.env.production`

## Troubleshooting

### TypeScript Errors
Run type checking separately:
```bash
npm run type-check
```

### Linting Issues
Auto-fix most issues:
```bash
npm run lint
```

### Build Size
The current build uses Naive UI which is large. Consider:
- Using dynamic imports for routes (already configured)
- Implementing lazy loading for heavy components
- Checking the Rollup configuration for manual chunking

## Resources

- [Vue 3 Documentation](https://vuejs.org/)
- [Vite Documentation](https://vitejs.dev/)
- [Naive UI Documentation](https://www.naiveui.com/)
- [Pinia Documentation](https://pinia.vuejs.org/)
- [Vue Router Documentation](https://router.vuejs.org/)
