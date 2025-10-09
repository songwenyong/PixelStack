# PixelStack Frontend Development Guide

## Project Overview

This is the frontend application for PixelStack, an image management system built with Vue 3, TypeScript, and Naive UI.

## Tech Stack

- **Vue 3.4** - Progressive JavaScript framework with Composition API
- **TypeScript 5.3** - Type-safe JavaScript
- **Vite 5.0** - Fast build tool and dev server
- **Naive UI 2.38** - Vue 3 UI component library
- **Vue Router 4.2** - Official routing library
- **Pinia 2.1** - State management
- **Axios 1.6** - HTTP client
- **@vicons/ionicons5** - Icon library

## Project Structure

```
pixel-front/
├── src/
│   ├── api/                    # API service layer
│   │   ├── request.ts          # Axios instance and interceptors
│   │   ├── user.ts             # User API
│   │   ├── image.ts            # Image API
│   │   ├── album.ts            # Album API
│   │   ├── category.ts         # Category API
│   │   ├── file.ts             # File upload API
│   │   └── index.ts            # API exports
│   ├── assets/                 # Static assets
│   ├── components/             # Reusable components
│   │   └── AppLayout.vue       # Main layout with sidebar
│   ├── router/                 # Routing configuration
│   │   └── index.ts            # Router setup and guards
│   ├── stores/                 # Pinia stores
│   │   ├── app.ts              # App-level state
│   │   ├── user.ts             # User authentication state
│   │   ├── image.ts            # Image management state
│   │   ├── album.ts            # Album management state
│   │   └── category.ts         # Category management state
│   ├── types/                  # TypeScript type definitions
│   │   ├── common.ts           # Common types
│   │   ├── user.ts             # User types
│   │   ├── image.ts            # Image types
│   │   ├── album.ts            # Album types
│   │   └── category.ts         # Category types
│   ├── utils/                  # Utility functions
│   │   ├── storage.ts          # Local storage utilities
│   │   └── format.ts           # Formatting utilities
│   ├── views/                  # Page components
│   │   ├── Login.vue           # Login page
│   │   ├── Register.vue        # Registration page
│   │   ├── Home.vue            # Dashboard/home page
│   │   ├── Images.vue          # Image gallery
│   │   ├── Albums.vue          # Album list
│   │   └── ...                 # Other views
│   ├── App.vue                 # Root component
│   ├── main.ts                 # Application entry point
│   └── env.d.ts                # Environment type declarations
├── .env                        # Environment variables
├── .env.development            # Development environment
├── .env.production             # Production environment
├── index.html                  # HTML template
├── tsconfig.json               # TypeScript configuration
├── vite.config.ts              # Vite configuration
└── package.json                # Dependencies and scripts
```

## API Integration

### API Base URL

The API base URL is configured via environment variables:

```env
# .env.development
VITE_API_BASE_URL=http://localhost:8080/api
```

### API Request Flow

1. **Request Interceptor** (src/api/request.ts:19-30)
   - Adds JWT token to Authorization header
   - Called before every request

2. **Response Interceptor** (src/api/request.ts:33-69)
   - Handles response data format
   - Handles HTTP errors (401, 403, 404, 500)
   - Auto-redirects to login on 401

### API Services

Each API module corresponds to a backend controller:

- **userApi** - User authentication and profile
- **imageApi** - Image upload, list, star, delete
- **albumApi** - Album CRUD and image management
- **categoryApi** - Category tree management
- **fileApi** - File upload utilities

### Example API Usage

```typescript
import { userApi, imageApi } from '@/api'

// Login
const response = await userApi.login({
  username: 'user',
  password: 'password'
})

// Upload image
const file = event.target.files[0]
const result = await imageApi.uploadImage(file, 'My Image')

// Get images with pagination
const images = await imageApi.getImagePage({
  current: 1,
  size: 20,
  keyword: 'search term'
})
```

## State Management

### User Store

Manages authentication and user information:

```typescript
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

// Login
await userStore.login({ username, password })

// Get user info
const user = userStore.userInfo
const isLoggedIn = userStore.isLoggedIn

// Logout
userStore.logout()
```

### Image Store

Manages image operations:

```typescript
import { useImageStore } from '@/stores/image'

const imageStore = useImageStore()

// Fetch images
await imageStore.fetchImages({ keyword: 'search' })

// Upload image
await imageStore.uploadImage(file, title)

// Star/unstar
await imageStore.starImage(imageId)
await imageStore.unstarImage(imageId)

// Delete
await imageStore.deleteImage(imageId)
```

### Album Store

Manages album operations:

```typescript
import { useAlbumStore } from '@/stores/album'

const albumStore = useAlbumStore()

// Create album
await albumStore.createAlbum({
  albumName: 'My Album',
  description: 'Description'
})

// Add images to album
await albumStore.addImagesToAlbum(albumId, [imageId1, imageId2])

// Get album detail
await albumStore.fetchAlbumDetail(albumId)
```

## Routing

### Route Structure

