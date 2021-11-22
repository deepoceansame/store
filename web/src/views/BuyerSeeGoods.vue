<template>
  <div>goodsInfo:{{goods}}</div>
  <br/>
  <div>sellerInfo:{{sellerAccount}}</div>
  <button @click="enroll">参与购买</button>
</template>

<script>
import axios from "axios";
import {computed, onMounted, reactive, ref} from "vue";
import {useRoute}  from 'vue-router'
import store from "@/store";

export default {
  name: "BuyerSeeGoods",
  setup(){
    const route = useRoute();
    const goods = ref();
    const sellerAccount = reactive({
      name: '',
      qq: '',
      mail: '',
      avator: ''
    });
    const account = computed(() => {
      return store.state.account
    })

    const handleQuery = (params) => {
      axios.get("/goods/getbyid/"+route.params.goodsid).then(
          (response) =>  {
            const data = response.data
            if (data.success){
              goods.value = data.content;
              axios.get("/account/getbyid/"+goods.value.accountId).then(
                  (response) => {
                    const data = response.data.content
                    sellerAccount.name = data.name
                    sellerAccount.qq = data.qq
                    sellerAccount.mail = data.mail
                    sellerAccount.avator = data.avator
                  }
              )
            } else{
              const a=0
            }
          }
      )
    };

    onMounted(
        () => {
          console.log('mounted')
          handleQuery({id: route.params.goodsid});
        }
    )

    // @GetMapping("account/requestGoods")
    const enroll = () => {
      axios.get('account/requestGoods',{
        params:{
          accountId: account.value.id,
          goodId: goods.value.id
        }
      }).then(
          (response) => {
            console.log(response.data)
          }
      )
    }

    return {
      goods,
      sellerAccount,
      enroll
    }
  }
}
</script>

<style scoped>

</style>