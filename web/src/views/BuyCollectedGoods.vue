<template>
  <div class="page">
    <a-layout style="margin-bottom: 50px" id="components-layout-demo-top-side-2">
      <CategoryNav></CategoryNav>
    </a-layout>
    <h1 style="margin-left: 575px; font-family: Arial, 'Hiragino Sans GB', 'Microsoft Yahei', 'Microsoft Sans Serif', 'WenQuanYi Micro Hei', sans-serif">
      <font size="100">Sustech Store</font>
    </h1>
    <div style="margin-left: 610px"><search></search></div>
    <div style="margin-left: 600px; margin-top: 15px "><buy-nav></buy-nav></div>


    <div style="margin-left: 250px; margin-top: 15px">
      <table width="1000" border=2 bgcolor= white>
        <tr v-if="goodsList.length>0" align="center"><th width="250">图片</th> <th width="250">名称</th> <th width="150">价格</th> <th width="450">描述</th></tr>
        <GoodsTableRow v-for="(goods, index) in goodsList" :goods="goods" :key="index"></GoodsTableRow>
      </table>
    </div>
    <div style="margin-left: 610px ; margin-top: 30px">
      <pagina :total="total"></pagina>
    </div>
    <br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>


  </div>

  <!--  <button @click="click"></button>-->
</template>

<style>
#components-layout-demo-top-side-2 .logo {
  width: 120px;
  height: 31px;
  background: rgba(255, 255, 255, 0.2);
  margin: 16px 28px 16px 0;
  float: left;
}
.page{
  background-color: lightcyan;
}
</style>



<!--<template>-->
<!--  <div>-->
<!--    <CategoryNav></CategoryNav>-->
<!--    <buy-sell-exchange></buy-sell-exchange>-->
<!--    <buy-nav></buy-nav>-->
<!--    buy-collected-goods list here-->
<!--    <table border=2>-->
<!--      <tr v-if="goodsList.length>0"><th>图片</th> <th>名称</th> <th>价格</th> <th>描述</th></tr>-->
<!--      <GoodsTableRow v-for="(goods, index) in goodsList" :goods="goods" :key="index"></GoodsTableRow>-->
<!--    </table>-->
<!--  </div>-->
<!--&lt;!&ndash;  {{goodsList}}&ndash;&gt;-->
<!--</template>-->

<script>
import BuyNav from "@/components/BuyNav";
import axios from "axios";
import {message} from "ant-design-vue";
import {computed, onMounted, ref} from "vue";
import {useRoute, useRouter} from 'vue-router'
import store from "@/store";
import CategoryNav from "@/components/CategoryNav";
import GoodsTableRow from "@/components/GoodsTableRow";
import BuySellExchange from "@/components/BuySellExchange";
import Pagina from "@/components/Pagina";

export default {
  name: "BuyCollectedGoods",
  components:{
    BuyNav, CategoryNav, GoodsTableRow, Pagina
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