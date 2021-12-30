<template>
  <div class="page">
    <a-layout style="margin-bottom: 50px" id="components-layout-demo-top-side-2">
      <CategoryNav></CategoryNav>
    </a-layout>
    <h1 style="margin-left: 575px; font-family: Arial, 'Hiragino Sans GB', 'Microsoft Yahei', 'Microsoft Sans Serif', 'WenQuanYi Micro Hei', sans-serif">
      <font size="100">Sustech Store</font>
    </h1>
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
<!--  <sell-nav></sell-nav>-->
<!--  {{"paticisupply list here"}}-->
<!--  <table border=2>-->
<!--    <tr v-if="desiredgoodsList.length>0"><th>图片</th> <th>名称</th> <th>价格</th> <th>描述</th></tr>-->
<!--    <InquiryTableRow v-for="(desiredgoods, index) in desiredgoodsList" :desiredgoods="desiredgoods" :key="index"></InquiryTableRow>-->
<!--  </table>-->
<!--&lt;!&ndash;  {{desiredgoodsList}}&ndash;&gt;-->
<!--</template>-->

<script>
import BuySellExchange from "@/components/BuySellExchange";
import SellNav from "@/components/SellNav";
import InquiryTableRow from "@/components/InquiryTableRow";
import Pagina from "@/components/Pagina";
import CategoryNav from "@/components/CategoryNav";
import {computed, onMounted, ref} from "vue";
import {useRoute} from "vue-router";
import store from "@/store";
import axios from "axios";
import {message} from "ant-design-vue";
export default {
  name: "ParticiSupply",
  components: {BuySellExchange, CategoryNav, SellNav, InquiryTableRow,Pagina},
  setup(){

    const desiredgoodsList = ref([])
    const route = useRoute()
    const account = computed(() => {
      return store.state.account
    })

    const handleQuery = (params) => {
      desiredgoodsList.value = [];
      axios.get("/account/getaccountsupplying",{
        params:{
          accountId: params.accountId
        }
      }).then(
          (response) =>  {
            const data = response.data
            if (data.success){
              desiredgoodsList.value = data.content;
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
      desiredgoodsList
    }
  }
}
</script>

<style scoped>

</style>