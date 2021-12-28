<template>
  <message-list-item v-for="(mess, index) in messageList" :mess="mess" :key="index"/>
  <button @click="getMessage">getMessage</button>
  <br/>
  <input v-model="tt"/>{{tt}}<button @click="submitText">提交</button>
  <br/>
  <input type="file" :value="imgToSubmit" name="image" accept="image/png, image/jpeg" @change="onFileChange"/>
  <button @click="submitImage">提交图片</button>
  <br/><button @click="showTransferPanel">给对方转账</button>
  <a-modal
      v-model:visible="trans_visible"
      title="输入金额 然后点击转账"
      :ok-button-props="{ disabled: false }"
      :cancel-button-props="{ disabled: false }"
      @ok="handleTransfer"
  >
    <template #footer>
      <a-button key="back" @click="handleCancel">取消</a-button>
      <a-button key="submit" type="primary" :loading="loading" @click="handleTransfer">转账</a-button>
    </template>
    <input v-model="transferAmount"/>
  </a-modal>
<!--  {{messageList}}-->
</template>

<script>
import {reactive, ref} from "vue";
import {useRoute} from 'vue-router'
import axios from "axios";
import MessageListItem from "@/components/MessageListItem";
import FormData from "form-data";
import {message} from "ant-design-vue";
export default {
  name: "Chatroom",
  components:{MessageListItem},
  setup(){
    var messageList = ref([]);
    const route = useRoute();
    const trans_visible = ref(false)
    const loading = ref(false)
    var tt = ref('')
    const transferAmount = ref('')
    let imgToSubmit = null
    let tempImage = ''
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
      console.log(tt.value);
      if(tt.value.trim().length !== 0){
        console.log(tt.value)
        addTxtContent(tt.value);
        messageList.value.push({
          goodsid: route.params.goodsid,
          senderid: route.params.senderid,
          receiverid: route.params.receiverid,
          type: 1,
          content: tt.value
        });
      }
      tt.value = '';
    }

    const submitImage = ()=>{
      if(tempImage!==null && tempImage.trim().length !== 0) {
        messageList.value.push({
          goodsid: route.params.goodsid,
          senderid: route.params.senderid,
          receiverid: route.params.receiverid,
          type: 2,
          content: tempImage,
          temp: true
        });
      }

      if(imgToSubmit!==null){
        let mess = {};
        mess.goodsid = route.params.goodsid;
        mess.senderid = route.params.senderid;
        mess.receiverid = route.params.receiverid;
        mess.type = 2;
        const fd = new FormData();
        fd.append('message', JSON.stringify(mess))
        fd.append('img', imgToSubmit, imgToSubmit.name)
        axios.post("message/addpicmessage", fd, {
          headers:{
            'Content-Type': `multipart/form-data`
          }
        }).then((response) => {
          console.log(response);
        })
        imgToSubmit = null
        tempImage = ''
      }
    }

    const onFileChange = (e)=> {
      console.log(e.target.files.length )
      if (e.target.files.length > 0) {
        imgToSubmit = e.target.files[0]
        let reader = new FileReader()
        reader.onload = (e) => {
          console.log(e)
          tempImage = e.target.result
        }
        reader.readAsDataURL(imgToSubmit)
      }
      else{
       tempImage = '';
       imgToSubmit = null;
      }
      console.log(tempImage)
    }

    const showTransferPanel = () =>{
      trans_visible.value = true
    }

    const handleTransfer = () => {
      loading.value = true
      let ta = transferAmount.value.trim()
      const intRegx = /^[1-9]\d{0,8}$/
      const floatRegex = /^[1-9]\d{0,8}\.\d{0,6}$/
      const floatRegex2 = /^0\.\d{0,6}$/
      if (ta === '') {
        message.info('输入转账金额')
        loading.value = false
      } else if (!(intRegx.test(ta) || floatRegex.test(ta) || floatRegex2.test(ta))) {
        message.info("不可接受的转账额度");
        loading.value = false
      } else {
        const fd = new FormData();
        fd.append('senderid', Number(route.params.senderid))
        fd.append('receiverid', Number(route.params.receiverid))
        fd.append('amount', transferAmount.value)
        axios.post("account/transferMoney", fd).then((response) => {
           message.info(response.data.message)
        })
        transferAmount.value = ''
        loading.value = false
      }
    }

    const handleCancel = () => {
      trans_visible.value = false
    }

    return {
      tt,
      getMessage,
      submitText,
      messageList,
      onFileChange,
      submitImage,
      imgToSubmit,
      showTransferPanel,
      trans_visible,
      transferAmount,
      handleTransfer,
      handleCancel,
      loading
    }
  }
}
</script>

<style scoped>

</style>