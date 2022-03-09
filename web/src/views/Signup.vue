<template>

  <br><br><br>
  <h1 style="margin-left: 575px;font-family: Arial, 'Hiragino Sans GB', 'Microsoft Yahei', 'Microsoft Sans Serif', 'WenQuanYi Micro Hei', sans-serif">
    <font size="100">Sustech Store</font>
  </h1>

  <a-form
      ref="formRef"
      name="custom-validation"
      :model="formState"
      :rules="rules"
      v-bind="layout"
  >

    <a-form-item style="margin-top: 30px; margin-left: 40px" has-feedback label="邮箱" name="mail">
      <a-input v-model:value="formState.mail" type="mail" autocomplete="off" />
    </a-form-item>
    <a-form-item style="margin-left: 40px" has-feedback label="用户名" name="name">
      <a-input v-model:value="formState.name" type="input" autocomplete="off" />
    </a-form-item>
    <a-form-item style="margin-left: 40px" has-feedback label="QQ" name="qq">
      <a-input v-model:value="formState.qq" type="input" autocomplete="off" />
    </a-form-item>
    <a-form-item style="margin-left: 40px" has-feedback label="密码" name="password">
      <a-input v-model:value="formState.password" type="password" autocomplete="off" />
    </a-form-item>
    <a-form-item style="margin-left: 40px" has-feedback label="确认密码" name="repPassword">
      <a-input v-model:value="formState.repPassword" type="password" autocomplete="off" />
    </a-form-item>
    <a-form-item style="margin-left: 40px" has-feedback label="收货地址" name="recvaddress">
      <a-input v-model:value="formState.recvaddress" type="input" autocomplete="off" />
    </a-form-item>



    <a-form-item style="margin-left: 40px" :wrapper-col="{ span: 14, offset: 9 }">
      <a-button type="primary" @click="onSubmit">提交</a-button>
      <a-button style="margin-left: 10px" @click="resetForm">重置</a-button>
      <a-button style="margin-left: 10px" @click="goback">返回</a-button>
    </a-form-item>
  </a-form>

<!--  <button @click="handleClick">click</button>-->
</template>

<script lang="ts">

import {reactive, ref, toRaw} from 'vue';
import axios from "axios";
import {message} from 'ant-design-vue';
import type { UnwrapRef } from 'vue';
import {Tool} from '@/util/tool';
import {useRouter} from "vue-router";
declare let hexMd5: any;
declare let KEY: any;
interface FormState {
  mail: string;
  name: string;
  qq: string;
  password: string;
  repPassword: string;
  recvaddress: string;
}

export default {
  name: "Signup",
  setup() {
    const router = useRouter();
    const formRef = ref();
    const formState : UnwrapRef<FormState> = reactive({
      mail:'',
      name:'',
      qq:'',
      password: '',
      repPassword: '',
      recvaddress:'',
    });

    let validatePass = async (_rule: any, value: string) => {
      if (value === '') {
        return Promise.reject('Please input the password');
      } else {
        if (formState.repPassword !== '') {
          const PASSWORD_REGEX_1=  /^[A-Za-z0-9]\w{5,}$/;
          if (!PASSWORD_REGEX_1.test(value)){
            return Promise.reject('六位以上');
          }
          formRef.value.validateFields('checkPass');
        }

        return Promise.resolve();
      }
    };

    let validatePass2 = async (_rule: any, value: string) => {
      if (value === '') {
        return Promise.reject('请确认密码');
      } else if (value !== formState.password) {
        return Promise.reject("两次输入密码不一致!");
      } else {
        return Promise.resolve();
      }
    };


    let validateMail = async (_rule: any, value: string) => {
      if (value === '') {
        return Promise.reject('请填写邮箱');
      } else {
        const MAIL_REGEX_1= /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        if (!MAIL_REGEX_1.test(value)) {
          return Promise.reject('邮箱格式错误')
        }
        return Promise.resolve();
      }
    };

    let validateQQ = async (_rule: any, value: string) => {
      if (value === '') {
        return Promise.reject('请填写QQ号');
      } else {
        const QQ_REGEX_1= /^\d{5,12}$/;
        if (!QQ_REGEX_1.test(value)) {
          return Promise.reject('QQ号格式错误')
        }
        return Promise.resolve();
      }
    };

    const handleClick = () => {
      console.log(formState.password)
      console.log( hexMd5(formState.password + KEY) )
    }

    const rules = {
      mail: [
        {
          required: true,
          validator: validateMail,
          trigger: 'blur',
        }
      ],
      name:[
        {
          required: true,
          trigger:'blur',
        }
      ],
      qq:[
        {
          required:true,
          validator: validateQQ,
          trigger:'blur'
        }
      ],
      password: [{
        required: true,
        validator: validatePass,
        trigger: 'blur',
      }],
      repPassword: [{
        validator: validatePass2,
        trigger: 'blur',
      }],

    };
    const layout = {
      labelCol: {
        span: 8,
      },
      wrapperCol: {
        span: 6,
      },
    };

    const onSubmit = () => {
      formRef.value.validate().then(() => {
        console.log('values', formState, toRaw(formState));
        var newaccount = {password: ''}
        Object.assign(newaccount, formState)
        newaccount.password = hexMd5(formState.password + KEY)
        axios.post("account/save", newaccount).then((response : any) => {
          console.log(response);
          if (response.data.success) {
            message.info('注册成功')
            router.push("/")
          } else {
            message.info('注册失败'+' '+response.data.message)
          }
        })
      }).catch((error: any) => {
        console.log('error', error);
      });
    }

    const resetForm = () => {
      formRef.value.resetFields();
    };

    const goback = () => {
      router.push('/')
    }
    return {
      formState,
      formRef,
      rules,
      layout,
      onSubmit,
      resetForm,
      handleClick,
      goback
    };
  },
}
</script>

<style scoped>

</style>