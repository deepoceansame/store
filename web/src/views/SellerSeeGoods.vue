<template>
  {{goods}}
  <br/>
  {{goodsimgs}}
  {{buyerList}}
  <button @click="shutGoods">关闭售卖</button>
</template>

<script>
import axios from "axios";
import {computed, onMounted, reactive, ref} from "vue";
import {useRoute, useRouter}  from 'vue-router'
import store from "@/store";
import {message} from 'ant-design-vue';
import FormData from "form-data";

export default {
  name: "BuyerSeeDesiredGoods",
  setup(){
    const route = useRoute();
    const router = useRouter()
    const goods = ref()
    const goodsimgs = ref([])
    const buyerList = ref([])
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

      axios.get("account/getbuyer",{
        params:{
          goodsId: route.params.goodsid
        }
      }).then(
          (response) =>{
            buyerList.value = response.data.content;
          }
      )
    };

    const shutGoods = ()=>{
      const fd = new FormData();
      fd.append('goodsId', route.params.goodsid)
      axios.post('goods/deletegoodsbypid', fd).then(
          (response) => {
            message.info("已删除")
            router.push('/mygoods/page=:page&category=:category&keyword=')
          }
      )
    }

    onMounted(
        () => {
          console.log('mounted')
          handleQuery({id: route.params.goodsid});
        }
    )

    return{
      goods,
      goodsimgs,
      shutGoods,
      buyerList,
    }
  }
}
</script>

<style scoped>

</style>