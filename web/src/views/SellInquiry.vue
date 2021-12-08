<template>
  <buy-sell-exchange></buy-sell-exchange>
  <CategoryNav></CategoryNav>
  <br/>
  <search></search>
  <sell-nav></sell-nav>
  <div>sell-desiredgoods list here</div>
  <table>
    <InquiryTableRow v-for="(desiredgoods, index) in desiredgoodsList" :desiredgoods="desiredgoods" :key="index"></InquiryTableRow>
  </table>
  {{desiredgoodsList}}

  <pagina :total="total"></pagina>

  <button @click="click"></button>
</template>

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
            handleQuery({page: route.params.page, keyword:route.params.keyword, categoryId: route.params.catagory, accountId: account.value.id})
          }
        }
    )
    const click = () => {
      console.log(typeof(route.params.keyword))
      console.log(route.path)
      console.log(route.name !== "Home" && route.name !== "signup")
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