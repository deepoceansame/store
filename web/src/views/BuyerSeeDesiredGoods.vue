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

    <h1 style="margin-left: 625px;font-size: 45px">
      求购商品信息
    </h1>

    <div style="margin-left: 415px; margin-top: 15px">
      <table width="750" height="100" border=2 bgcolor= white>
        <tr align="center"><th width="150">商品名</th> <th width="150">价格</th> <th width="300">描述</th> <th width="150">关闭求购</th></tr>
        <tr align="center">
          <td>{{desiredgoods.name}}</td>
          <td>{{desiredgoods.price}}</td>
          <td>{{desiredgoods.description}}</td>
          <td>  <a-button type='danger' @click="shutDesiredGoods">关闭求购</a-button></td>
        </tr>
      </table>
    </div>

    <h1 style="margin-left: 655px;  margin-top: 15px; font-size: 45px">
      商品图片
    </h1>
    <div style="margin-left: 700px">
      <img v-for="(img, index) in desiredgoodsimgs" :key="index" :src="'http://127.0.0.1:8083/' + img" style="max-height: 150px" alt="图片"/>
    </div>


    <h1 style="margin-left: 625px; margin-top: 50px; size: 50px">
      这些用户参与到您的求购
    </h1>

    <div style="margin-left: 415px; margin-top: 15px">
      <table width="720" height="100" border=2 bgcolor= white>
        <tr v-if="supplyerList.length>0" align="center"><th width="120">头像</th> <th width="60">id</th> <th width="180">邮箱</th><th width="120">qq</th><th width="120">昵称</th> <th width="120">联系</th></tr>
          <supplyer-table-row v-for="(seller, index) in supplyerList" :seller="seller" :key="index" :buyer="account"></supplyer-table-row>
      </table>
    </div>

    <br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>
  </div>

</template>


<!--<template>-->
<!--  <div>-->
<!--    <label>求购商品名: {{desiredgoods.name}}</label><br/>-->
<!--    <label>价格: {{desiredgoods.price}}</label><br/>-->
<!--    <label>描述: {{desiredgoods.description}}</label>-->
<!--  </div>-->
<!--  <br/>-->
<!--  <div>-->
<!--    <img v-for="(img, index) in desiredgoodsimgs" :key="index" :src="'http://127.0.0.1:8083/' + img" style="max-height: 150px" alt="图片"/>-->
<!--  </div>-->
<!--  <button @click="shutDesiredGoods">关闭求购</button>-->
<!--  <table border=2>-->
<!--    <tr v-if="supplyerList.length>0"> <th>头像</th> <th>id</th> <th>邮箱</th> <th>qq</th> <th>昵称</th></tr>-->
<!--    <supplyer-table-row v-for="(seller, index) in supplyerList" :seller="seller" :key="index" :buyer="account"></supplyer-table-row>-->
<!--  </table>-->
<!--</template>-->

<script>
import axios from "axios";
import {computed, onMounted, reactive, ref} from "vue";
import {useRoute, useRouter}  from 'vue-router'
import store from "@/store";
import {message} from 'ant-design-vue';
import FormData from "form-data";
import SupplyerTableRow from "@/components/SupplyerTableRow";

export default {
  name: "BuyerSeeDesiredGoods",
  components: {SupplyerTableRow},
  setup(){
    const route = useRoute();
    const router = useRouter()
    const desiredgoods = ref({})
    const desiredgoodsimgs = ref([])
    const supplyerList = ref([])
    const account = computed(() => {
      return store.state.account
    })

    const handleQuery = (params) => {
      axios.get("desiredGoods/getbyid/"+route.params.desiredgoodsid).then(
          (response) =>  {
            const data = response.data
            if (data.success){
              desiredgoods.value = data.content;
            } else{
              const a=0
            }
          }
      )

      axios.get("desiredGoods/getimages", {
        params:{
          desiredgoodsid: route.params.desiredgoodsid
        }
      }).then(
          (response) => {
            desiredgoodsimgs.value = response.data.content
          }
      )

      axios.get("account/getsupplyer",{
        params: {
          desiredGoodsId: route.params.desiredgoodsid
        }
      }).then(
          (response) =>{
            supplyerList.value = response.data.content;
          }
      )
    };

    onMounted(
        () => {
          console.log('mounted')
          handleQuery({id: route.params.goodsid});
        }
    )

    const shutDesiredGoods = ()=>{
      const fd = new FormData();
      fd.append('desiredgoodsId', route.params.desiredgoodsid)
      axios.post('desiredGoods/deleteinquirybypid', fd).then(
          (response) => {
            message.info("已删除")
            router.push('/myinquiry/page=:page&category=:category&keyword=')
          }
      )
    }

    return{
      desiredgoods,
      desiredgoodsimgs,
      shutDesiredGoods,
      supplyerList,
      account,
    }
  }
}
</script>

<style scoped>

</style>