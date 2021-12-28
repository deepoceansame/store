<template>
  <buy-sell-exchange/>
  <sell-nav></sell-nav>
  <div>my goods list here</div>
  <button @click="goToAddGoods">添加商品</button>
  <table border=2>
    <tr v-if="myGoodsList.length>0"><th>图片</th> <th>名称</th> <th>价格</th> <th>描述</th></tr>
    <my-goods-table-row v-for="(goods, index) in myGoodsList" :goods="goods" :key="index"></my-goods-table-row>
  </table>
<!--  {{myGoodsList}}-->
</template>

<script>
import SellNav from "@/components/SellNav";
import BuySellExchange from "@/components/BuySellExchange";
import MyGoodsTableRow from "@/components/MyGoodsTableRow";
import {computed, onMounted, ref} from "vue";
import {useRoute, useRouter} from "vue-router";
import store from "@/store";
import axios from "axios";
import {message} from "ant-design-vue";

export default {
  name: "MyGoods",
  components: {SellNav, BuySellExchange, MyGoodsTableRow},
  setup(){
    const myGoodsList = ref([])
    const route = useRoute()
    const router = useRouter()
    const account = computed(() => {
      return store.state.account
    })

    const handleQuery = (params) => {
      myGoodsList.value = [];
      axios.get("/goods/getmygoods",{
        params:{
          accountId: params.accountId
        }
      }).then(
          (response) =>  {
            const data = response.data
            if (data.success){
              myGoodsList.value = data.content;
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

    const goToAddGoods = () => {
      router.push('/addgoods')
    }

    return {
      myGoodsList,
      goToAddGoods,
    }
  }
}
</script>

<style scoped>

</style>