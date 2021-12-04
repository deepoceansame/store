<template>
  {{desiredgoods}}
  <br/>
  {{desiredgoodsimgs}}
</template>

<script>
import axios from "axios";
import {computed, onMounted, reactive, ref} from "vue";
import {useRoute, useRouter}  from 'vue-router'
import store from "@/store";
import {message} from 'ant-design-vue';

export default {
  name: "BuyerSeeDesiredGoods",
  setup(){
    const route = useRoute();
    const router = useRouter()
    const desiredgoods = ref()
    const desiredgoodsimgs = ref([])
    const account = computed(() => {
      return store.state.account
    })

    const handleQuery = (params) => {
      axios.get("desiredGoods/getbyid/"+route.params.desiredgoodsid).then(
          (response) =>  {
            const data = response.data
            if (data.success){
              desiredgoods.value = data.content;
            } else{
              const a=0
            }
          }
      )

      axios.get("desiredGoods/getimages", {
        params:{
          desiredgoodsid: route.params.desiredgoodsid
        }
      }).then(
          (response) => {
            desiredgoodsimgs.value = response.data.content
          }
      )
    };

    onMounted(
        () => {
          console.log('mounted')
          handleQuery({id: route.params.goodsid});
        }
    )

    return{
      desiredgoods,
      desiredgoodsimgs
    }
  }
}
</script>

<style scoped>

</style>