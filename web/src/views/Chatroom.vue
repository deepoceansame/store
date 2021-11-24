<template>
  <button @click="getMessage">getMessage</button>
  <br/>
  <input v-model="tt"/>{{tt}}<button @click="submitText">提交</button>
  <br/>
  <button>给对方转账</button>
  <message-list-item v-for="(mess, index) in messageList" :mess="mess" :key="index"/>
  {{messageList}}
</template>

<script>
import {reactive, ref} from "vue";
import {useRoute} from 'vue-router'
import axios from "axios";
import MessageListItem from "@/components/MessageListItem";
export default {
  name: "Chatroom",
  components:{MessageListItem},
  setup(){
    var messageList = ref([]);
    const route = useRoute();
    var tt = ref('')

    const getMessage =  () => {
      console.log(route.params)
      axios.get('/message/getmessage',{
        params:{
          goodsid: route.params.goodsid,
          senderid: route.params.senderid,
          receiverid: route.params.receiverid
        }
      }).then(
          function (response){
            console.log(response)
            messageList.value = response.data.content
          }
      )
    }

    const addTxtContent = (content) => {
      axios.post('/message/addtextmessage', {
          goodsid: route.params.goodsid,
          senderid: route.params.senderid,
          receiverid: route.params.receiverid,
          type: 1,
          content: content
      }).then(
          (response) => {
            console.log(response)
          }
      )
    };

    const submitText = ()=>{
      console.log(tt);
      addTxtContent(tt.value);
      messageList.value.push({
        goodsid: route.params.goodsid,
        senderid: route.params.senderid,
        receiverid: route.params.receiverid,
        type: 1,
        content: tt.value
      });
      tt.value = '';
    }

    return {
      tt,
      getMessage,
      submitText,
      messageList,
    }
  }
}
</script>

<style scoped>

</style>