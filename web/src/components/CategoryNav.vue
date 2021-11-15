<template>
  <a-button @click="handle(1)" :disabled="getDisabled(1)">全部</a-button>
  <a-button @click="handle(2)" :disabled="getDisabled(2)">生活用品</a-button>
  <a-button @click="handle(3)" :disabled="getDisabled(3)">零食</a-button>
  <a-button @click="handle(4)" :disabled="getDisabled(4)">电子</a-button>
  <a-button @click="handle(5)" :disabled="getDisabled(5)">书籍</a-button>
  <a-button @click="handle(6)" :disabled="getDisabled(6)">其它</a-button>
</template>

<script>
import {useRoute, useRouter} from 'vue-router'

export default {
  props:['posi'],
  name: "CategoryNav",
  setup(){
    const route = useRoute()
    const router = useRouter()
    const getDisabled = (num) => {
      return num===Number(route.params.category)
    }
    const base = route.path.split('/')
    const handle = (num) => {
      router.push('/'+base[1]+`/page=1&category=${num}&keyword=${route.params.keyword}`)
    }
    return{
      handle, getDisabled
    }
  }
}
</script>

<style scoped>

</style>