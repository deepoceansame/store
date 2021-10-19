<template>
  <div class="home">
    <a-button type="text" danger>Danger Text</a-button>
    <img alt="Vue logo" src="../assets/logo.png">
    <HelloWorld msg="Welcome to Your Vue.js + TypeScript App"/>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import axios from 'axios'

export default defineComponent({
  name: 'Home',
  setup(){
    console.log("setup");

    axios.get("http://localhost:8081/goods/list?name=橘子").then(
        (response) => {
          console.log(response)
        }
    );

  }
});
</script>
