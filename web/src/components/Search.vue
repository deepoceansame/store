<template>
<input v-model="keyw"/><button @click="handleSearch">搜索</button>
</template>

<script>
import {ref} from 'vue'
import {useRoute, useRouter} from 'vue-router'
export default {
  name: "Search",
  setup(){
    const router = useRouter()
    const route = useRoute()
    const keyw =ref("")
    const base = route.path.split('/')
    const handleSearch = () =>{
      if(String(keyw.value)===''){
        keyw.value=' '
      }
      router.push('/'+base[1]+`/category=${route.params.category}&keyword=${keyw.value}`)
    }
    return{
      keyw,
      handleSearch
    }
  }
}
</script>

<style scoped>

</style>