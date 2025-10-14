import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'
import { useUserStore } from '@/stores/user'
import AppLayout from '@/components/AppLayout.vue'

const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: {
      title: 'Login - PixelStack',
      requiresAuth: false
    }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue'),
    meta: {
      title: 'Register - PixelStack',
      requiresAuth: false
    }
  },
  {
    path: '/',
    component: AppLayout,
    meta: {
      requiresAuth: true
    },
    children: [
      {
        path: '',
        name: 'Home',
        component: () => import('@/views/Home.vue'),
        meta: {
          title: 'Home - PixelStack'
        }
      },
      {
        path: 'images',
        name: 'Images',
        component: () => import('@/views/Images.vue'),
        meta: {
          title: 'Images - PixelStack'
        }
      },
      {
        path: 'albums',
        name: 'Albums',
        component: () => import('@/views/Albums.vue'),
        meta: {
          title: 'Albums - PixelStack'
        }
      },
      {
        path: 'categories',
        name: 'Categories',
        component: () => import('@/views/Categories.vue'),
        meta: {
          title: 'Categories - PixelStack'
        }
      },
      {
        path: 'starred/images',
        name: 'StarredImages',
        component: () => import('@/views/StarredImages.vue'),
        meta: {
          title: 'Starred Images - PixelStack'
        }
      },
      {
        path: 'starred/albums',
        name: 'StarredAlbums',
        component: () => import('@/views/StarredAlbums.vue'),
        meta: {
          title: 'Starred Albums - PixelStack'
        }
      },
      {
        path: 'albums/:id',
        name: 'AlbumDetail',
        component: () => import('@/views/AlbumDetail.vue'),
        meta: {
          title: 'Album Detail - PixelStack'
        }
      }
      // {
      //   path: 'profile',
      //   name: 'Profile',
      //   component: () => import('@/views/Profile.vue'),
      //   meta: {
      //     title: 'Profile - PixelStack'
      //   }
      // }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/NotFound.vue'),
    meta: {
      title: '404 Not Found - PixelStack'
    }
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

// Navigation guard
router.beforeEach((to, from, next) => {
  // Set page title
  document.title = (to.meta.title as string) || 'PixelStack'

  const userStore = useUserStore()
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth !== false)

  if (requiresAuth && !userStore.isLoggedIn) {
    // Redirect to login if not authenticated
    next({
      path: '/login',
      query: { redirect: to.fullPath }
    })
  } else if ((to.path === '/login' || to.path === '/register') && userStore.isLoggedIn) {
    // Redirect to home if already logged in
    next('/')
  } else {
    next()
  }
})

export default router
