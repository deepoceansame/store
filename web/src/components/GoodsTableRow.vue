<template>
  <tr class="row">
    <td class="pic" @click="handleClick"><img class="pic" src="../assets/logo.png" alt="图片"/></td>
    <td class="goodsname">{{goods.name}}</td>
    <td class="price">{{goods.price}}</td>
    <td class="accountId">
      {{goods.accountId}}
    </td>
    <td>
      <p class="info">
        {{goods.description}}
      </p>
    </td>
  </tr>
</template>

<script>
import {useRouter} from 'vue-router'

export default {
  name: "GoodsTableRow",
  props:['goods'],
  setup(props){
    const router = useRouter()
    const handleClick = () =>{
      router.push(`/buyerseegoods/goodsid=${props.goods.id}`);
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