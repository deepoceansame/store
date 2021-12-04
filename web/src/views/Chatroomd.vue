<template>
  <button @click="getMessage">getMessage</button>
  <br/>
  <input v-model="tt"/>{{tt}}<button @click="submitText">提交</button>
  <br/>
  <input type="file" :value="imgToSubmit" name="image" accept="image/png, image/jpeg" @change="onFileChange"/>
  <button @click="submitImage">提交图片</button>
  <br/><button>给对方转账</button>
  {{messageList}}
</template>

<script>
import {reactive, ref} from "vue";
import {useRoute} from 'vue-router'
import axios from "axios";
//import MessageListItemd from "@/components/MessageListItemd";
import FormData from "form-data";
import {message} from "ant-design-vue";
export default {
  name: "Chatroomd",
  //components:{MessageListItemd},
  setup(){
    var messageList = ref([]);
    const route = useRoute();
    var tt = ref('')
    let imgToSubmit = null
    let tempImage = ''
    const getMessage =  () => {
      console.log(route.params)
      axios.get('/messaged/getmessage',{
        params:{
          desiredgoodsid: route.params.desiredgoodsid,
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
      axios.post('/messaged/addtextmessage', {
        desiredgoodsid: route.params.desiredgoodsid,
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
          desiredgoodsid: route.params.desiredgoodsid,
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
          desiredgoodsid: route.params.goodsid,
          senderid: route.params.senderid,
          receiverid: route.params.receiverid,
          type: 2,
          content: tempImage,
          temp: true
        });
      }

      if(imgToSubmit!==null){
        let mess = {};
        mess.desiredgoodsid = route.params.desiredgoodsid;
        mess.senderid = route.params.senderid;
        mess.receiverid = route.params.receiverid;
        mess.type = 2;
        const fd = new FormData();
        fd.append('message', JSON.stringify(mess))
        fd.append('img', imgToSubmit, imgToSubmit.name)
        axios.post("messaged/addpicmessage", fd, {
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


    return {
      tt,
      getMessage,
      submitText,
      messageList,
      onFileChange,
      submitImage,
      imgToSubmit,
    }
  }
}
</script>

<style scoped>

</style>