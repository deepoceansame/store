<template>
  <tr class="row">
    <td class="pic"><img class="pic" src="../assets/logo.png"/></td>
    <td class="goodsname">{{goods.name}}</td>
    <td class="price">{{goods.price}}</td>
    <td class="accountname">
      {{goods.accountname}}
    </td>
    <td>
      <p class="info">
        {{goods.info}}
      </p>
    </td>
  </tr>
</template>

<script>
import {ref} from "vue";

export default {
  name: "GoodsTableRow",
  props:['goods']
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