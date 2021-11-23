package com.agroup.store.service;

import com.agroup.store.domain.Account;
import com.agroup.store.domain.AccountExample;
import com.agroup.store.domain.DesiredGoods;
import com.agroup.store.domain.Goods;
import com.agroup.store.exception.BusinessException;
import com.agroup.store.exception.BusinessExceptionCode;
import com.agroup.store.mapper.AccountMapper;
import com.agroup.store.req.*;
import com.agroup.store.resp.*;
import com.agroup.store.util.CopyUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.validation.Valid;
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

    public Account selectById(Integer id){
        AccountExample accountExample = new AccountExample();
        AccountExample.Criteria criteria = accountExample.createCriteria();
        criteria.andIdEqualTo(id);
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

    public CommonResp requestGoods(RequestGoodsReq req){
        boolean success=accountMapper.insertPurchaseRecordWithoutEffect(req.getAccountId(),req.getGoodId())==1;
        CommonResp resp=new CommonResp();
        resp.setSuccess(success);
        if(success){
            resp.setMessage("已参与购买，待卖家回复");
        }else {
            resp.setMessage("购买失败！");
        }
        return resp;
    }

    public CommonResp purchaseGoods(RequestGoodsReq req){
        boolean success=accountMapper.updatePurchaseRecord(req.getAccountId(),req.getGoodId())==1;
        CommonResp resp=new CommonResp();
        resp.setSuccess(success);
        if(success){
            resp.setMessage("购买成功！");
        }else {
            resp.setMessage("购买失败！");
        }
        return resp;
    }

    public PageResp<GoodsPurchaseRecordResp> showPurchaseRecordsByAccountId(PurchaseRecordReq req){
        Integer accountId=req.getAccountId();
        int page=1;
        if(req.getPage()!=0){
            page=req.getPage();
        }
        PageHelper.startPage(page, 3);
        LOG.info("传过来的page{}",req.getPage());
        List<GoodsPurchaseRecordResp> goodsList=accountMapper.selectGoodsByAccountId(accountId);
        PageInfo<GoodsPurchaseRecordResp> pageInfo = new PageInfo<>(goodsList);
        LOG.info("总行数{}",pageInfo.getTotal());
        LOG.info("总页数{}",pageInfo.getPages());

        List<GoodsPurchaseRecordResp> lisr = CopyUtil.copyList(goodsList, GoodsPurchaseRecordResp.class);

        PageResp<GoodsPurchaseRecordResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(lisr);

        return pageResp;
    }

    public CommonResp addDesiredGoods(DesiredGoodsReq req){
        DesiredGoods desiredGoods = CopyUtil.copy(req, DesiredGoods.class);
        boolean success=accountMapper.insertDesiredGoods(desiredGoods)==1;
        CommonResp resp=new CommonResp();
        resp.setSuccess(success);
        if(success){
            resp.setMessage("添加求购成功！");
        }else {
            resp.setMessage("添加求购失败！");
        }
        return resp;
    }

    public PageResp<DesiredGoods> showDesiredGoodsListByAccountId(@Valid DesiredGoodsListReq req){
        Integer accountId=req.getAccountId();
        int page=1;
        if(req.getPage()!=0){
            page=req.getPage();
        }
        PageHelper.startPage(page, 3);
        LOG.info("传过来的page{}",req.getPage());
        List<DesiredGoods> goodsList=accountMapper.selectDesiredGoodsByAccountId(accountId);
        PageInfo<DesiredGoods> pageInfo = new PageInfo<>(goodsList);
        LOG.info("总行数{}",pageInfo.getTotal());
        LOG.info("总页数{}",pageInfo.getPages());


        PageResp<DesiredGoods> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(goodsList);

        return pageResp;
    }

}
