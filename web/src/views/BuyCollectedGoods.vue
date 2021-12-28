<template>
  <div>
    <buy-sell-exchange></buy-sell-exchange>
    <buy-nav></buy-nav>
    buy-collected-goods list here
    <table border=2>
      <tr v-if="goodsList.length>0"><th>图片</th> <th>名称</th> <th>价格</th> <th>描述</th></tr>
      <GoodsTableRow v-for="(goods, index) in goodsList" :goods="goods" :key="index"></GoodsTableRow>
    </table>
  </div>
<!--  {{goodsList}}-->
</template>

<script>
import BuyNav from "@/components/BuyNav";
import axios from "axios";
import {message} from "ant-design-vue";
import {computed, onMounted, ref} from "vue";
import {useRoute, useRouter} from 'vue-router'
import store from "@/store";
import GoodsTableRow from "@/components/GoodsTableRow";
import BuySellExchange from "@/components/BuySellExchange";

export default {
  name: "BuyCollectedGoods",
  components:{
    BuyNav, GoodsTableRow, BuySellExchange
  },
  setup(){

    const goodsList = ref([])
    const route = useRoute()
    const account = computed(() => {
      return store.state.account
    })

    const handleQuery = (params) => {
      goodsList.value = [];
      axios.get("/account/showPurchaseRecordsByAccountId",{
        params:{
          accountId: params.accountId
        }
      }).then(
          (response) =>  {
            const data = response.data
            if (data.success){
              goodsList.value = data.content.list;
            } else{
              message.error(data.message)
            }
          }
      )
    }

    onMounted(
        () => {
          console.log('mounted')
          if(typeof(route.params.keyword) == 'undefined' || route.params.keyword.trim().length === 0){
            handleQuery({accountId: account.value.id});
          }
          else{
            handleQuery({accountId: account.value.id})
          }
        }
    )

    return {
      goodsList
    }
  }


}
</script>

<style scoped>

</style>