<template>
  <a-list item-layout="horizontal" size="large" :pagination="pagination" :data-source="goods">
    <template #footer>
      <div>
        <b>ant design vue</b>
        footer part
      </div>
    </template>

    <template #renderItem="{ item }">
      <a-list-item key="item.id">
        <a-list-item-meta>
          <template #title>
            <a :href="item.href">{{ item.name }} {{item.accountid}}</a>
            <br/>
            <label>12元</label>
          </template>
          <template #avatar><a-avatar :src="item.img" /></template>
        </a-list-item-meta>
        {{ item.description }}
      </a-list-item>
    </template>
  </a-list>
</template>
<script>
import {defineComponent, onMounted, ref} from 'vue';
import axios from 'axios'


export default defineComponent({
  name: "Home",
  setup() {
    const pagination = {
      onChange: (page) => {
        console.log(page);
      },
      pageSize: 2,
    };
    const goods = ref();
    onMounted(() => {
      axios.get("http://localhost:8081/goods/list").then(
          ( response ) => {
            console.log("12")
            console.log(response)
            goods.value = response.data.content;
          }
      )
    })

    return {
      goods,
      pagination,
    };
  },
});
</script>

<style scoped>
.ant-avatar{
  height: 50px;
  width: 50px;
  line-height: 50px;
  border-radius: 8%;
  margin: 5px 0;
}
</style>