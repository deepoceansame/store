<template>

  <div>
    <h2>求购信息</h2>
    <table border=2>
      <tr>
        <th>求购名</th> <th>价格</th>
      </tr>
      <tr>
        <td>{{desiredgoods.name}}</td>
        <td>{{desiredgoods.price}}</td>
      </tr>
    </table>
  </div>

  <div id="des">
    <h2>求购描述</h2>
    <p>
      {{desiredgoods.description}}
    </p>
  </div>

  <div>
    <h2>求购者信息</h2>
    <table border=2>
      <tr>
        <th>头像</th> <th>id</th> <th>邮箱</th> <th>qq</th> <th>昵称</th> <th>收货地址</th>
      </tr>
      <tr>
        <td><img :src="'http://127.0.0.1:8083/' + buyerAccount.avator" alt="无头像" style="max-height: 50px"/></td>
        <td>{{buyerAccount.id}}</td>
        <td>{{buyerAccount.mail}}</td>
        <td>{{buyerAccount.qq}}</td>
        <td>{{buyerAccount.name}}</td>
        <td>{{buyerAccount.recvaddress}}</td>
      </tr>
    </table>
  </div>

  <div>
    <h2>求购图片</h2>
    <img v-for="(img, index) in desiredgoodsimgs"
         :src="'http://127.0.0.1:8083/'+img"
         :key="index" alt="图片没了"
         style="max-height: 100px"/>
  </div>
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