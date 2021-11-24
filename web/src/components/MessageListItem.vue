<template>
  <div>
    {{mess.content}}   靠右{{atRight}}
  </div>
</template>

<script>
import {useRoute} from 'vue-router'

export default {
  name: "MessageListItem",
  props: ['mess'],
  setup(props){
    const route = useRoute();
    console.log(props.mess.senderid)
    console.log(Number(route.params.senderid))
    const atRight = Number(props.mess.senderid) === Number(route.params.senderid)
    console.log(atRight)
    return {
      atRight,
    }
  }
}
</script>

<style scoped>

</style>