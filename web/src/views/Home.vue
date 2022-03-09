<template>
<!--  <router-link to="/goods/page=1&category=1&keyword=">enter</router-link>-->
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

    <a-form-item style="margin-top: 30px ; margin-left: 40px" has-feedback label="邮箱" name="mail">
      <a-input v-model:value="formState.mail" type="mail" autocomplete="off" />
    </a-form-item>
    <a-form-item has-feedback label="密码" style=" margin-left: 40px" name="password">
      <a-input v-model:value="formState.password" type="password" autocomplete="off" />
    </a-form-item>
    <a-form-item :wrapper-col="{ span: 20, offset: 9 }">
      <a-button style=" margin-left: 40px" type="primary" @click="onSubmit">登录</a-button>
      <a-button style="margin-left: 10px" @click="resetForm">重置</a-button>
      <a-button style="margin-left: 10px" @click="goToSignup">注册</a-button>
    </a-form-item>
  </a-form>

<!--  <button @click="handleClick">click</button>-->
</template>

<script lang="ts">
import {computed, defineComponent, reactive, ref, toRaw} from 'vue';
import axios from "axios";
import {message, Layout} from "ant-design-vue";
import {useRouter, useRoute} from "vue-router";
import store from '@/store';
declare let hexMd5: any;
declare let KEY: any;
export default defineComponent({
  setup() {
    const formRef = ref();
    const route = useRoute();
    const router = useRouter();
    const formState = reactive({
      mail: '',
      password: '',
    });


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

    let validatePass = async (_rule: any, value: string) => {
      if (value === '') {
        return Promise.reject('请输入密码');
      } else {
        return Promise.resolve();
      }
    };

    const rules = {
      mail: [{
        required: true,
        validator: validateMail,
        trigger: 'blur',
      }],
      password: [{
        required: true,
        validator: validatePass,
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
        var loginreq = {password: ''}
        Object.assign(loginreq, formState)
        loginreq.password = hexMd5(formState.password + KEY)
        axios.post("account/login", loginreq).then((response : any) => {
          console.log(response);
          if (response.data.success) {
            message.info('登录成功')
            store.commit("setAccount", response.data.content)
            router.replace("/goods/page=1&category=1&keyword=")
          } else {
            message.info('登陆失败'+' '+response.data.message)
          }
        })
      }).catch((error :any) => {
        console.log('error', error);
      });
    }

    const resetForm = () => {
      formRef.value.resetFields();
    };

    const handleClick = () => {
      console.log(formState.password)
      console.log( hexMd5(formState.password + KEY) )
      console.log(route.name)
      console.log(route.name !== "Home" && route.name !== "signup")

    }

    const goToSignup = () => {
      router.push('/signup')
    }

    return {
      formState,
      formRef,
      rules,
      layout,
      resetForm,
      onSubmit,
      handleClick,
      goToSignup,
    };
  },

});
</script>

