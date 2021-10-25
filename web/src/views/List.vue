<template>
  <div class="lis">
    <img class="pic" src="../assets/logo.png" alt="picture"/>
    <div class="panda">
      <label class="price">120000yuan</label><br/>
      <label class="accountName">TTTTTXY</label>
    </div>
    <div class="goodsname">goodsName</div>
    <p class="info">出售Vue标签， 世间仅有一个，售价人道</p>
  </div>
</template>

<script>
export default {
  name: "List"
}
</script>

<style scoped>
  .lis{
    margin: 1px;
    display: flex;
    flex-direction: row;
    flex-wrap: nowrap;
    border-width: 3px;
    height: 60px;
    border-color: red;
    border-style: solid;
  }
  .pic{
    width: 50px;
    height: 50px;
  }

  .panda{
    padding: 0 10px;
  }
  .info{
    height: 100%;
    width: 40%;
    margin: 0;
    max-height: 60px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .goodsname{
    text-align: center;
    padding: 0 10px;
  }
</style>