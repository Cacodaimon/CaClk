import Vue from 'vue'
import Router from 'vue-router'
import Dashboard from '@/components/Dashboard'
import Settings from '@/components/Settings'
import AppAdd from '@/components/AppAdd'
import AppEdit from '@/components/AppEdit'
import Apps from '@/components/Apps'
import Log from '@/components/Log'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Dashboard',
      component: Dashboard
    },
    {
      path: '/settings',
      name: 'Settings',
      component: Settings
    },
    {
      path: '/app',
      name: 'Apps',
      component: Apps
    },
    {
      path: '/app/add',
      name: 'AppAdd',
      component: AppAdd
    },
    {
      path: '/app/:id',
      name: 'AppEdit',
      component: AppEdit
    },
    {
      path: '/log',
      name: 'Log',
      component: Log
    },
  ]
})
