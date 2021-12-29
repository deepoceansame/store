package com.agroup.store.mapper;

import com.agroup.store.domain.Account;
import com.agroup.store.domain.Goods;
import com.agroup.store.domain.GoodsExample;

import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GoodsMapper {
    long countByExample(GoodsExample example);

    int deleteByExample(GoodsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Goods record);

    int insertSelective(Goods record);

    List<Goods> selectByExample(GoodsExample example);

    Goods selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Goods record, @Param("example") GoodsExample example);

    int updateByExample(@Param("record") Goods record, @Param("example") GoodsExample example);

    int updateByPrimaryKeySelective(Goods record);

    int updateByPrimaryKey(Goods record);

    Integer getAccountIdByGoodsId(Integer goodId);

    int updateTradingInf(@Param("buyerId") Integer buyerId, @Param("goodsId")Integer goodsId, @Param("tradingDate")Date tradingDate, @Param("tradingLocation")String tradingLocation, @Param("payment") Float payment);

    int updateTradingEffectToFalse(@Param("buyerId") Integer buyerId, @Param("goodsId")Integer goodsId);
}