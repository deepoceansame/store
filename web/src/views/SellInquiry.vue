<template>
  <div class="page">
    <a-layout style="margin-bottom: 50px" id="components-layout-demo-top-side-2">
      <CategoryNav></CategoryNav>
    </a-layout>
    <h1 style="margin-left: 575px; font-family: Arial, 'Hiragino Sans GB', 'Microsoft Yahei', 'Microsoft Sans Serif', 'WenQuanYi Micro Hei', sans-serif">
      <font size="100">Sustech Store</font>
    </h1>
    <div style="margin-left: 610px"><search></search></div>
    <div style="margin-left: 600px; margin-top: 15px "><sell-nav></sell-nav></div>


    <div style="margin-left: 250px; margin-top: 15px">
      <table width="1000" border=2 bgcolor= white>
        <tr v-if="desiredgoodsList.length>0" align="center"><th width="250">图片</th> <th width="250">名称</th> <th width="150">价格</th> <th width="450">描述</th></tr>
        <InquiryTableRow v-for="(desiredgoods, index) in desiredgoodsList" :desiredgoods="desiredgoods" :key="index"></InquiryTableRow>
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
<!--  <buy-sell-exchange></buy-sell-exchange>-->
<!--  <CategoryNav></CategoryNav>-->
<!--  <br/>-->
<!--  <search></search>-->
<!--  <sell-nav></sell-nav>-->
<!--  <div>sell-desiredgoods list here</div>-->
<!--  <table border=2>-->
<!--    <tr v-if="desiredgoodsList.length>0"><th>图片</th> <th>名称</th> <th>价格</th> <th>描述</th></tr>-->
<!--    <InquiryTableRow v-for="(desiredgoods, index) in desiredgoodsList" :desiredgoods="desiredgoods" :key="index"></InquiryTableRow>-->
<!--  </table>-->
<!--&lt;!&ndash;  {{desiredgoodsList}}&ndash;&gt;-->

<!--  <pagina :total="total"></pagina>-->

<!--&lt;!&ndash;  <button @click="click"></button>&ndash;&gt;-->
<!--</template>-->

<script>
import GoodsTableRow from "@/components/GoodsTableRow";
import SellNav from "@/components/SellNav";
import CategoryNav from "@/components/CategoryNav";
import Search from "@/components/Search";
import Pagina from "@/components/Pagina";
import BuySellExchange from "@/components/BuySellExchange";
import InquiryTableRow from "@/components/InquiryTableRow";
import {computed, onMounted, ref, watch} from 'vue';
import {useRoute, useRouter} from 'vue-router'
import axios from "axios";
import {message} from 'ant-design-vue';
import store from "@/store";

export default {
  name: "SellInquiry",
  components: {SellNav, CategoryNav, Search, Pagina, BuySellExchange, InquiryTableRow},
  setup(){
    const route = useRoute()
    //const router = useRouter()
    const desiredgoodsList = ref([]);
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
            handleQuery({page: newv.page, keyword: newv.keyword, categoryId: newv.category, accountId: account.value.id})
          }
        }
    )
    const handleQuery = (params) => {
      desiredgoodsList.value = [];
      axios.get("/desiredgoods/getList",{
        params:{
          page: params.page,
          keyword: params.keyword,
          categoryId: params.categoryId,
          accountId: params.accountId
        }
      }).then(
          (response) =>  {
            const data = response.data
            if (data.success){
              desiredgoodsList.value = data.content.list;
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
            handleQuery({page: route.params.page, keyword:route.params.keyword, categoryId: route.params.category, accountId: account.value.id})
          }
        }
    )
    const click = () => {
      console.log(route.params.category)
    }
    return {
      desiredgoodsList,
      total,
      click
    };
  },
}
</script>

<style scoped>

</style>