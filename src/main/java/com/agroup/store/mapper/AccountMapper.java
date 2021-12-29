package com.agroup.store.mapper;

import com.agroup.store.domain.*;

import java.util.List;

import com.agroup.store.req.DesiredGoodsReq;
import com.agroup.store.req.GoodsReq;
import com.agroup.store.req.PurchaseRecordReq;
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

    List<Account> getBuyersByGoodsId(Integer goodsid);

    int deletePurchaseRecord(QuitBuyReq req);

    List<PurchaseRecord> selectPurchaseRecordByPid(@Param("buyerId") Integer buyerId, @Param("goodsId") Integer goodsId);

    int updateAvatar(@Param("accountId") Integer accountId, @Param("filename") String filename);

    int chargeMoney(@Param("accountId") Integer accountId, @Param("chargeAmount") Float chargeAmount);

    Float getMoney(@Param("accountId") Integer accountId);

    int updateMoney(@Param("accountId") Integer accountId, @Param("amount") Float amount);

    Integer getAccountIdByGoodsId(Integer GoodsId);
}