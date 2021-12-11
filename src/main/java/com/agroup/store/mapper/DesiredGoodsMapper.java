package com.agroup.store.mapper;

import com.agroup.store.domain.DesiredGoods;
import com.agroup.store.domain.Goodsimage;
import com.agroup.store.req.DesiredGoodsMainListReq;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DesiredGoodsMapper {

    int insertDesiredGoods(DesiredGoods desiredGoods);

    List<DesiredGoods> selectDesiredGoodsByAccountId(Integer accountId);

    int insertDesiredGoodsImg(Goodsimage goodsimage);

    List<String> getDesiredGoodsImgs(Integer desiredgoodsid);

    DesiredGoods selectById(Integer id);

    List<DesiredGoods> selectList(DesiredGoodsMainListReq req);

    int deleteByPrimaryKey(Integer id);
}
