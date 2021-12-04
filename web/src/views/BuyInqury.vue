<template>
  <buy-nav></buy-nav>
  <div>buy inqury list here</div>
  <button @click="goToAddDesiredGoods">添加求购</button>
  {{DesiredGoodsList}}
</template>

<script>
import BuyNav from "@/components/BuyNav";
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
  },
  setup(){
    const DesiredGoodsList = ref([])
    const route = useRoute()
    const router = useRouter()
    const account = computed(() => {
      return store.state.account
    })

    const handleQuery = (params) => {
      DesiredGoodsList.value = [];
      axios.get("/desiredGoods/showDesiredGoodsListByAccountId",{
        params:{
          accountId: params.accountId
        }
      }).then(
          (response) =>  {
            const data = response.data
            if (data.success){
              DesiredGoodsList.value = data.content.list;
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
      DesiredGoodsList,
      goToAddDesiredGoods
    }
  }
}
</script>

<style scoped>

</style>