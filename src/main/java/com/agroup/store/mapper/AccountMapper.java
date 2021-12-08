package com.agroup.store.mapper;

import com.agroup.store.domain.Account;
import com.agroup.store.domain.AccountExample;
import java.util.List;

import com.agroup.store.domain.DesiredGoods;
import com.agroup.store.domain.Goods;
import com.agroup.store.req.DesiredGoodsReq;
import com.agroup.store.req.GoodsReq;
import com.agroup.store.req.QuitBuyReq;
import com.agroup.store.resp.GoodsPurchaseRecordResp;
import org.apache.ibatis.annotations.Param;

public interface AccountMapper {
    long countByExample(AccountExample example);

    int deleteByExample(AccountExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Account record);

    int insertSelective(Account record);

    List<Account> selectByExample(AccountExample example);

    Account selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Account record, @Param("example") AccountExample example);

    int updateByExample(@Param("record") Account record, @Param("example") AccountExample example);

    int updateByPrimaryKeySelective(Account record);

    int updateByPrimaryKey(Account record);

    int insertPurchaseRecordWithoutEffect(@Param("accountId") Integer accountId, @Param("goodsId") Integer goodsId);

    int updatePurchaseRecord(@Param("accountId") Integer accountId, @Param("goodsId") Integer goodsId);

    List<GoodsPurchaseRecordResp> selectGoodsByAccountId(Integer accountId);

    int insertSupplyRecord(@Param("accountId") Integer accountId, @Param("desiredgoodsId") Integer desiredgoodsId);

    List<DesiredGoods> getSupplyingGoodsByAccountId(Integer accountid);

    List<Account> getSupplyingAccountByDesiredGoodsId(Integer desiredgoodsid);

    int deletePurchaseRecord(QuitBuyReq req);
}