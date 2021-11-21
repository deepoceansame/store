package com.agroup.store.mapper;

import com.agroup.store.domain.Account;
import com.agroup.store.domain.AccountExample;
import java.util.List;

import com.agroup.store.domain.Goods;
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

    int insertBuyGoodRecord(@Param("accountId") Integer accountId, @Param("GoodId") Integer GoodId);

    List<Goods> selectGoodsByAccountId(Integer accountId);
}