<template>
  <tr class="row" align="center">
    <td><img :src="'http://127.0.0.1:8083/' + seller.avator" alt="无头像" style="max-height: 50px"/></td>
    <td>{{seller.id}}</td>
    <td>{{seller.mail}}</td>
    <td>{{seller.qq}}</td>
    <td>{{seller.name}}</td>
    <td><button @click="goToChatRoom">联系他</button> </td>
  </tr>
</template>

<script>
import {useRoute, useRouter}  from 'vue-router'

export default {
  name: "SupplyerTableRow",
  props: ['buyer', 'seller'],
  setup(props){
    const route = useRoute()
    const router = useRouter()
    const printProps = () => {
      console.log(props.seller.avator)
      console.log(props.buyer.id)
    }

    const goToChatRoom = () => {
      let params = {desiredgoodsid: route.params.desiredgoodsid, senderid: props.buyer.id, receiverid:props.seller.id}
      router.push({name:'chatroomd', params: params})
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