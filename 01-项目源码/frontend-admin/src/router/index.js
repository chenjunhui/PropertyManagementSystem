import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../stores/user'
import Login from '../views/Login.vue'
import AdminLayout from '../layout/AdminLayout.vue'
import Dashboard from '../views/Dashboard.vue'
import Buildings from '../views/Buildings.vue'
import Units from '../views/Units.vue'
import Owners from '../views/Owners.vue'
import Repairs from '../views/Repairs.vue'
import Profile from '../views/Profile.vue'
import PropertyFees from '../views/PropertyFees.vue'
import Announcements from '../views/Announcements.vue'
import Visitors from '../views/Visitors.vue'
import InspectionRecords from '../views/InspectionRecords.vue'
import Logs from '../views/Logs.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/login', component: Login },
    {
      path: '/',
      component: AdminLayout,
      redirect: '/dashboard',
      children: [
        { path: 'dashboard', component: Dashboard },
        { path: 'buildings', component: Buildings },
        { path: 'units', component: Units },
        { path: 'owners', component: Owners },
        { path: 'repairs', component: Repairs },
        { path: 'inspection-records', component: InspectionRecords },
        { path: 'visitors', component: Visitors },
        { path: 'announcements', component: Announcements },
        { path: 'property-fees', component: PropertyFees },
        { path: 'logs', component: Logs },
        { path: 'profile', component: Profile }
      ]
    }
  ]
})

router.beforeEach(async (to) => {
  const userStore = useUserStore()
  if (to.path === '/login') {
    if (userStore.user) return '/dashboard'
    return true
  }
  if (!userStore.user) return '/login'
  if (!userStore.menus.length) {
    await userStore.fetchMenus(userStore.user.role)
  }
  const allowed = userStore.menus.map(m => m.path)
  if (to.path !== '/' && allowed.length && !allowed.includes(to.path)) {
    return allowed[0] || '/login'
  }
})

export default router
