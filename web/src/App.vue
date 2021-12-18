<template>
  <div>
    {{account}}
    <button v-show="account.id" @click="logout">登出</button>
    <button v-show="account.id" @click="goToCenter">账户中心</button>
  </div>
  <router-view></router-view>
</template>


<script lang="ts">
import { defineComponent, computed } from 'vue';
import store from "@/store"
import axios from "axios";
import {message} from "ant-design-vue";
import {useRoute, useRouter} from 'vue-router'

export default defineComponent({
  setup() {
    const route = useRoute();
    const router = useRouter();
    const account = computed(() => {
      return store.state.account
    })
    const logout = () => {
      axios.get('/account/logout/' + account.value.token).then((response) => {
        const data: any = response.data;
        if (data.success) {
          message.success("退出登录成功！");
          store.commit("setAccount", {});
          router.push("/")
        } else {
          message.error(data.message);
        }
      });
    }
    const goToCenter = ()=>{
      router.push({name:'accountcenter', params: {accountid: account.value.id}})
    }
    return {
      account,
      logout,
      goToCenter
    };
  },
});
</script>


<style>


#components-layout-demo-top .logo {
  float: left;
  width: 120px;
  height: 31px;
  margin: 16px 24px 16px 0;
  background: rgba(255, 255, 255, 0.3);
}
.ant-row-rtl #components-layout-demo-top .logo {
  float: right;
  margin: 16px 0 16px 24px;
}

[data-theme='dark'] .site-layout-content {
  background: #141414;
}
</style>