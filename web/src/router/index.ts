import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import Home from '../views/Home.vue'
import List from  '../views/List.vue'
import Goods from '../views/Goods.vue'
import BuyGoods from '../views/BuyGoods.vue'
import  BuyCollectedGoods from '../views/BuyCollectedGoods.vue'
import BuyInqury from '../views/BuyInqury.vue'
const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/about',
    name: 'About',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
  },
  {
    path:'/list',
    name:'List',
    component:List
  },
  {
    path:'/goodsinfo/:id',
    name:'Goods',
    component:Goods
  },
  {
    path:'/goods/category=:category&keyword=:keyword',
    name:'buy-goods',
    component:BuyGoods
  },
  {
    path:'/collectedgoods/category=:category&keyword=:keyword',
    name:'buy-collectedgoods',
    component:BuyCollectedGoods
  },
  {
    path:'/myinquiry/category=:category&keyword=:keyword',
    name:'myinqury',
    component:BuyInqury
  },
  {
    path:'/goods/category=:category&keyword=',
    name:'buy-goodsi',
    component:BuyGoods
  },
  {
    path:'/collectedgoods/category=:category&keyword=',
    name:'buy-collectedgoodsi',
    component:BuyCollectedGoods
  },
  {
    path:'/myinquiry/category=:category&keyword=',
    name:'myinquryi',
    component:BuyInqury
  },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
