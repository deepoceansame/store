<template>
<input placeholder="输入关键词进行查询" v-model="keyw"/>
<a-button style="margin-left: 15px" type="primary" @click="handleSearch">搜索</a-button>
</template>

<script>
import {onMounted, ref} from 'vue'
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
      router.push('/'+base[1]+`/page=1&category=${route.params.category}&keyword=${keyw.value}`)
    }
    onMounted(
        () => {
          if(!(typeof(route.params.keyword) == 'undefined' || route.params.keyword.trim().length === 0)){
            keyw.value = route.params.keyword
          }
        }
    )
    return{
      keyw,
      handleSearch
    }
  }
}
</script>

<style scoped>

</style>