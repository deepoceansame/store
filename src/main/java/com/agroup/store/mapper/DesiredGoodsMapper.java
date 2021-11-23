package com.agroup.store.mapper;

import com.agroup.store.domain.DesiredGoods;

import java.util.List;

public interface DesiredGoodsMapper {

    int insertDesiredGoods(DesiredGoods desiredGoods);

    List<DesiredGoods> selectDesiredGoodsByAccountId(Integer accountId);

}