- `/login` - Login page (public)
- `/register` - Registration page (public)
- `/` - Main layout (requires authentication)
  - `/` - Home/dashboard
  - `/images` - Image gallery
  - `/albums` - Album list
  - `/albums/:id` - Album detail
  - `/categories` - Category management
  - `/favorites/images` - Starred images
  - `/favorites/albums` - Starred albums
  - `/profile` - User profile

### Navigation Guards

Authentication is enforced via router guards (src/router/index.ts:114-133):

```typescript
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth !== false)

  if (requiresAuth && !userStore.isLoggedIn) {
    // Redirect to login
    next({ path: '/login', query: { redirect: to.fullPath } })
  } else if ((to.path === '/login' || to.path === '/register') && userStore.isLoggedIn) {
    // Redirect to home if already logged in
    next('/')
  } else {
    next()
  }
})
```

## Components

### AppLayout

Main application layout with:
- Collapsible sidebar navigation
- Header with user menu
- Upload modal
- Route outlet for page content

Location: `src/components/AppLayout.vue`

### Page Components

All views are in `src/views/`:
- **Login.vue** - User login form
- **Register.vue** - User registration form
- **Home.vue** - Dashboard with statistics and recent images
- **Images.vue** - Image gallery with grid layout
- **Albums.vue** - Album list with cards
- **AlbumDetail.vue** - Album detail with images (to be created)
- More views can be added as needed

## Development Workflow

### Adding a New Feature

1. **Define Types** (src/types/)
   ```typescript
   export interface NewFeature {
     id: number
     name: string
   }
   ```

2. **Create API Service** (src/api/)
   ```typescript
   export const newFeatureApi = {
     getList() {
       return request.get<NewFeature[]>('/new-feature')
     }
   }
   ```

3. **Create Store** (src/stores/)
   ```typescript
   export const useNewFeatureStore = defineStore('newFeature', () => {
     const items = ref<NewFeature[]>([])

     const fetchItems = async () => {
       const res = await newFeatureApi.getList()
       items.value = res.data
     }

     return { items, fetchItems }
   })
   ```

4. **Create View** (src/views/)
   ```vue
   <template>
     <div>
       <n-list :data="store.items" />
     </div>
   </template>

   <script setup lang="ts">
   import { onMounted } from 'vue'
   import { useNewFeatureStore } from '@/stores/newFeature'

   const store = useNewFeatureStore()

   onMounted(() => {
     store.fetchItems()
   })
   </script>
   ```

5. **Add Route** (src/router/index.ts)
   ```typescript
   {
     path: 'new-feature',
     name: 'NewFeature',
     component: () => import('@/views/NewFeature.vue')
   }
   ```

## Best Practices

### TypeScript

- Always define interfaces for API responses
- Use type inference where possible
- Avoid `any` type
- Use strict type checking

### Vue Composition API

- Use `<script setup>` syntax
- Group related reactive state
- Extract reusable logic into composables
- Use `ref()` for primitives, `reactive()` for objects

### Error Handling

```typescript
try {
  await someAsyncOperation()
  message.success('Success!')
} catch (error: any) {
  message.error(error.message || 'Operation failed')
  console.error(error)
}
```

### Loading States

```typescript
const loading = ref(false)

const fetchData = async () => {
  loading.value = true
  try {
    // Fetch data
  } finally {
    loading.value = false
  }
}
```

## Styling

### Naive UI Theming

Colors and spacing use Naive UI design tokens:

```vue
<style scoped>
.container {
  color: var(--n-text-color);
  background: var(--n-color-embedded);
}
</style>
```

### Responsive Design

Use Naive UI's responsive grid:

```vue
<n-grid :cols="responsiveCols">
  <!-- content -->
</n-grid>

<script setup>
const responsiveCols = computed(() => {
  return 'xs:1 s:2 m:3 l:4 xl:5 2xl:6'
})
</script>
```

## Testing

### Unit Tests

```bash
npm run test
```

### Type Checking

```bash
npm run type-check
```

### Linting

```bash
npm run lint
```

## Building for Production

```bash
npm run build
```

Output will be in the `dist/` directory.

## Environment Variables

- `VITE_API_BASE_URL` - Backend API base URL
- Add more as needed

## Troubleshooting

### API Connection Issues

- Verify backend is running on port 8080
- Check `VITE_API_BASE_URL` in `.env.development`
- Check browser console for CORS errors

### Authentication Issues

- Clear localStorage: `localStorage.clear()`
- Check token in Application tab of DevTools
- Verify token format in request headers

### Build Errors

- Delete `node_modules` and reinstall: `rm -rf node_modules && npm install`
- Clear Vite cache: `rm -rf node_modules/.vite`
- Check TypeScript errors: `npm run type-check`

## Contributing

1. Follow existing code patterns
2. Write TypeScript types for all new features
3. Use Composition API and `<script setup>`
4. Test your changes
5. Run linter before committing

## Additional Resources

- [Vue 3 Documentation](https://vuejs.org/)
- [Naive UI Components](https://www.naiveui.com/)
- [Vite Guide](https://vitejs.dev/)
- [Pinia Documentation](https://pinia.vuejs.org/)
