<template>
  <nav id="nav">
    <router-link to="/inquiry/page=1&category=1&keyword=" :style="{color: getColor('inquiry')}">全部求购</router-link>|
    <router-link to="/particisupply/page=1&category=1&keyword=" :style="{color: getColor('particisupply')}">我参加提供的求购</router-link>|
    <router-link to="/mygoods/page=1&category=1&keyword=" :style="{color: getColor('mygoods')}">我的商品</router-link>
  </nav>
</template>

<script>

import {useRoute, useRouter} from 'vue-router'

export default {
  name: "SellNav",
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