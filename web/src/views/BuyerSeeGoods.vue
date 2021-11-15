<template>
  {{goods}}
</template>

<script>
import axios from "axios";
import {onMounted, reactive, ref} from "vue";
import {useRoute}  from 'vue-router'

export default {
  name: "BuyerSeeGoods",
  setup(){
    const route = useRoute();
    const goods = ref();
    const handleQuery = (params) => {
      axios.get("/goods/list",{
        params:{
          page: 0,
          id: params.id
        }
      }).then(
          (response) =>  {
            const data = response.data
            if (data.success){
              goods.value = data.content.list[0];
            } else{
              const a=0
            }
          }
      )
    };
    onMounted(
        () => {
          console.log('mounted')
          handleQuery({id: route.params.goodsid});
        }
    )

    return {
      goods
    }
  }
}
</script>

<style scoped>

</style>