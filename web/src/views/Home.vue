<template>
  <table>
    <GoodsTableRow v-for="(item, index) in goodsList.values()" :goods="item" :key="index"/>
  </table>
</template>
<script>
import {defineComponent, ref} from 'vue';
import GoodsTableRow from "@/components/GoodsTableRow";


export default defineComponent({
  name: "Home",
  components: {GoodsTableRow},
  setup(){
    const goodsList = ref([
      {
        name:'大橘子',
        price:12,
        accountname:'TTTTXY',
        info:'品质优秀 品质优秀 品质优秀 品质优秀 品质优秀 品质优秀 品质优秀 品质优秀' +
            '品质优秀'
      },
      {
        name:'大橘子',
        price:14,
        accountname:'TTTTXYjjjjjjji',
        info:'品质优秀 品质优秀 品质优秀 品质优秀 品质优秀 品质优秀 品质优秀 品质优秀' +
            '品质优秀'
      },
      {
        name:'大橘子',
        price:15,
        accountname:'TTTTXYLOPoods1',
        info:'品质优秀 品质优秀 品质优秀 品质优秀 品质优秀 品质优秀 品质优秀 品质优秀' +
            '品质优秀'
      }
    ]);

    return {
      goodsList
    }
  }
});
</script>

<style scoped>
.ant-avatar{
  height: 50px;
  width: 50px;
  line-height: 50px;
  border-radius: 8%;
  margin: 5px 0;
}
</style>