<template>
  <nav id="nav">
    <router-link to="/goods/category=1&keyword=" :style="{color: getColor('goods')}">在卖的商品</router-link>|
    <router-link to="/collectedgoods/category=1&keyword=" :style="{color: getColor('collectedgoods')}">收藏的商品</router-link>|
    <router-link to="/myinquiry/category=1&keyword=" :style="{color: getColor('myinquiry')}">我的求购</router-link>
  </nav>
</template>

<script>

import {useRoute, useRouter} from 'vue-router'

export default {
  name: "BuyNav",
  setup(){
    const route = useRoute()
    const router = useRouter()
    const getColor = (stri) => {
      if(stri===route.path.split('/')[1]){
        return 'black'
      }
    }

    return {
      getColor
    }
  }
}
</script>

<style scoped>
  #nav a.router-link-exact-active{
    color: brown;

  }
</style>