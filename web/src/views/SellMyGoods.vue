<template>
  <div class="page">
    <a-layout style="margin-bottom: 50px" id="components-layout-demo-top-side-2">
      <CategoryNav></CategoryNav>
    </a-layout>
    <h1 style="margin-left: 575px; font-family: Arial, 'Hiragino Sans GB', 'Microsoft Yahei', 'Microsoft Sans Serif', 'WenQuanYi Micro Hei', sans-serif">
      <font size="100">Sustech Store</font>
    </h1>
    <div style="margin-left: 600px; margin-top: 15px "><sell-nav></sell-nav></div>

    <a-button style="margin-left: 1100px; margin-top: 15px" type="primary" @click="goToAddGoods">
      添加商品
    </a-button>

    <div style="margin-left: 250px; margin-top: 15px">
      <table width="1000" border=2 bgcolor= white>
        <tr v-if="myGoodsList.length>0" align="center"><th width="250">图片</th> <th width="250">名称</th> <th width="150">价格</th> <th width="450">描述</th></tr>
        <my-goods-table-row v-for="(goods, index) in myGoodsList" :goods="goods" :key="index"></my-goods-table-row>
      </table>
    </div>
    <div style="margin-left: 610px ; margin-top: 30px">
      <pagina :total="total"></pagina>
    </div>
    <br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>
  </div>

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
<!--  <CategoryNav></CategoryNav>-->
<!--  <buy-sell-exchange/>-->
<!--  <sell-nav></sell-nav>-->
<!--  <div>my goods list here</div>-->
<!--  <button @click="goToAddGoods">添加商品</button>-->
<!--  <table border=2>-->
<!--    <tr v-if="myGoodsList.length>0"><th>图片</th> <th>名称</th> <th>价格</th> <th>描述</th></tr>-->
<!--    <my-goods-table-row v-for="(goods, index) in myGoodsList" :goods="goods" :key="index"></my-goods-table-row>-->
<!--  </table>-->
<!--&lt;!&ndash;  {{myGoodsList}}&ndash;&gt;-->
<!--</template>-->

<script>
import SellNav from "@/components/SellNav";
import CategoryNav from "@/components/CategoryNav";
import BuySellExchange from "@/components/BuySellExchange";
import MyGoodsTableRow from "@/components/MyGoodsTableRow";
import Pagina from "@/components/Pagina";
import {computed, onMounted, ref} from "vue";
import {useRoute, useRouter} from "vue-router";
import store from "@/store";
import axios from "axios";
import {message} from "ant-design-vue";

export default {
  name: "MyGoods",
  components: {SellNav, CategoryNav, BuySellExchange, MyGoodsTableRow, Pagina},
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