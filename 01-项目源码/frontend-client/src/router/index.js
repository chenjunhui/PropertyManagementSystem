import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../stores/user'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import ClientLayout from '../layout/ClientLayout.vue'
import Home from '../views/Home.vue'
import RepairApply from '../views/RepairApply.vue'
import MyProperty from '../views/MyProperty.vue'
import Profile from '../views/Profile.vue'
import Announcements from '../views/Announcements.vue'
import VisitorApply from '../views/VisitorApply.vue'

const publicPaths = ['/login', '/register']

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/login', component: Login },
    { path: '/register', component: Register },
    {
      path: '/',
      component: ClientLayout,
      redirect: '/home',
      children: [
        { path: 'home', component: Home },
        { path: 'repair', component: RepairApply },
        { path: 'my-property', component: MyProperty },
        { path: 'profile', component: Profile },
        { path: 'announcements', component: Announcements },
        { path: 'visitor-apply', component: VisitorApply }
      ]
    }
  ]
})

router.beforeEach(async (to) => {
  const userStore = useUserStore()
  if (publicPaths.includes(to.path)) {
    if (userStore.user) return '/home'
    return true
  }
  if (!userStore.user) return '/login'
  return true
})

export default router
