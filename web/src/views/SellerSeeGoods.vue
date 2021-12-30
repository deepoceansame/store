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

    <h1 style="margin-left: 650px;font-size: 45px">
      商品信息
    </h1>

    <div style="margin-left: 415px; margin-top: 15px">
      <table width="750" height="100" border=2 bgcolor= white>
        <tr align="center"><th width="150">商品名</th> <th width="150">价格</th> <th width="300">描述</th> <th width="150">关闭售卖</th></tr>
        <tr align="center">
          <td>{{goods.name}}</td>
          <td>{{goods.price}}</td>
          <td>{{goods.description}}</td>
          <td>  <a-button type='danger' @click="shutGoods">关闭售卖</a-button></td>
        </tr>
      </table>
    </div>

    <h1 style="margin-left: 655px;  margin-top: 15px; font-size: 45px">
      商品图片
    </h1>
    <div style="margin-left: 700px">
      <img v-for="(img, index) in goodsimgs" :key="index" :src="'http://127.0.0.1:8083/' + img" style="max-height: 150px" alt="图片"/>
    </div>


    <h1 style="margin-left: 590px; margin-top: 50px; size: 50px">
      这些用户有意购买您的商品
    </h1>

    <div style="margin-left: 365px; margin-top: 15px">
      <table width="840" height="100" border=2 bgcolor= white>
        <tr v-if="buyerList.length>0" align="center"><th width="120">头像</th> <th width="60">id</th> <th width="180">邮箱</th><th width="120">qq</th><th width="120">昵称</th><th width="120">地址</th> <th width="120">联系</th></tr>
        <buyer-table-row v-for="(buyer, index) in buyerList" :buyer="buyer" :key="index" :seller="account"></buyer-table-row>
      </table>
    </div>

    <br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>
  </div>

</template>



<!--<template>-->
<!--  <div>-->
<!--    <label>商品名: {{goods.name}}</label><br/>-->
<!--    <label>价格: {{goods.price}}</label><br/>-->
<!--    <label>描述: {{goods.description}}</label>-->
<!--  </div>-->
<!--  <br/>-->
<!--  <div>-->
<!--    <img v-for="(img, index) in goodsimgs" :key="index" :src="'http://127.0.0.1:8083/' + img" style="max-height: 150px" alt="图片"/>-->
<!--  </div>-->
<!--  <button @click="shutGoods">关闭售卖</button>-->
<!--  <table border=2>-->
<!--    <tr v-if="buyerList.length>0"><th>头像</th> <th>id</th> <th>邮箱</th> <th>qq</th> <th>昵称</th> <th>收货地址</th></tr>-->
<!--    <buyer-table-row v-for="(buyer, index) in buyerList" :buyer="buyer" :key="index" :seller="account"></buyer-table-row>-->
<!--  </table>-->
<!--</template>-->

<script>
import axios from "axios";
import {computed, onMounted, reactive, ref} from "vue";
import {useRoute, useRouter}  from 'vue-router'
import store from "@/store";
import {message} from 'ant-design-vue';
import FormData from "form-data";
import BuyerTableRow from "@/components/BuyerTableRow";

export default {
  name: "BuyerSeeDesiredGoods",
  components: {BuyerTableRow},
  setup(){
    const route = useRoute();
    const router = useRouter()
    const goods = ref({})
    const goodsimgs = ref([])
    const buyerList = ref([])
    const account = computed(() => {
      return store.state.account
    })

    const handleQuery = (params) => {
      axios.get("goods/getbyid/"+route.params.goodsid).then(
          (response) =>  {
            const data = response.data
            if (data.success){
              goods.value = data.content;
            } else{
              const a=0
            }
          }
      )

      axios.get("goods/getimages", {
        params:{
          goodsid: route.params.goodsid
        }
      }).then(
          (response) => {
            goodsimgs.value = response.data.content
          }
      )

      axios.get("account/getbuyer",{
        params:{
          goodsId: route.params.goodsid
        }
      }).then(
          (response) =>{
            buyerList.value = response.data.content;
          }
      )
    };

    const shutGoods = ()=>{
      const fd = new FormData();
      fd.append('goodsId', route.params.goodsid)
      axios.post('goods/deletegoodsbypid', fd).then(
          (response) => {
            message.info("已删除")
            router.push('/mygoods/page=:page&category=:category&keyword=')
          }
      )
    }

    onMounted(
        () => {
          console.log('mounted')
          handleQuery({id: route.params.goodsid});
        }
    )

    return{
      goods,
      goodsimgs,
      shutGoods,
      buyerList,
      account
    }
  }
}
</script>

<style scoped>

</style>