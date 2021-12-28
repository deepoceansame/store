<template>
  <div>
    <label>商品名: {{goods.name}}</label><br/>
    <label>价格: {{goods.price}}</label><br/>
    <label>描述: {{goods.description}}</label>
  </div>
  <br/>
  <div>
    <img v-for="(img, index) in goodsimgs" :key="index" :src="'http://127.0.0.1:8083/' + img" style="max-height: 150px" alt="图片"/>
  </div>
  <button @click="shutGoods">关闭售卖</button>
  <table border=2>
    <tr v-if="buyerList.length>0"><th>头像</th> <th>id</th> <th>邮箱</th> <th>qq</th> <th>昵称</th> <th>收货地址</th></tr>
    <buyer-table-row v-for="(buyer, index) in buyerList" :buyer="buyer" :key="index" :seller="account"></buyer-table-row>
  </table>
</template>

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