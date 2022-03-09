<template>
  <tr class="row" align="center">
    <td class="pic" @click="handleClick"><img class="pic" :src="'http://127.0.0.1:8083/' + desiredgoods.img" alt="图片"/></td>
    <td class="goodsname">{{desiredgoods.name}}</td>
    <td class="price">{{desiredgoods.price}}</td>
    <td>
      <p class="info">
        {{desiredgoods.description}}
      </p>
    </td>
  </tr>
</template>

<script>
import {useRouter} from 'vue-router'

export default {
  name: "GoodsTableRow",
  props:['desiredgoods'],
  setup(props){
    const router = useRouter()
    const handleClick = () =>{
      router.push(`/sellerseedesiredgoods/desiredgoodsid=${props.desiredgoods.id}`);
    }
    return{
      handleClick
    }
  }
}
</script>

<style scoped>

.row{
  width: 100%;
}

.info{
  padding-left: 4px;
  font-size: 6px;
  width: 100%;
  overflow : hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.pic{
  width: 50px;
  height: 50px;
}

.price{
  width:50px;
}

.goodsname{
  width: 50px;
}

</style>