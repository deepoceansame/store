<template>
<div>
  <button @click="goToBuy">买</button>
  <button @click="goToSell">卖</button>
</div>
</template>

<script>
import {useRouter} from 'vue-router'
export default {
  name: "BuySellExchange",
  setup(){
    const router = useRouter()
    const goToBuy = () => {
      router.push("/goods/page=1&category=1&keyword=")
    }
    const goToSell = () => {
      router.push("/inquiry/page=1&category=1&keyword=")
    }
    return {
      goToBuy, goToSell
    }
  }
}
</script>

<style scoped>

</style>