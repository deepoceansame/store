<template>
  <div>goodsInfo:{{goods}}</div>
  <div>
    <h2>商品描述</h2>
    <table border=2>
      <tr>
        <th>商品名</th> <th>价格</th>
      </tr>
      <tr>
        <td>{{goods.name}}</td>
        <td>{{goods.price}}</td>
      </tr>
    </table>
  </div>
  <div>
    <h3>商品描述</h3>
    <p>
      {{goods.description}}
    </p>
  </div>
  <div>sellerInfo:{{sellerAccount}}</div>
  <div>
    <h2>卖家信息</h2>
    <table border=2>
      <tr>
        <th>头像</th> <th>id</th> <th>邮箱</th> <th>qq</th> <th>昵称</th>
      </tr>
      <tr>
        <td><img :src="'http://127.0.0.1:8083/' + sellerAccount.avator" alt="无头像" style="max-height: 50px"/></td>
        <td>{{sellerAccount.id}}</td>
        <td>{{sellerAccount.mail}}</td>
        <td>{{sellerAccount.qq}}</td>
        <td>{{sellerAccount.name}}</td>
      </tr>
    </table>
  </div>
  {{goodsimgs}}
  <div>
    <h2>商品图片</h2>
    <img v-for="(img, index) in goodsimgs"
         :src="'http://127.0.0.1:8083/'+img"
         :key="index" alt="图片没了"
         style="max-height: 100px"/>
  </div>
  <button @click="enroll" v-if="!hasenrolled">参与购买</button>
  <button @click="goToChatRoom">联系卖家</button>
  <button @click="quitBuy" v-if="hasenrolled">退出购买</button>
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
    const goods = ref({});
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