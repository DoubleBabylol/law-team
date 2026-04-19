import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/LoginView.vue'),
    meta: { public: true }
  },
  {
    path: '/',
    name: 'MainLayout',
    component: () => import('@/layouts/MainLayout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: '/dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/DashboardView.vue')
      },
      {
        path: '/system/role',
        name: 'Role',
        component: () => import('@/views/system/RoleView.vue')
      },
      {
        path: '/system/user',
        name: 'User',
        component: () => import('@/views/system/UserView.vue')
      },
      {
        path: '/system/menu',
        name: 'Menu',
        component: () => import('@/views/system/MenuView.vue')
      },
      {
        path: '/system/permission',
        name: 'Permission',
        component: () => import('@/views/permission/PermissionView.vue')
      },
      {
        path: '/lawsuit/claims',
        name: 'LawsuitClaimList',
        component: () => import('@/views/lawsuit/LawsuitClaimList.vue')
      },
      {
        path: '/lawsuit/claims/add',
        name: 'LawsuitClaimAdd',
        component: () => import('@/views/lawsuit/LawsuitClaimForm.vue')
      },
      {
        path: '/lawsuit/claims/edit/:id',
        name: 'LawsuitClaimEdit',
        component: () => import('@/views/lawsuit/LawsuitClaimForm.vue')
      },
      {
        path: '/lawsuit/claims/detail/:id',
        name: 'LawsuitClaimDetail',
        component: () => import('@/views/lawsuit/LawsuitClaimForm.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, _from, next) => {
  const userStore = useUserStore()
  const token = localStorage.getItem('token')

  // 未登录（无 token）且非登录页 → 跳转 /login
  if (!token && !to.meta.public) {
    next('/login')
    return
  }

  // 已登录访问 /login → 跳转 /dashboard
  if (token && to.path === '/login') {
    next('/dashboard')
    return
  }

  // 已登录但 store 中无用户信息 → 从 localStorage 恢复
  if (token && !userStore.userId) {
    userStore.loadFromStorage()
  }

  next()
})

export default router
