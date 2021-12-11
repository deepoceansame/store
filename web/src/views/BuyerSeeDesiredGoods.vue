<template>
  {{desiredgoods}}
  <br/>
  {{desiredgoodsimgs}}
  <button @click="shutDesiredGoods">关闭求购</button>
  {{supplyerList}}
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
    const desiredgoods = ref()
    const desiredgoodsimgs = ref([])
    const supplyerList = ref([])
    const account = computed(() => {
      return store.state.account
    })

    const handleQuery = (params) => {
      axios.get("desiredGoods/getbyid/"+route.params.desiredgoodsid).then(
          (response) =>  {
            const data = response.data
            if (data.success){
              desiredgoods.value = data.content;
            } else{
              const a=0
            }
          }
      )

      axios.get("desiredGoods/getimages", {
        params:{
          desiredgoodsid: route.params.desiredgoodsid
        }
      }).then(
          (response) => {
            desiredgoodsimgs.value = response.data.content
          }
      )

      axios.get("account/getsupplyer",{
        params: {
          desiredGoodsId: route.params.desiredgoodsid
        }
      }).then(
          (response) =>{
            supplyerList.value = response.data.content;
          }
      )
    };

    onMounted(
        () => {
          console.log('mounted')
          handleQuery({id: route.params.goodsid});
        }
    )

    const shutDesiredGoods = ()=>{
      const fd = new FormData();
      fd.append('desiredgoodsId', route.params.desiredgoodsid)
      axios.post('desiredGoods/deleteinquirybypid', fd).then(
          (response) => {
            message.info("已删除")
            router.push('/myinquiry/page=:page&category=:category&keyword=')
          }
      )
    }

    return{
      desiredgoods,
      desiredgoodsimgs,
      shutDesiredGoods,
      supplyerList,
    }
  }
}
</script>

<style scoped>

</style>