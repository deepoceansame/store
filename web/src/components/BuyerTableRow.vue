<template>
  <tr class="row">
    <td><img :src="'http://127.0.0.1:8083/' + buyer.avator" alt="无头像" style="max-height: 50px"/></td>
    <td>{{buyer.id}}</td>
    <td>{{buyer.mail}}</td>
    <td>{{buyer.qq}}</td>
    <td>{{buyer.name}}</td>
    <td>{{buyer.recvaddress}}</td>
    <td><button @click="goToChatRoom">联系他</button> </td>
  </tr>
</template>

<script>
import {useRoute, useRouter}  from 'vue-router'

export default {
  name: "BuyerTableRow",
  props: ['seller', 'buyer'],
  setup(props){
    const route = useRoute()
    const router = useRouter()
    const printProps = () => {
      console.log(props.buyer.avator)
      console.log(props.seller.id)
    }

    const goToChatRoom = () => {
      let params = {goodsid: route.params.goodsid, senderid: props.seller.id, receiverid:props.buyer.id}
      router.push({name:'chatroom', params: params})
    }
    return {
      printProps,
      goToChatRoom
    }
  }
}
</script>

<style scoped>

</style>