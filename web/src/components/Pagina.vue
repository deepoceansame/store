<template>
  <div>
    <a-pagination
        v-model:current="current"
        :total="total"
        :show-total="total => `共 ${total} 项`"
        :page-size="10"
        @change = 'pageChange'
    />
  </div>
</template>

<script>
import {ref, onMounted, watch} from "vue";
import {useRoute, useRouter} from 'vue-router'
export default {
  props:['total'],
  name: "Pagina",
  setup(){
    const route = useRoute();
    const router = useRouter();
    const current = ref(1);
    const base = route.path.split('/')
    const pageChange = (pageNumber) => {
      current.value = pageNumber;
      if (typeof(route.params.keyword) == 'undefined'){
        router.push('/'+base[1]+`/page=${pageNumber}&category=${route.params.category}&keyword=`)
      }
      else {
        router.push('/' + base[1] + `/page=${pageNumber}&category=${route.params.category}&keyword=${route.params.keyword}`)
      }
      console.log('Page: ', pageNumber);
    };

    onMounted(
        () => {
          current.value = Number(route.params.page);
        }
    )

    watch(() => route.params,(newv, oldv) => {
          current.value = Number(route.params.page);
        }
    )

    return{
      current,
      pageChange,
    }
  }
}
</script>

<style scoped>

</style>