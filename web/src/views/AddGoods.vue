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

    <h1 style="margin-left: 670px;font-family: Arial, 'Hiragino Sans GB', 'Microsoft Yahei', 'Microsoft Sans Serif', 'WenQuanYi Micro Hei', sans-serif">
      <font size="80">添加商品</font>
    </h1>
    <a-form
        ref="formRef"
        :model="formState"
        :rules="rules"
        :label-col="labelCol"
        :wrapper-col="wrapperCol"
        enctype="multipart/form-data"
    >
      <a-form-item style="margin-left: 460px" ref="name" label="商品名称" name="name" >
        <a-input style="width: 300px;" v-model:value="formState.name" />
      </a-form-item>

      <a-form-item style="margin-left: 460px" label="类别" name="category">
        <a-select style="width: 300px" v-model:value="formState.category">
          <a-select-option value="2">生活用品</a-select-option>
          <a-select-option value="3">零食</a-select-option>
          <a-select-option value="4">电子</a-select-option>
          <a-select-option value="5">书籍</a-select-option>
          <a-select-option value="6">其它</a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item style="margin-left: 460px" label="价格" name="price">
        <a-input style="width: 300px" v-model:value="formState.price" />
      </a-form-item>

      <a-form-item style="margin-left: 460px" label="描述" name="description">
        <a-textarea style="width: 300px" v-model:value="formState.description" :rows="4" />
      </a-form-item>

      <a-form-item style="margin-left: 460px" label="图片" name="image">
        <input type="file" name="image" accept="image/png, image/jpeg" @change="onFileChange" multiple="multiple"/>
      </a-form-item>
      <img v-for="(imgurl, index) in showImages" :src="imgurl" :key="index" style="max-height: 150px" />

      <a-form-item style="margin-left: 545px" :wrapper-col="{ span: 14, offset: 4 }">
        <a-button type="primary" @click="onSubmit">提交</a-button>
        <a-button style="margin-left: 10px" @click="resetForm">重置</a-button>
      </a-form-item>
    </a-form>

    <br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>
  </div>

</template>




<script>
import { reactive, ref, toRaw } from 'vue';
import axios from "axios";
import {message} from 'ant-design-vue';
import FormData from 'form-data'
import store from "@/store";

export default ({
  name: "AddGoods",
  setup() {
    const formRef = ref();
    const formState = reactive({
      name: '',
      category: undefined,
      price: undefined,
      description: '',
      images: undefined,
    });

    let validatePrice = async (_rule, value) => {
      const intRegx = /^\d{0,8}$/
      const floatRegex = /^\d{0,8}\.\d{0,6}$/
      const floatRegex2 = /^0\.\d{0,6}$/
      if (value === '') {
        return Promise.reject('Please input price');
      } else if (!(intRegx.test(value) || floatRegex.test(value) || floatRegex2.test(value))) {
        return Promise.reject("bad price");
      } else {
        return Promise.resolve();
      }
    };


    const rules = {
      name: [{
        required: true,
        message: 'Please input goods name',
        trigger: 'blur',
      }, {
        min: 0,
        max: 100,
        message: 'Length should be 0 to 100',
        trigger: 'blur',
      }],
      category: [{
        required: true,
        message: 'Please select category',
        trigger: 'change',
      }],
      price:[{
        required: true,
        validator: validatePrice,
        trigger:'change',
      }],
      description:[{
        require: false,
        max: 200,
        trigger: 'change'
      }]
    };
    const showImages = ref([]);
    const goods = reactive({
      name:  undefined,
      categoryId: undefined,
      price: undefined,
      accountId: undefined,
      description:  undefined,
      img: undefined
    });

    const onSubmit = () => {
      formRef.value.validate().then(() => {
        if (formState.images == null){
          formState.images = []
        }
        console.log('values', formState, toRaw(formState));
        goods.categoryId = formState.category;
        goods.name = formState.name;
        goods.price = formState.price;
        goods.accountId = store.state.account.id;
        goods.description = formState.description;
        const fd = new FormData();
        fd.append('goods', JSON.stringify(goods))
        if(formState.images !== undefined){
          for(let i=0; i<formState.images.length; i++){
            fd.append('imgs', formState.images[i], formState.images[i].name);
          }
        }
        else{
          fd.append('imgs', undefined)
        }
        console.log(goods)
        axios.post("goods/save", fd, {
          headers:{
            'Content-Type': `multipart/form-data`
          }
        }).then((response) => {
          console.log(response);
          if (response.data.success){
            message.info('添加成功'+response.data.content)
          }
          else{
            message.info('添加失败')
          }
        })
      }).catch(error => {
        console.log('error', error);
      });
    };

    const resetForm = () => {
      formRef.value.resetFields();
    };


    const handleClick = ()=>{
      console.log(typeof (formState.images))
      console.log(formState.images)
    }


    const onFileChange = (e)=>{
      showImages.value = []
      if (e.target.files.length > 4){
        alert('too many images')
        formState.images = null
        return
      }
      console.log('====================')
      formState.images = e.target.files
      for(let i=0; i<formState.images.length; i++){
        let reader = new FileReader()
        reader.onload = (e) => {
          showImages.value.push(e.target.result);
        }
        let img = formState.images[i]
        reader.readAsDataURL(img)
      }
      console.log(formState.images)
    }

    return {
      formRef,
      labelCol: {
        span: 4,
      },
      wrapperCol: {
        span: 14,
      },
      other: '',
      formState,
      rules,
      onSubmit,
      resetForm,
      handleClick,
      onFileChange,
      showImages,
    };
  },

});
</script>

<style scoped>

</style>