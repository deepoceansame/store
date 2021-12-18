<template>
{{"account center here"}}

  <input type="file" :value="imgToSubmit" name="image" accept="image/png, image/jpeg" @change="onFileChange"/>
  <button @click="submitImage">提交图片</button>
  <img :src="tempImage"  v-if="showTempImage" alt="头像"/>

  <button @click="clk">ckj</button>
</template>

<script>
import axios from "axios";
import {computed, onMounted, reactive, ref} from "vue";
import {useRouter, useRoute} from 'vue-router'
import FormData from "form-data";

export default {
  name: "AccountCenter",
  setup(){
    const account = ref({})
    let imgToSubmit = null
    let tempImage = ref('')
    const route = useRoute()
    const router = useRouter()

    const handleQuery = () => {
      axios.get("/account/getbyid/"+route.params.accountid).then(
          (response) => {
            account.value = response.data.content
          }
      )
    }

    onMounted(
        () => {
          console.log('mounted')
          handleQuery();
        }
    )

    const submitImage = ()=>{

      if(imgToSubmit!==null){
        let mess = {};
        const fd = new FormData();
        fd.append('accountid', route.params.accountid)
        fd.append('img', imgToSubmit, imgToSubmit.name)
        // axios.post("message/addpicmessage", fd, {
        //   headers:{
        //     'Content-Type': `multipart/form-data`
        //   }
        // }).then((response) => {
        //   console.log(response);
        // })
        imgToSubmit = null
        tempImage.value = ''
      }
    }

    const onFileChange = (e)=> {
      console.log(e.target.files.length )
      if (e.target.files.length > 0) {
        imgToSubmit = e.target.files[0]
        let reader = new FileReader()
        reader.onload = (e) => {
          console.log(e)
          tempImage.value = e.target.result
        }
        reader.readAsDataURL(imgToSubmit)
      }
      else{
        tempImage.value = '';
        imgToSubmit = null;
      }
    }

    const showTempImage = computed(() => {
      return tempImage.value.trim().length !== 0
    })

    const clk = () => {
      console.log(tempImage.value)
      console.log(imgToSubmit)
    }

    return{
      onFileChange,
      submitImage,
      tempImage,
      imgToSubmit,
      clk,
      showTempImage,

    }
  }
}
</script>

<style scoped>

</style>