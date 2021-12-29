<template>
  <message-list-item v-for="(mess, index) in messageList" :mess="mess" :key="index"/>
  <button @click="getMessage">进入聊天室</button>
  <br/>
  <input v-model="tt"/>{{tt}}<button @click="submitText">提交</button>
  <br/>
  <input type="file" :value="imgToSubmit" name="image" accept="image/png, image/jpeg" @change="onFileChange"/>
  <button @click="submitImage">提交图片</button>


  <br/><button @click="showPanel">给对方转账</button>
  <a-modal
      v-model:visible="visible"
      title="输入金额 然后点击转账"
      :ok-button-props="{ disabled: false }"
      :cancel-button-props="{ disabled: false }"
      @ok="handleTrans"
  >
    <template #footer>
      <a-button key="back" @click="handleCan">取消</a-button>
      <a-button key="submit" type="primary" :loading="loading" @click="handleTrans">转账</a-button>
    </template>
    <input v-model="transferAmount"/>
  </a-modal>


  <br/><button v-if="!isSeller" @click="showTransferPanel">写订单给卖家</button>
  <br/><button @click="showPurchaseRecord">查看订单</button>
  <a-modal
      v-model:visible="trans_visible"
      title="填写订单"
      :ok-button-props="{ disabled: false }"
      :cancel-button-props="{ disabled: false }"
      @ok="handleTransfer"
  >

    <template #footer>
      <a-button key="back" @click="handleCancel">取消</a-button>
      <a-button key="submit" type="primary" :loading="loading" @click="handleTransfer">提交订单</a-button>
    </template>
    <p>选择交易时间</p>
    <input v-model="transferDate" ref="date" id="date" type="datetime-local"/>
    <br><br>
    <p>选择交易地点</p>
    <select v-model="transferLocation" name="location">
      <option value="一教">一教</option>
      <option value="二教">二教</option>
      <option value="荔园1栋">荔园1栋</option>
      <option value="荔园2栋">荔园2栋</option>
      <option value="荔园3栋">荔园3栋</option>
      <option value="荔园4栋">荔园4栋</option>
      <option value="荔园5栋">荔园5栋</option>
      <option value="荔园6栋">荔园6栋</option>
      <option value="欣园">欣园</option>
      <option value="湖畔1栋">湖畔1栋</option>
      <option value="湖畔2栋">湖畔2栋</option>
      <option value="湖畔3栋">湖畔3栋</option>
      <option value="湖畔4栋">湖畔4栋</option>
      <option value="湖畔5栋">湖畔5栋</option>
      <option value="11栋">11栋</option>
      <option value="润扬体育馆">润扬体育馆</option>
    </select>
    <br>
    <br>
    <p>选择转账金额</p>
    <input v-model="transferAmount"/>
  </a-modal>

  <a-modal
      v-model:visible="purRec_visible"
      title="订单详情"
      :ok-button-props="{ disabled: false }"
      :cancel-button-props="{ disabled: false }"
      @ok="closePurRec">

    <p v-if="isExistPurRec">买家： {{purchaseRec.buyerName}}</p>
    <br v-if="isExistPurRec">
    <p v-if="isExistPurRec">卖家： {{purchaseRec.sellerName}}</p>
    <br v-if="isExistPurRec">
    <p v-if="isExistPurRec">商品： {{purchaseRec.goodsName}}</p>
    <br v-if="isExistPurRec">
    <p v-if="isExistPurRec">交易时间： {{purchaseRec.tradingDate}}</p>
    <br v-if="isExistPurRec">
    <p v-if="isExistPurRec">交易地点： {{purchaseRec.tradingLocation}}</p>
    <br v-if="isExistPurRec">
    <p v-if="isExistPurRec">支付金额： {{purchaseRec.payment}}</p>
    <template v-if="isExistPurRec" #footer>
      <a-button v-if="!isSeller" key="back" type="primary" @click="closePurRec">确认</a-button>
      <a-button v-if="isSeller" key="submit" type="danger" :loading="loading" @click="cancelPurRec">取消订单，退出交易</a-button>
      <a-button v-if="isSeller" key="submit" type="primary" :loading="loading" @click="confirmPurRec">确认订单，完成交易</a-button>
    </template>

    <a-div v-if="!isExistPurRec">
      <p>当前没有订单！</p>
    </a-div>

  </a-modal>

