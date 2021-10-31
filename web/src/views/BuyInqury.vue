<template>
  <category-nav></category-nav>
  <br/>
  <search></search>
  <buy-nav></buy-nav>
  <div>buy inqury list here</div>
</template>

<script>
import BuyNav from "@/components/BuyNav";
import CategoryNav from "@/components/CategoryNav";
import Search from "@/components/Search";
export default {
  name: "BuyInqury",
  components:{
    BuyNav,
    CategoryNav,
    Search,
  }
}
</script>

<style scoped>

</style>