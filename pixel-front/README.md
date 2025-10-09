# Pixel Front

A modern Vue 3 + TypeScript + Vite application with Naive UI component library.

## Technology Stack

- **Vue 3** - Progressive JavaScript Framework
- **TypeScript** - Typed JavaScript at Any Scale
- **Vite** - Next Generation Frontend Tooling
- **Naive UI** - A Vue 3 Component Library
- **Vue Router** - Official Router for Vue.js
- **Pinia** - Intuitive State Management for Vue
- **Axios** - Promise based HTTP client
- **ESLint** - Pluggable JavaScript linter
- **Prettier** - Opinionated Code Formatter

## Project Structure

```
pixel-front/
├── public/              # Static assets
├── src/
│   ├── api/            # API services
│   ├── assets/         # Assets (images, styles, etc.)
│   ├── components/     # Reusable components
│   ├── layouts/        # Layout components
│   ├── router/         # Vue Router configuration
│   ├── stores/         # Pinia stores
│   ├── types/          # TypeScript type definitions
│   ├── utils/          # Utility functions
│   ├── views/          # Page components
│   ├── App.vue         # Root component
│   ├── main.ts         # Application entry point
│   └── env.d.ts        # TypeScript environment declarations
├── .eslintrc.cjs       # ESLint configuration
├── .prettierrc.json    # Prettier configuration
├── tsconfig.json       # TypeScript configuration
├── vite.config.ts      # Vite configuration
└── package.json        # Project dependencies

```

## Getting Started

### Prerequisites

- Node.js >= 18.x
- npm or yarn or pnpm

### Installation

```bash
# Install dependencies
npm install
```

### Development

```bash
# Start development server
npm run dev
```

The application will be available at `http://localhost:3000`

### Build

```bash
# Build for production
npm run build
```

### Preview

```bash
# Preview production build
npm run preview
```

### Code Quality

```bash
# Run ESLint
npm run lint

# Format code with Prettier
npm run format
```

## Features

- ✅ Vue 3 Composition API
- ✅ TypeScript Support
- ✅ Hot Module Replacement (HMR)
- ✅ State Management with Pinia
- ✅ Vue Router Integration
- ✅ Axios HTTP Client with Interceptors
- ✅ ESLint & Prettier Configuration
- ✅ Naive UI Components
- ✅ Path Aliases (@/)
- ✅ Environment Variables
- ✅ API Proxy Configuration

## Configuration

### API Proxy

The Vite development server is configured to proxy API requests to the backend server. Update `vite.config.ts` to change the proxy settings:

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

### Environment Variables

Environment variables are managed through `.env` files:

- `.env` - Shared environment variables
- `.env.development` - Development environment
- `.env.production` - Production environment

## License

MIT
