<template>
  <CategoryNav></CategoryNav>
  <br/>
  <search></search>
  <buy-nav></buy-nav>
  <div>buy-goods list here</div>
    <GoodsTableRow v-for="(goods, index) in goodsList" :goods="goods" :key="index"></GoodsTableRow>
  <table>

  </table>

  <pagina :total="total"></pagina>

  <button @click="click"></button>
</template>

<script>
import GoodsTableRow from "@/components/GoodsTableRow";
import BuyNav from "@/components/BuyNav";
import CategoryNav from "@/components/CategoryNav";
import Search from "@/components/Search";
import Pagina from "@/components/Pagina";
import {onMounted, ref, watch} from 'vue';
import {useRoute, useRouter} from 'vue-router'
import axios from "axios";
import {message} from 'ant-design-vue';

export default {
  name: "BuyGoods",
  components: {BuyNav, CategoryNav, Search, GoodsTableRow, Pagina},
  setup(){
    const route = useRoute()
    //const router = useRouter()
    const goodsList = ref([]);
    const total = ref();

    watch(() => route.params,(newv, oldv) => {
          console.log(newv.keyword)
          if(typeof(newv.keyword) == 'undefined' || newv.keyword.trim().length === 0){
            handleQuery({page: newv.page, categoryId: newv.category})
          }
          else{
            handleQuery({page: newv.page, name: newv.keyword, categoryId: newv.category})
          }
        }
    )
    const handleQuery = (params) => {
      goodsList.value = [];
      axios.get("/goods/list",{
        params:{
          page: params.page,
          name: params.name,
          categoryId: params.categoryId
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
            handleQuery({page: route.params.page, categoryId: route.params.category});
          }
          else{
            handleQuery({page: route.params.page, name:route.params.keyword, categoryId: route.params.catagory})
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