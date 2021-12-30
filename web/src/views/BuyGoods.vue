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

<script>
import GoodsTableRow from "@/components/GoodsTableRow";
import BuyNav from "@/components/BuyNav";
import CategoryNav from "@/components/CategoryNav";
import Search from "@/components/Search";
import Pagina from "@/components/Pagina";
import BuySellExchange from "@/components/BuySellExchange";
import {computed, onMounted, ref, watch} from 'vue';
import {useRoute, useRouter} from 'vue-router'
import axios from "axios";
import {message,Layout} from 'ant-design-vue';
import store from "@/store";

export default {
  name: "BuyGoods",
  components: {BuyNav, CategoryNav, Search, GoodsTableRow, Pagina, BuySellExchange},
  setup(){
    const route = useRoute()
    //const router = useRouter()
    const goodsList = ref([]);
    const total = ref();
    const account = computed(() => {
      return store.state.account
    })
    watch(() => route.params,(newv, oldv) => {
          if (typeof(newv.page) == 'undefined'){
            return
          }
          console.log(newv.keyword)
          if(typeof(newv.keyword) == 'undefined' || newv.keyword.trim().length === 0){
            handleQuery({page: newv.page, categoryId: newv.category, accountId: account.value.id})
          }
          else{
            handleQuery({page: newv.page, name: newv.keyword, categoryId: newv.category, accountId: account.value.id})
          }
        }
    )
    const handleQuery = (params) => {
      goodsList.value = [];
      axios.get("/goods/getothersgoods",{
        params:{
          page: params.page,
          name: params.name,
          categoryId: params.categoryId,
          accountId: params.accountId
        }
      }).then(
          (response) =>  {
            const data = response.data
            if (data.success){
              goodsList.value = data.content.list;
              total.value = data.content.total;
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
            handleQuery({page: route.params.page, categoryId: route.params.category, accountId: account.value.id});
          }
          else{
            handleQuery({page: route.params.page, name:route.params.keyword, categoryId: route.params.catagory, accountId: account.value.id})
          }
        }
    )
    const click = () => {
      console.log(typeof(route.params.keyword))
      console.log(route.path)
      console.log(route.name !== "Home" && route.name !== "signup")
    }
    return {
      goodsList,
      total,
      click
    };
  },
}
</script>

<style scoped>

</style>