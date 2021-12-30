<template>
  <a-layout-header class="header">
    <div class="logo" />
    <a-menu
        theme="dark"
        mode="horizontal"
        :default-selected-keys="['2']"
        :style="{ lineHeight: '64px' }"
    >
      <a-menu-item key="1" @click="handle(1)" :disabled="getDisabled(1)">
        全部
      </a-menu-item>
      <a-menu-item key="2" @click="handle(2)" :disabled="getDisabled(2)">
        生活用品
      </a-menu-item>
      <a-menu-item key="3" @click="handle(3)" :disabled="getDisabled(3)">
        零食
      </a-menu-item>
      <a-menu-item key="4" @click="handle(4)" :disabled="getDisabled(4)">
        电子
      </a-menu-item>
      <a-menu-item key="5" @click="handle(5)" :disabled="getDisabled(5)">
        书籍
      </a-menu-item>
      <a-menu-item key="6" @click="handle(6)" :disabled="getDisabled(6)">
        其它
      </a-menu-item>
      <a-menu-item key="7" style="margin-left: 450px" @click="goToBuy">
        买家模式
      </a-menu-item>
      <a-menu-item key="8" @click="goToSell">
        卖家模式
      </a-menu-item>
    </a-menu>
  </a-layout-header>






</template>

<script>
import {useRoute, useRouter} from 'vue-router'
import {ref,} from "vue";

export default {
  props:['posi'],
  name: "CategoryNav",
  setup(){
    const route = useRoute()
    const router = useRouter()
    const getDisabled = (num) => {
      return num===Number(route.params.category)
    }
    const goToBuy = () => {
      router.push("/goods/page=1&category=1&keyword=")
    }
    const goToSell = () => {
      router.push("/inquiry/page=1&category=1&keyword=")
    }
    const val = ref(1)
    const base = route.path.split('/')
    const handle = (num) => {
      if (typeof(route.params.keyword) !== 'undefined'){
        router.push('/'+base[1]+`/page=1&category=${num}&keyword=${route.params.keyword}`)
      } else{
        router.push('/'+base[1]+`/page=1&category=${num}&keyword=`)
      }
    };

    return{
      handle, getDisabled,goToBuy, goToSell
    }
  }
}
</script>

<style scoped>

</style>