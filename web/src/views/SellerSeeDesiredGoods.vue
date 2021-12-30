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
          <a-menu-item key="2">
          </a-menu-item>
          <a-menu-item key="3" >
          </a-menu-item>
        </a-menu>
      </a-layout-header>
    </a-layout>

    <h1 style="margin-left: 655px">
      <font size="75">求购信息</font>
    </h1>

    <div style="margin-left: 460px; margin-top: 15px">
      <table width="600" height="100" border=2 bgcolor= white>
        <tr align="center"><th width="150">求购名</th> <th width="150">价格</th> <th width="300">描述</th> </tr>
        <tr align="center">
          <td>{{desiredgoods.name}}</td>
          <td>{{desiredgoods.price}}</td>
          <td>{{desiredgoods.description}}</td>
        </tr>
      </table>
    </div>

    <h1 style="margin-left: 655px;  margin-top: 15px">
      <font size="75">求购图片</font>
    </h1>
    <div style="margin-left: 700px">
      <img v-for="(img, index) in desiredgoodsimgs"
           :src="'http://127.0.0.1:8083/'+img"
           :key="index" alt="图片没了"
           style="max-height: 100px"/>
    </div>


    <h1 style="margin-left: 635px; margin-top: 30px">
      <font size="75">求购者信息</font>
    </h1>

    <div style="margin-left: 390px; margin-top: 15px">
      <table width="720" height="100" border=2 bgcolor= white>
        <tr align="center"><th width="120">头像</th> <th width="60">id</th> <th width="180">邮箱</th><th width="120">qq</th><th width="120">昵称</th><th width="120">地址</th> </tr>
        <tr align="center">
          <td><img :src="'http://127.0.0.1:8083/' + buyerAccount.avator" alt="无头像" style="max-height: 50px"/></td>
          <td>{{buyerAccount.id}}</td>
          <td>{{buyerAccount.mail}}</td>
          <td>{{buyerAccount.qq}}</td>
          <td>{{buyerAccount.name}}</td>
          <td>{{buyerAccount.recvaddress}}</td>
        </tr>

      </table>
    </div>
    <div style="margin-left: 650px; margin-top: 25px">
      <a-button style="margin-right: 30px" type="primary" @click="enroll" v-if="!hasenrolled">参与提供</a-button>
      <a-button @click="goToChatRoom">联系求购者</a-button>
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

<!--  <div>-->
<!--    <h2>求购信息</h2>-->
<!--    <table border=2>-->
<!--      <tr>-->
<!--        <th>求购名</th> <th>价格</th>-->
<!--      </tr>-->
<!--      <tr>-->
<!--        <td>{{desiredgoods.name}}</td>-->
<!--        <td>{{desiredgoods.price}}</td>-->
<!--      </tr>-->
<!--    </table>-->
<!--  </div>-->

<!--  <div id="des">-->
<!--    <h2>求购描述</h2>-->
<!--    <p>-->
<!--      {{desiredgoods.description}}-->
<!--    </p>-->
<!--  </div>-->

<!--  <div>-->
<!--    <h2>求购者信息</h2>-->
<!--    <table border=2>-->
<!--      <tr>-->
<!--        <th>头像</th> <th>id</th> <th>邮箱</th> <th>qq</th> <th>昵称</th> <th>收货地址</th>-->
<!--      </tr>-->
<!--      <tr>-->
<!--        <td><img :src="'http://127.0.0.1:8083/' + buyerAccount.avator" alt="无头像" style="max-height: 50px"/></td>-->
<!--        <td>{{buyerAccount.id}}</td>-->
<!--        <td>{{buyerAccount.mail}}</td>-->
<!--        <td>{{buyerAccount.qq}}</td>-->
<!--        <td>{{buyerAccount.name}}</td>-->
<!--        <td>{{buyerAccount.recvaddress}}</td>-->
<!--      </tr>-->
<!--    </table>-->
<!--  </div>-->

<!--  <div>-->
<!--    <h2>求购图片</h2>-->
<!--    <img v-for="(img, index) in desiredgoodsimgs"-->
<!--         :src="'http://127.0.0.1:8083/'+img"-->
<!--         :key="index" alt="图片没了"-->
<!--         style="max-height: 100px"/>-->
<!--  </div>-->
<!--  <button @click="enroll">参与提供</button>-->
<!--  <button @click="goToChatRoom">联系求购者</button>-->
<!--</template>-->

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
    const desiredgoods = ref({});
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
                    buyerAccount.recvaddress = data.recvaddress
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
              message.info(response.data.message)
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
  #des{
    border-width: 2px;
    border-style: solid;
  }
</style>