import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import Home from '../views/Home.vue'
import List from  '../views/List.vue'
import Goods from '../views/Goods.vue'
import BuyGoods from '../views/BuyGoods.vue'
import  BuyCollectedGoods from '../views/BuyCollectedGoods.vue'
import BuyInqury from '../views/BuyInqury.vue'
import AddGoods from '../views/AddGoods.vue'
import BuyerSeeGoods from '../views/BuyerSeeGoods.vue'
import BuyerSeeDesiredGoods from '../views/BuyerSeeDesiredGoods.vue'
import Signup from '../views/Signup.vue'
import Chatroom from '../views/Chatroom.vue'
import AddDesiredGoods from '../views/AddDesiredGoods.vue'
import Chatroomd from '../views/Chatroomd.vue'

import store from "@/store";
import axios from "axios";
import {message} from "ant-design-vue";
import {computed} from "vue";

const account = computed(() => {
  return store.state.account
})

const logout = () => {
  axios.get('/account/logout/' + account.value.token).then((response) => {
    const data: any = response.data;
    if (data.success) {
      message.success("退出登录成功！");
      store.commit("setAccount", {});
      router.push("/")
    } else {
      message.error(data.message);
    }
  });
}

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'Home',
    component: Home,
    beforeEnter: (to, from, next) => {
      if (account.value.id){
        logout()
        next()
      }
      else next()
    },
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
    path:'/goods/page=:page&category=:category&keyword=:keyword',
    name:'buy-goods',
    component:BuyGoods
  },
  {
    path:'/collectedgoods/page=:page&category=:category&keyword=:keyword',
    name:'buy-collectedgoods',
    component:BuyCollectedGoods
  },
  {
    path:'/myinquiry/page=:page&category=:category&keyword=:keyword',
    name:'myinqury',
    component:BuyInqury
  },
  {
    path:'/goods/page=:page&category=:category&keyword=',
    name:'buy-goodsi',
    component:BuyGoods
  },
  {
    path:'/collectedgoods/page=:page&category=:category&keyword=',
    name:'buy-collectedgoodsi',
    component:BuyCollectedGoods
  },
  {
    path:'/myinquiry/page=:page&category=:category&keyword=',
    name:'myinquryi',
    component:BuyInqury
  },
  {
    path:'/addgoods',
    name: 'addgoods',
    component: AddGoods
  },
  {
    path:'/adddesiredgoods',
    name: 'adddesiredgoods',
    component: AddDesiredGoods
  },
  {
    path:'/buyerseegoods/goodsid=:goodsid',
    name:'buyerseegoods',
    component: BuyerSeeGoods
  },
  {
    path:'/buyerseedesiredgoods/desiredgoodsid=:desiredgoodsid',
    name:'buyerseedesiredgoods',
    component: BuyerSeeDesiredGoods
  },
  {
    path:'/signup',
    name:'signup',
    component: Signup,
    beforeEnter: (to, from, next) => {
      if (account.value.id){
        logout()
        next()
      }
      else next()
    },
  },
  {
    path:'/chatroom/goodsid=:goodsid&senderid=:senderid&receiverid=:receiverid',
    name:'chatroom',
    component: Chatroom,
  },
  {
    path:'/chatroomd/desiredgoodsid=:desiredgoodsid&senderid=:senderid&receiverid=:receiverid',
    name:'chatroomd',
    component: Chatroomd,
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

router.beforeEach((to, from, next) => {
  const account = store.state.account;
  if (to.name !== 'signup' && to.name !== 'Home' && !account.id) next({name: 'Home'})
  else next()
})

export default router