<!--  {{messageList}}-->
</template>

<script>
import {onBeforeUnmount, onMounted, reactive, ref} from "vue";
import {useRoute} from 'vue-router'
import axios from "axios";
import MessageListItem from "@/components/MessageListItem";
import FormData from "form-data";
import {message} from "ant-design-vue";

export default {
  name: "Chatroom",
  components:{MessageListItem},
  setup(){
    const isSeller = ref(false)
    const isExistPurRec = ref(false)
    var messageList = ref([]);
    const route = useRoute();
    const trans_visible = ref(false)
    const purRec_visible = ref(false)
    let showMessage = ref(false)
    const loading = ref(false)
    let timer = undefined
    var tt = ref('')
    const transferAmount = ref('')
    const transferDate = ref('')
    const transferLocation = ref('')
    const  visible = ref(false)
    const purchaseRec = reactive({
      buyerName: '',
      goodsName: '',
      sellerName:'',
      effective: false,
      tradingDate: undefined,
      tradingLocation: '',
      payment:undefined
    });
    let imgToSubmit = null
    let tempImage = ''
    const getMessage =  () => {
      console.log(route.params)
      showMessage.value = true
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
        addTxtContent(tt.value)
        showMessage.value = true
      }
      tt.value = '';
    }

    const submitImage = ()=>{
      if(tempImage!==null && tempImage.trim().length !== 0) {
        showMessage.value = true
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

    const confirmPurRec = () =>{
      axios.get("account/confirmPurchaseRecord/",{
        params:{
          buyerId: route.params.receiverid,
          goodsId: route.params.goodsid
        }
      }).then(
          (response) =>  {
            const data = response.data
            isExistPurRec.value = false
            message.info(response.data.message)
          }
      );
    }

    const cancelPurRec = () =>{
      axios.get("account/cancelPurRec/",{
        params:{
          buyerId: route.params.receiverid,
          goodsId: route.params.goodsid
        }
      }).then(
          (response) =>  {
            const data = response.data
            if(data.success){
              isExistPurRec.value = false
            }
            message.info(response.data.message)
          }
      );
    }

    const init = () => {
      axios.get("/account/getAccountByGoodsId/"+route.params.goodsid).then(
          (response) => {
            const data = Number(response.data.content)
            isSeller.value = (data === Number(route.params.senderid))
            axios.get("/goods/getPurchaseRecord/", {
              params:{
                buyerId: isSeller.value ? route.params.receiverid : route.params.senderid,
                goodsId: route.params.goodsid
              }
            }).then(
                (response) => {
                  const data = response.data.content
                  if(data !== null){
                    isExistPurRec.value = (data.effective === false && data.tradingLocation !== null);
                  }else {
                    isExistPurRec.value = false
                  }
                }
            )
          },
      )
    }

    const showPurchaseRecord = () =>{
      init()
      axios.get("/goods/getPurchaseRecord/",{
        params:{
          buyerId: isSeller.value ? route.params.receiverid : route.params.senderid,
          goodsId: route.params.goodsid
        }
      }).then(
          (response) =>  {
            const data = response.data.content
            purchaseRec.tradingDate = dateFormat(data.tradingDate)
            purchaseRec.tradingLocation = data.tradingLocation
            purchaseRec.payment = data.payment
            axios.get("/account/getbyid/"+(isSeller.value ? route.params.receiverid : route.params.senderid)).then(
                (response) => {
                  const data = response.data.content
                  purchaseRec.buyerName = data.name
                }
            )
            axios.get("/account/getbyid/"+(isSeller.value ? route.params.senderid : route.params.receiverid)).then(
                (response) => {
                  const data = response.data.content
                  purchaseRec.sellerName = data.name
                }
            )
            axios.get("/goods/getbyid/"+route.params.goodsid).then(
                (response) => {
                  const data = response.data.content
                  purchaseRec.goodsName = data.name
                }
            )
          }
      );
      purRec_visible.value = true
    }

    const closePurRec = () =>{
      purRec_visible.value = false
    }

    const handleTransfer = () => {
      loading.value = true
      let ta = transferAmount.value.trim()
      let date = transferDate.value
      let location = transferLocation.value
      const intRegx = /^[1-9]\d{0,8}$/
      const floatRegex = /^[1-9]\d{0,8}\.\d{0,6}$/
      const floatRegex2 = /^0\.\d{0,6}$/
      if (date === ''){
        message.info('输入交易日期')
        loading.value = false
      } else if(location ===''){
        message.info('输入交易地点')
        loading.value = false
      } else if (ta === '') {
        message.info('输入转账金额')
        loading.value = false
      } else if (!(intRegx.test(ta) || floatRegex.test(ta) || floatRegex2.test(ta))) {
        message.info("不可接受的转账额度");
        loading.value = false
      } else {
        axios.get("/account/getMoney/" + route.params.senderid,).then(
            (response) =>  {
              const data = response.data.content
              if (data < transferAmount.value){
                message.info("余额不足，请先充值")
                loading.value = false
              }else {
                const fd = new FormData();
                fd.append('goodsId', Number(route.params.goodsid))
                fd.append('buyerId', Number(route.params.senderid))
                fd.append('tradingDate', Date(transferDate))
                fd.append('tradingLocation', transferLocation.value)
                fd.append('payment', transferAmount.value)
                axios.post("goods/submitPurchaseForm", fd).then((response) => {
                  message.info(response.data.message)
                })
                transferAmount.value = ''
                isExistPurRec.value = true
                loading.value = false
                trans_visible.value = false
              }
            }
        );
      }
    }

    const handleCancel = () => {
      trans_visible.value = false
    }

    const handleTrans = () => {
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

    const handleCan = () => {
      visible.value = false
    }

    const showPanel = () => {
      visible.value = true
    }



    onMounted(
        () => {
          init();
          //定时器
          timer = setInterval(() => {
            if(showMessage.value){
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
          }, 1000)
        }
    )

    onBeforeUnmount(()=>{
      clearInterval(timer)
    }
    )



    return {
      tt,
      isSeller,
      isExistPurRec,
      getMessage,
      submitText,
      messageList,
      onFileChange,
      submitImage,
      imgToSubmit,
      showTransferPanel,
      showPurchaseRecord,
      closePurRec,
      timer,
      confirmPurRec,
      cancelPurRec,
      trans_visible,
      purRec_visible,
      transferAmount,
      transferDate,
      transferLocation,
      purchaseRec,
      handleTransfer,
      handleCancel,
      loading,
      handleTrans,
      handleCan,
      visible,
      showPanel
    }
  },

  beforeUpdate(){
    this.$nextTick(()=>{
      var date = document.getElementById("date")
      date.setAttribute("min", getDate())
    })
  },

}


function dateFormat (time) {
  var date = new Date(time);
  var year = date.getFullYear();
  /* 在日期格式中，月份是从0开始的，因此要加0
   * 使用三元表达式在小于10的前面加0，以达到格式统一  如 09:11:05
   * */
  var month =
      date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
  var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
  var hours = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
  var minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
  var seconds = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
  // 拼接
  return year + "-" + month + "-" + day + " " + hours + ":" + minutes + ":" + seconds;
}

function getDate() {
  var today = new Date();//返回当前日期和时间
  var yyyy = today.getFullYear();//获取当前年份
  var MM = today.getMonth() + 1;//因为getMonth()方法获取的是索引值，获取的月份为0-11，所以要＋1
  var dd = today.getDate();//从 Date 对象返回一个月中的某一天 (1 ~ 31）
  var hh = today.getHours();//返回 Date 对象的小时 (0 ~ 23)
  var mm = today.getMinutes();//返回 Date 对象的分钟 (0 ~ 59)
  MM = checkTime(MM);//调用下面的checkTime函数，设置小于10的时间数字格式，例如5秒显示成05秒
  dd = checkTime(dd);
  hh = checkTime(hh);
  mm = checkTime(mm);
  return yyyy + "-" + MM + "-" + dd + "T" + hh + ":" + mm

  function checkTime(i) {
    if (i < 10) {
      i = "0" + i;//这里如果是一位数则在前面添加一位0
    }
    return i
  }
}
</script>

<style scoped>

</style>