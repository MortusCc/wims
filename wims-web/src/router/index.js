// 路由配置 —— 左侧菜单与页面一一对应,hash 模式便于任意静态服务器部署
import { createRouter, createWebHashHistory } from 'vue-router'

const routes = [
  { path: '/', redirect: '/products' },
  { path: '/products', name: 'Products', component: () => import('../views/Products.vue'), meta: { title: '商品管理' } },
  { path: '/stock', name: 'Stock', component: () => import('../views/Stock.vue'), meta: { title: '库存管理' } },
  { path: '/inbound', name: 'Inbound', component: () => import('../views/Inbound.vue'), meta: { title: '入库单' } },
  { path: '/outbound', name: 'Outbound', component: () => import('../views/Outbound.vue'), meta: { title: '出库单' } },
  { path: '/alert', name: 'Alert', component: () => import('../views/Alert.vue'), meta: { title: '库存预警' } },
  { path: '/statistics', name: 'Statistics', component: () => import('../views/Statistics.vue'), meta: { title: '今日统计' } }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
