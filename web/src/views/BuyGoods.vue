<template>
  <CategoryNav></CategoryNav>
  <br/>
  <search></search>
  <buy-nav></buy-nav>
  <div>buy-goods list here</div>
</template>

<script>
import BuyNav from "@/components/BuyNav";
import CategoryNav from "@/components/CategoryNav";
import Search from "@/components/Search";

export default {
  name: "BuyGoods",
  components: {BuyNav, CategoryNav, Search}
}
</script>

<style scoped>

</style>