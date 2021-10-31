<template>
  <a-layout-header>
    <div class="logo" />
    <a-menu
        theme="dark"
        mode="horizontal"
        :style="{ lineHeight: '64px' }"
    >
      <a-menu-item key="1"><router-link to="/buy">买</router-link></a-menu-item>
      <a-menu-item key="2">卖</a-menu-item>
    </a-menu>
  </a-layout-header>
</template>

<script lang="ts">
import { defineComponent } from 'vue';

export default defineComponent({
  name: 'TheHeader'
});
</script>

<style scoped>
</style>