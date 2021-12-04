<template>
  <buy-nav></buy-nav>
  <div>buy inqury list here</div>
  <button @click="goToAddDesiredGoods">添加求购</button>
  <table>
    <DesiredGoodsTableRow v-for="(goods, index) in desiredGoodsList" :goods="goods" :key="index"></DesiredGoodsTableRow>
  </table>
  {{desiredGoodsList}}
</template>

<script>
import BuyNav from "@/components/BuyNav";
import DesiredGoodsTableRow from "@/components/DesiredGoodsTableRow";
import CategoryNav from "@/components/CategoryNav";
import Search from "@/components/Search";
import {computed, onMounted, ref} from "vue";
import {useRoute, useRouter} from "vue-router";
import store from "@/store";
import axios from "axios";
import {message} from "ant-design-vue";
export default {
  name: "BuyInqury",
  components:{
    BuyNav,
    DesiredGoodsTableRow,
  },
  setup(){
    const desiredGoodsList = ref([])
    const route = useRoute()
    const router = useRouter()
    const account = computed(() => {
      return store.state.account
    })

    const handleQuery = (params) => {
      desiredGoodsList.value = [];
      axios.get("/desiredGoods/showDesiredGoodsListByAccountId",{
        params:{
          accountId: params.accountId
        }
      }).then(
          (response) =>  {
            const data = response.data
            if (data.success){
              desiredGoodsList.value = data.content.list;
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

    const goToAddDesiredGoods = () => {
      router.push('/adddesiredgoods')
    }

    return {
      desiredGoodsList,
      goToAddDesiredGoods
    }
  }
}
</script>

<style scoped>

</style>