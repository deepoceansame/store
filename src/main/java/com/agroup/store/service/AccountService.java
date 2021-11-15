package com.agroup.store.service;

import com.agroup.store.domain.Account;
import com.agroup.store.domain.AccountExample;
import com.agroup.store.domain.Goods;
import com.agroup.store.exception.BusinessException;
import com.agroup.store.exception.BusinessExceptionCode;
import com.agroup.store.mapper.AccountMapper;
import com.agroup.store.req.AccountLoginReq;
import com.agroup.store.req.AccountSaveReq;
import com.agroup.store.req.GoodsSaveReq;
import com.agroup.store.resp.AccountLoginResp;
import com.agroup.store.resp.CommonResp;
import com.agroup.store.util.CopyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AccountService {

    private static final Logger LOG = LoggerFactory.getLogger(AccountService.class);

    @Resource
    private AccountMapper accountMapper;

    public CommonResp list(){
        CommonResp<List<Account>> resp = new CommonResp<>();
        resp.setContent(accountMapper.selectByExample(null));
        return resp;
    }



    public void save(AccountSaveReq req) {
        Account account = CopyUtil.copy(req, Account.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            // 新增
            Account accountDB = selectByMail(req.getMail());
            if (ObjectUtils.isEmpty(accountDB)){
                accountMapper.insert(account);
            } else{
                throw new BusinessException(BusinessExceptionCode.MAIL_BEEN_USED);
            }

        } else {
            // 更新
            account.setId(null);
            account.setPassword(null);
            account.setMoney(null);
            accountMapper.updateByPrimaryKeySelective(account);
        }
    }


    public Account selectByMail(String mail) {
        AccountExample accountExample = new AccountExample();
        AccountExample.Criteria criteria = accountExample.createCriteria();
        criteria.andMailEqualTo(mail);
        List<Account> accountList = accountMapper.selectByExample(accountExample);
        if (CollectionUtils.isEmpty(accountList)) {
            return null;
        } else {
            return accountList.get(0);
        }
    }

    public AccountLoginResp login(AccountLoginReq req){
        Account accountDB = selectByMail(req.getMail());
        if (ObjectUtils.isEmpty(accountDB)) {
            // 用户名不存在
            LOG.info("用户名不存在, {}", req.getMail());
            throw new BusinessException(BusinessExceptionCode.WRONG_PASS_OR_ACCOUNT);
        } else {
            if (accountDB.getPassword().equals(req.getPassword())) {
                // 登录成功
                AccountLoginResp accountLoginResp = CopyUtil.copy(accountDB, AccountLoginResp.class);
                accountLoginResp.setId(accountDB.getId());
                return accountLoginResp;
            } else {
                // 密码不对
                LOG.info("密码不对, 输入密码：{}, 数据库密码：{}", req.getPassword(), accountDB.getPassword());
                throw new BusinessException(BusinessExceptionCode.WRONG_PASS_OR_ACCOUNT);
            }
        }
    }
}
