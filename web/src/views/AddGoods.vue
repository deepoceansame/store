<template>
  <div>add goods here</div>

  <a-form
      ref="formRef"
      :model="formState"
      :rules="rules"
      :label-col="labelCol"
      :wrapper-col="wrapperCol"
      enctype="multipart/form-data"
  >
    <a-form-item ref="name" label="GoodsName" name="name">
      <a-input v-model:value="formState.name" />
    </a-form-item>

    <a-form-item label="category" name="category">
      <a-select v-model:value="formState.category">
        <a-select-option value="1">全部</a-select-option>
        <a-select-option value="2">人族</a-select-option>
        <a-select-option value="3">虫族</a-select-option>
        <a-select-option value="4">神族</a-select-option>
      </a-select>
    </a-form-item>

    <a-form-item label="price" name="price">
      <a-input v-model:value="formState.price" />
    </a-form-item>

    <a-form-item label="description" name="description">
      <a-textarea v-model:value="formState.description" :rows="4" />
    </a-form-item>

    <a-form-item label="image" name="image">
      <input type="file" name="image" accept="image/png, image/jpeg" @change="onFileChange" enctype="multipart/form-data"/>
      <img :src="showImage">
    </a-form-item>


    <a-form-item :wrapper-col="{ span: 14, offset: 4 }">
      <a-button type="primary" @click="onSubmit">Create</a-button>
      <a-button style="margin-left: 10px" @click="resetForm">Reset</a-button>
    </a-form-item>
  </a-form>

  <button @click="handleClick">Click</button>



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
      img: undefined,
    });
    const rules = {
      name: [{
        required: true,
        message: 'Please input Activity name',
        trigger: 'blur',
      }, {
        min: 0,
        max: 100,
        message: 'Length should be 0 to 100',
        trigger: 'blur',
      }],
      category: [{
        required: true,
        message: 'Please select Activity zone',
        trigger: 'change',
      }],
      price:[{
        required: true,
        trigger:'change',
      }],
      description:[{
        require: false,
        max: 200,
        trigger: 'change'
      }]
    };
    const showImage = ref();
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
        console.log('values', formState, toRaw(formState));
        goods.categoryId = formState.category;
        goods.name = formState.name;
        goods.price = formState.price;
        goods.accountId = store.state.account.id;
        goods.description = formState.description;
        const fd = new FormData();
        fd.append('goods', JSON.stringify(goods))
        fd.append('img', formState.image, formState.image.name);
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
      console.log(typeof (formState.image))
    }

    const onFileChange = (e)=>{
      console.log(e)
      formState.image = e.target.files[0]
      var reader = new FileReader()
      reader.onload = (e) => {
        console.log(e)
        showImage.value = e.target.result;
      }
      reader.readAsDataURL(formState.image)
      console.log(formState.image)
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
      showImage,
    };
  },

});
</script>

<style scoped>

</style>