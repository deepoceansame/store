<template>
  <div>desiredgoodsInfo:{{desiredgoods}}</div>
  <br/>
  <div>buyerInfo:{{buyerAccount}}</div>
  {{desiredgoodsimgs}}
  <button @click="enroll">参与提供</button>
  <button @click="goToChatRoom">联系求购者</button>
</template>

<script>
import {useRoute, useRouter} from "vue-router";
import {computed, onMounted, reactive, ref} from "vue";
import store from "@/store";
import axios from "axios";
import {message} from "ant-design-vue";

export default {
  name: "SellerSeeDesiredGoods",
  setup(){
    const route = useRoute();
    const router = useRouter()
    const desiredgoods = ref();
    const desiredgoodsimgs = ref([]);
    const buyerAccount = reactive({
      id: undefined,
      name: '',
      qq: '',
      mail: '',
      avator: ''
    });
    const account = computed(() => {
      return store.state.account
    })

    const handleQuery = (params) => {
      axios.get("/desiredGoods/getbyid/"+route.params.desiredgoodsid).then(
          (response) =>  {
            const data = response.data
            if (data.success){
              desiredgoods.value = data.content;
              axios.get("/account/getbyid/"+desiredgoods.value.accountId).then(
                  (response) => {
                    const data = response.data.content
                    buyerAccount.id = data.id
                    buyerAccount.name = data.name
                    buyerAccount.qq = data.qq
                    buyerAccount.mail = data.mail
                    buyerAccount.avator = data.avator
                  }
              )
            } else{
              const a=0
            }
          }
      )
      axios.get("/desiredGoods/getimages", {
        params:{
          desiredgoodsid: route.params.desiredgoodsid
        }
      }).then(
          (response) => {
            desiredgoodsimgs.value = response.data.content
          }
      )
    };

    const goToChatRoom = () => {
      axios.post('account/particisupply',{
        desiredgoodsId: desiredgoods.value.id,
        accountId: account.value.id
      })
      router.push(`/chatroomd/desiredgoodsid=${desiredgoods.value.id}&senderid=${account.value.id}&receiverid=${buyerAccount.id}`)
    }

    onMounted(
        () => {
          console.log('mounted')
          handleQuery({id: route.params.desiredgoodsid});
        }
    )

    // @GetMapping("account/requestGoods")
    const enroll = () => {
      axios.post('account/particisupply',{
        desiredgoodsId: desiredgoods.value.id,
        accountId: account.value.id
      }).then(
          (response) => {
            if(typeof(response.data.message)=='undefined'){
              message.info('参与过了')
            }
            else{
              message.info('成功参与')
            }
          }
      )
    }

    return {
      desiredgoods,
      buyerAccount,
      enroll,
      goToChatRoom,
      desiredgoodsimgs,
    }
  }
}
</script>

<style scoped>

</style>