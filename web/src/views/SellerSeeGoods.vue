<template>
  {{goods}}
  <br/>
  {{goodsimgs}}
</template>

<script>
import axios from "axios";
import {computed, onMounted, reactive, ref} from "vue";
import {useRoute, useRouter}  from 'vue-router'
import store from "@/store";
import {message} from 'ant-design-vue';

export default {
  name: "BuyerSeeDesiredGoods",
  setup(){
    const route = useRoute();
    const router = useRouter()
    const goods = ref()
    const goodsimgs = ref([])
    const account = computed(() => {
      return store.state.account
    })

    const handleQuery = (params) => {
      axios.get("goods/getbyid/"+route.params.goodsid).then(
          (response) =>  {
            const data = response.data
            if (data.success){
              goods.value = data.content;
            } else{
              const a=0
            }
          }
      )

      axios.get("goods/getimages", {
        params:{
          goodsid: route.params.goodsid
        }
      }).then(
          (response) => {
            goodsimgs.value = response.data.content
          }
      )
    };

    onMounted(
        () => {
          console.log('mounted')
          handleQuery({id: route.params.goodsid});
        }
    )

    return{
      goods,
      goodsimgs
    }
  }
}
</script>

<style scoped>

</style>