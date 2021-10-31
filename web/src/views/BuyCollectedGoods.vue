<template>
  <div>
    <category-nav></category-nav>
    <br/>
    <search/>
    <buy-nav></buy-nav>
    buy-collected-goods list here
  </div>
</template>

<script>
import BuyNav from "@/components/BuyNav";
import CategoryNav from "@/components/CategoryNav";
import Search from "@/components/Search";
export default {
  name: "BuyCollectedGoods",
  components: {
    Search,
    BuyNav, CategoryNav
  }
}
</script>

<style scoped>

</style>