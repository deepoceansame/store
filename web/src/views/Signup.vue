<template>
  <a-form
      ref="formRef"
      name="custom-validation"
      :model="formState"
      :rules="rules"
      v-bind="layout"
  >

    <a-form-item has-feedback label="mail" name="mail">
      <a-input v-model:value="formState.mail" type="mail" autocomplete="off" />
    </a-form-item>
    <a-form-item has-feedback label="name" name="name">
      <a-input v-model:value="formState.name" type="input" autocomplete="off" />
    </a-form-item>
    <a-form-item has-feedback label="qq" name="qq">
      <a-input v-model:value="formState.qq" type="input" autocomplete="off" />
    </a-form-item>
    <a-form-item has-feedback label="Password" name="password">
      <a-input v-model:value="formState.password" type="password" autocomplete="off" />
    </a-form-item>
    <a-form-item has-feedback label="Confirm" name="repPassword">
      <a-input v-model:value="formState.repPassword" type="password" autocomplete="off" />
    </a-form-item>
    <a-form-item has-feedback label="收货地址" name="recvaddress">
      <a-input v-model:value="formState.recvaddress" type="input" autocomplete="off" />
    </a-form-item>



    <a-form-item :wrapper-col="{ span: 14, offset: 4 }">
      <a-button type="primary" @click="onSubmit">Submit</a-button>
      <a-button style="margin-left: 10px" @click="resetForm">Reset</a-button>
    </a-form-item>
  </a-form>
  <a-button @click="goback">返回</a-button>
  <button @click="handleClick">click</button>
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
        return Promise.reject('Please input the password again');
      } else if (value !== formState.password) {
        return Promise.reject("Two inputs don't match!");
      } else {
        return Promise.resolve();
      }
    };


    let validateMail = async (_rule: any, value: string) => {
      if (value === '') {
        return Promise.reject('Please input the mail');
      } else {
        const MAIL_REGEX_1= /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        if (!MAIL_REGEX_1.test(value)) {
          return Promise.reject('wrong mail format')
        }
        return Promise.resolve();
      }
    };

    let validateQQ = async (_rule: any, value: string) => {
      if (value === '') {
        return Promise.reject('Please input the qq');
      } else {
        const QQ_REGEX_1= /^\d{0,8}$/;
        if (!QQ_REGEX_1.test(value)) {
          return Promise.reject('wrong qq format')
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
        span: 4,
      },
      wrapperCol: {
        span: 14,
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