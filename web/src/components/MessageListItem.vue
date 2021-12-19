<template>
  <div>
    <div v-if="Number(mess.type)===1&&mess.temp===undefined">
      {{mess.content}} 靠右{{atRight}}
    </div>
    <div v-if="Number(mess.type)===2&&mess.temp===undefined">
      <img  :src="'http://127.0.0.1:8083/'+mess.content" style="max-width: 150px">
      靠右{{atRight}}
    </div>
    <div v-if="mess.temp===true">
      <img  :src=mess.content style="max-width: 150px"> 靠右{{atRight}}
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

</style>