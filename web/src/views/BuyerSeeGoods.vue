<template>
  <div>goodsInfo:{{goods}}</div>
  <br/>
  <div>sellerInfo:{{sellerAccount}}</div>
  <button @click="enroll" v-if="!hasenrolled">参与购买</button>
  <button @click="goToChatRoom">联系卖家</button>
  <button @click="quitBuy" v-if="hasenrolled">退出购买</button>
  {{goodsimgs}}
</template>

<script>
import axios from "axios";
import {computed, onMounted, reactive, ref} from "vue";
import {useRoute, useRouter}  from 'vue-router'
import store from "@/store";
import {message} from 'ant-design-vue';

export default {
  name: "BuyerSeeGoods",
  setup(){
    const route = useRoute();
    const router = useRouter()
    const goods = ref();
    const goodsimgs = ref([]);
    const hasenrolled = ref(false);
    const sellerAccount = reactive({
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
      axios.get("/goods/getbyid/"+route.params.goodsid).then(
          (response) =>  {
            const data = response.data
            if (data.success){
              goods.value = data.content;
              axios.get("/account/getbyid/"+goods.value.accountId).then(
                  (response) => {
                    const data = response.data.content
                    sellerAccount.id = data.id
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
      );
      axios.get("/goods/getimages", {
        params:{
          goodsid: route.params.goodsid
        }
      }).then(
          (response) => {
            goodsimgs.value = response.data.content
          }
      );
      axios.get("/account/hasenrolledbuy", {
        params:{
          buyerId: account.value.id,
          goodsId: route.params.goodsid
        }
      }).then(
          (response) => {
            hasenrolled.value = response.data.content
          }
      )
    };

    const goToChatRoom = () => {
      axios.get('account/requestGoods',{
        params:{
          accountId: account.value.id,
          goodId: goods.value.id
        }
      })
      router.push(`/chatroom/goodsid=${goods.value.id}&senderid=${account.value.id}&receiverid=${sellerAccount.id}`)
    }

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
            if(typeof(response.data.message)=='undefined'){
              message.info('参与过了')
            }
            else{
              message.info('成功参与')
              hasenrolled.value = true
            }
          }
      )
    }

    const quitBuy = () => {
      axios.post('account/quitbuy',{
        buyerId: account.value.id,
        goodsId: goods.value.id
      }).then(
          (response) => {
            message.info(response.data.message)
            hasenrolled.value = false
          }
      )
    }


    return {
      goods,
      sellerAccount,
      enroll,
      goToChatRoom,
      goodsimgs,
      hasenrolled,
      quitBuy
    }
  }
}
</script>

<style scoped>

</style>