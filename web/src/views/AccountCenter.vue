<template>
  <div class="page">
    <a-layout style="margin-bottom: 20px">
      <a-layout-header class="header">
        <div class="logo" />
        <a-menu
            theme="dark"
            mode="horizontal"
            :default-selected-keys="['2']"
            :style="{ lineHeight: '64px' }"
        >
          <a-menu-item style="margin-left: 650px; font-weight: bold" key="1" >
            Sustech Store
          </a-menu-item>
        </a-menu>
      </a-layout-header>
    </a-layout>

    <a-descriptions style="padding-top: 25px" title="用户信息" align="center" class="ad" bordered>
      <a-descriptions-item label="id">
        {{account.id}}
      </a-descriptions-item>
      <a-descriptions-item label="邮箱">
        {{account.mail}}
      </a-descriptions-item>
      <a-descriptions-item label="QQ">
        {{account.qq}}
      </a-descriptions-item>
      <a-descriptions-item label="余额">
        {{balance}}
      </a-descriptions-item>
      <a-descriptions-item label="收货地址" :span="2">
        {{account.recvaddress}}
      </a-descriptions-item>
      <a-descriptions-item label="头像" :span="6">
        <div>
          <img :src="'http://127.0.0.1:8083/' + account.avator" alt="无头像" style="max-height: 150px"/>
        </div>
      </a-descriptions-item>
      <a-descriptions-item label="信誉积分">
        {{account.creditPoint}}
      </a-descriptions-item >
      <a-descriptions-item label="信誉状态" :span="3">
        <a-badge status="processing" text="正常" v-if="account.creditPoint>0"/>
        <a-badge status="error" text="信誉分不足" v-if="account.creditPoint<=0"/>
      </a-descriptions-item>
      <a-descriptions-item label="充值">
        <input style="width: 60px" v-model="chargeMoney"/><button style="margin-left: 10px" @click="charge">充值</button>
      </a-descriptions-item >
      <a-descriptions-item label="提交头像">
        <div style="margin-left: 1px">
          <input type="file" :value="imgToSubmit" name="image" accept="image/png, image/jpeg" @change="onFileChange"/>
          <button @click="submitImage">提交头像</button>
        </div>
      </a-descriptions-item >
    </a-descriptions>

    <!--  <button @click="clk">ckj</button>-->
    <br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>
  </div>

</template>

<style>
.ad{
  background-color: white;
  width: 60%;
  margin: auto;
}

</style>


<script>
import axios from "axios";
import {computed, onMounted, reactive, ref} from "vue";
import {useRouter, useRoute} from 'vue-router'
import FormData from "form-data";
import {message} from "ant-design-vue";

export default {
  name: "AccountCenter",
  setup(){
    const account = ref({})
    let imgToSubmit = null
    let tempImage = ref('')
    const route = useRoute()
    const router = useRouter()
    const chargeMoney = ref('')
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
        axios.post("account/updateAvatar", fd, {
          headers:{
            'Content-Type': `multipart/form-data`
          }
        }).then((response) => {
          console.log(response);
        })
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

    const balance = computed(()=>{
      if (account.value.money == null) {
        return 0
      }
      else{
        return account.value.money
      }
    })

    const clk = () => {
      console.log(tempImage.value)
      console.log(imgToSubmit)
    }

    const charge = () => {
      let cm = chargeMoney.value.trim()
      const intRegx = /^[1-9]\d{0,8}$/
      const floatRegex = /^[1-9]\d{0,8}\.\d{0,6}$/
      const floatRegex2 = /^0\.\d{0,6}$/
      if (cm === '') {
        message.info('输入充值额度')
      } else if (!(intRegx.test(cm) || floatRegex.test(cm) || floatRegex2.test(cm))) {
        message.info("不可接受的充值额度");
      } else {
        const fd = new FormData();
        fd.append('accountId', Number(route.params.accountid))
        fd.append('chargeAmount', cm)
        axios.post("account/chargeMoney", fd).then((response) => {
          if (response.data.success){
            message.info("充值成功")
          }
        })
        chargeMoney.value = ''
      }
    }

    return{
      onFileChange,
      submitImage,
      tempImage,
      imgToSubmit,
      clk,
      showTempImage,
      account,
      balance,
      chargeMoney,
      charge
    }
  }
}
</script>

<style scoped>

</style>