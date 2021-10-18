package com.agroup.store.service;

import com.agroup.store.domain.Account;
import com.agroup.store.mapper.AccountMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AccountService {
    @Resource
    private AccountMapper accountMapper;

    public List<Account> list(){
        return accountMapper.list();
    }
}
