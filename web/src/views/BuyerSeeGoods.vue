<template>
  <div class="page">
    <a-layout style="margin-bottom: 20px">
      <a-layout-header class="header">
        <div class="logo" />
        <a-menu
            theme="dark"
            mode="horizontal"
            :default-selected-keys="['2']"
            :style="{ lineHeight: '64px' }"
        >
          <a-menu-item style="margin-left: 650px; font-weight: bold" key="1" >
            Sustech Store
          </a-menu-item>
        </a-menu>
      </a-layout-header>
    </a-layout>

    <h1 style="margin-left: 655px">
      <font size="75">商品信息</font>
    </h1>

    <div style="margin-left: 460px; margin-top: 15px">
      <table width="600" height="100" border=2 bgcolor= white>
        <tr align="center"><th width="150">商品名</th> <th width="150">价格</th> <th width="300">描述</th> </tr>
        <tr align="center">
          <td>{{goods.name}}</td>
          <td>{{goods.price}}</td>
          <td>{{goods.description}}</td>
        </tr>
      </table>
    </div>

    <h1 style="margin-left: 655px;  margin-top: 15px">
      <font size="75">商品图片</font>
    </h1>
    <div style="margin-left: 700px">
      <img v-for="(img, index) in goodsimgs"
               :src="'http://127.0.0.1:8083/'+img"
               :key="index" alt="图片没了"
               style="max-height: 100px"/>
    </div>


    <h1 style="margin-left: 655px; margin-top: 30px">
      <font size="75">卖家信息</font>
    </h1>

    <div style="margin-left: 460px; margin-top: 15px">
    <table width="600" height="100" border=2 bgcolor= white>
      <tr align="center"><th width="120">头像</th> <th width="60">id</th> <th width="180">邮箱</th><th width="120">qq</th><th width="120">昵称</th> </tr>
      <tr align="center">
        <td><img :src="'http://127.0.0.1:8083/' + sellerAccount.avator" alt="无头像" style="max-height: 50px"/></td>
        <td>{{sellerAccount.id}}</td>
        <td>{{sellerAccount.mail}}</td>
        <td>{{sellerAccount.qq}}</td>
        <td>{{sellerAccount.name}}</td>
      </tr>

    </table>
    </div>
    <div style="margin-left: 650px; margin-top: 25px">
    <a-button style="margin-right: 30px" type="primary" @click="enroll" v-if="!hasenrolled">参与购买</a-button>
    <a-button @click="goToChatRoom">联系卖家</a-button>
    <a-button style="margin-left: 30px" @click="quitBuy" v-if="hasenrolled">退出购买</a-button>
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
<!--&lt;!&ndash;  <div>goodsInfo:{{goods}}</div>&ndash;&gt;-->
<!--  <div>-->
<!--    <h2>商品信息</h2>-->
<!--    <table border=2>-->
<!--      <tr>-->
<!--        <th>商品名</th> <th>价格</th>-->
<!--      </tr>-->
<!--      <tr>-->
<!--        <td>{{goods.name}}</td>-->
<!--        <td>{{goods.price}}</td>-->
<!--      </tr>-->
<!--    </table>-->
<!--  </div>-->
<!--  <div style="border-style: solid">-->
<!--    <h2>商品描述</h2>-->
<!--    <p>-->
<!--      {{goods.description}}-->
<!--    </p>-->
<!--  </div>-->
<!--&lt;!&ndash;  <div>sellerInfo:{{sellerAccount}}</div>&ndash;&gt;-->
<!--  <div>-->
<!--    <h2>卖家信息</h2>-->
<!--    <table border=2>-->
<!--      <tr>-->
<!--        <th>头像</th> <th>id</th> <th>邮箱</th> <th>qq</th> <th>昵称</th>-->
<!--      </tr>-->
<!--      <tr>-->
<!--        <td><img :src="'http://127.0.0.1:8083/' + sellerAccount.avator" alt="无头像" style="max-height: 50px"/></td>-->
<!--        <td>{{sellerAccount.id}}</td>-->
<!--        <td>{{sellerAccount.mail}}</td>-->
<!--        <td>{{sellerAccount.qq}}</td>-->
<!--        <td>{{sellerAccount.name}}</td>-->
<!--      </tr>-->
<!--    </table>-->
<!--  </div>-->
<!--&lt;!&ndash;  {{goodsimgs}}&ndash;&gt;-->
<!--  <div>-->
<!--    <h2>商品图片</h2>-->
<!--    <img v-for="(img, index) in goodsimgs"-->
<!--         :src="'http://127.0.0.1:8083/'+img"-->
<!--         :key="index" alt="图片没了"-->
<!--         style="max-height: 100px"/>-->
<!--  </div>-->
<!--  <button @click="enroll" v-if="!hasenrolled">参与购买</button>-->
<!--  <button @click="goToChatRoom">联系卖家</button>-->
<!--  <button @click="quitBuy" v-if="hasenrolled">退出购买</button>-->
<!--</template>-->

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
            else if(response.data.message === '成功参与'){
              message.info('成功参与')
              hasenrolled.value = true
            }else {
              message.info(response.data.message)
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