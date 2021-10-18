package com.agroup.store.service;

import com.agroup.store.domain.Account;
import com.agroup.store.mapper.AccountMapper;
import com.agroup.store.resp.CommonResp;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AccountService {
    @Resource
    private AccountMapper accountMapper;

    public CommonResp list(){
        CommonResp<List<Account>> resp = new CommonResp<>();
        resp.setContent(accountMapper.selectByExample(null));
        return resp;
    }
}
