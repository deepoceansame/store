package com.agroup.store.mapper;

import com.agroup.store.domain.DesiredGoods;
import com.agroup.store.domain.Goodsimage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DesiredGoodsMapper {

    int insertDesiredGoods(DesiredGoods desiredGoods);

    List<DesiredGoods> selectDesiredGoodsByAccountId(Integer accountId);

    int insertDesiredGoodsImg(Goodsimage goodsimage);

    List<String> getDesiredGoodsImgs(Integer desiredgoodsid);

    DesiredGoods selectById(Integer id);

}
