<template>
  <tr class="row">
    <td><img :src="seller.avator" alt="头像"/></td>
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