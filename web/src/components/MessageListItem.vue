<template>
  <div>
    <div v-if="Number(mess.type)===1&&mess.temp===undefined" :class="[{right: atRight}]">
      {{mess.content}}
    </div>
    <div v-if="Number(mess.type)===2&&mess.temp===undefined" :class="[{right: atRight}]">
      <img  :src="'http://127.0.0.1:8083/'+mess.content" style="max-width: 150px">
    </div>
    <div v-if="mess.temp===true" :class="[{right: atRight}]">
      <img  :src=mess.content style="max-width: 150px">
    </div>
  </div>
</template>

<script>
import {useRoute} from 'vue-router'
import {computed, ref} from "vue";
import store from "@/store";

export default {
  name: "MessageListItem",
  props: ['mess'],
  setup(props){
    const route = useRoute();
    console.log(props.mess.senderid)
    console.log(Number(route.params.senderid))
    const atRight = computed(() => {
      return Number(props.mess.senderid) === Number(route.params.senderid)
    })
    console.log(atRight)
    return {
      atRight,
    }
  }
}
</script>

<style scoped>
.right{
  color: brown;
  padding-left: 300px;
}
</style>