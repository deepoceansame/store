<template>
  <buy-sell-exchange></buy-sell-exchange>
  <sell-nav></sell-nav>
  {{"paticisupply list here"}}
  <table>
    <InquiryTableRow v-for="(desiredgoods, index) in desiredgoodsList" :desiredgoods="desiredgoods" :key="index"></InquiryTableRow>
  </table>
  {{desiredgoodsList}}
</template>

<script>
import BuySellExchange from "@/components/BuySellExchange";
import SellNav from "@/components/SellNav";
import InquiryTableRow from "@/components/InquiryTableRow";
import {computed, onMounted, ref} from "vue";
import {useRoute} from "vue-router";
import store from "@/store";
import axios from "axios";
import {message} from "ant-design-vue";
export default {
  name: "ParticiSupply",
  components: {BuySellExchange, SellNav, InquiryTableRow},
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