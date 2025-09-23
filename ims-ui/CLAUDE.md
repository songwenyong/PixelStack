# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Development Commands

- `npm run dev` - Start development server
- `npm run serve` - Alternative development server command
- `npm run build` - Build for production (outputs to `dist/`)
- `npm run lint` - Run ESLint code linting

## Project Architecture

This is a Vue.js 2 + Vuetify material design dashboard for an image sharing platform called "Pixelstack". Key architectural patterns:

### API Layer Architecture
The project uses a modular API structure in `src/plugins/api/`:
- `base.js` - Environment-specific API base URLs (dev: localhost:8080, prod: localhost/api)
- `user.js` - User authentication, profile management, follow/unfollow functionality
- `image.js` - Image CRUD operations, comments, stars, thumbs, search
- `admin.js` - Admin-specific operations
- `axios.js` - Centralized axios instance with request/response interceptors

### State Management Pattern
Vuex store follows standard modular structure:
- Global state includes `user` (uid, authority) and `token`
- User authority levels: 'root', 'admin', and regular users
- Token-based authentication with automatic logout on 401 responses

### Routing Architecture
- Dynamic routes for user profiles: `/user-profile/:id/type/:type`
- Role-based navigation in drawer component based on user authority
- Mixed Chinese/English route names (Upload: '上传作品', AdminUser: '管理用户')

### Component Structure
- Core layout components: `core-drawer`, `core-toolbar`, `core-view` in main App.vue
- Navigation drawer shows different menu items based on user authority (root/admin vs regular users)
- Material design components from Vuetify with additional Element UI and Vant components

### Key Features
- Image upload and management with tagging system
- User authentication and profile management
- Social features: following, stars, thumbs, comments
- Admin panel for user and comment management
- Search functionality by tags and keywords
- Responsive design with Material Design principles

### Development Notes
- **Upgraded to Vue CLI 5** with Node.js 18.18 compatibility
- **Dependencies Updated**: All major dependencies updated for Node 18 compatibility:
  - Vue 2.7.16 (latest Vue 2.x)
  - Vuetify 1.5.24 (compatible version)
  - Vue CLI 5.0.8
  - Modern Sass compiler (node-sass 9.0.0)
  - Updated ESLint and Babel configurations
- **Build Configuration**: ESLint disabled (`lintOnSave: false`) to avoid legacy code conflicts
- **Babel Configuration**: Updated to use `@vue/cli-plugin-babel/preset`
- SCSS styling with custom Material theme
- Internationalization setup in `src/lang/`
- Environment-based API configuration (easily switch between dev/prod)
- Comprehensive error handling with user-friendly messages
- Token persistence in localStorage with automatic cleanup

### Node.js 18 Compatibility Notes
- All native dependencies (node-sass, babel, webpack) updated to Node 18 compatible versions
- ESLint rules relaxed to accommodate legacy codebase patterns
- Build process generates both modern and legacy bundles for browser compatibility
- Development server runs successfully on http://localhost:8080
- Production build process completes without errors