package com.agroup.store.service;

import com.agroup.store.domain.*;
import com.agroup.store.exception.BusinessException;
import com.agroup.store.exception.BusinessExceptionCode;
import com.agroup.store.mapper.AccountMapper;
import com.agroup.store.req.*;
import com.agroup.store.resp.*;
import com.agroup.store.util.CopyUtil;
import com.fasterxml.uuid.Generators;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.File;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Service
public class AccountService {

    private static final Logger LOG = LoggerFactory.getLogger(AccountService.class);

    @Value("${picturesPath}")
    private String picturesPath;

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
        //PageHelper.startPage(page, 3);
        LOG.info("传过来的page{}",req.getPage());
        List<GoodsPurchaseRecordResp> goodsList=accountMapper.selectGoodsByAccountId(accountId);
//        PageInfo<GoodsPurchaseRecordResp> pageInfo = new PageInfo<>(goodsList);
//        LOG.info("总行数{}",pageInfo.getTotal());
//        LOG.info("总页数{}",pageInfo.getPages());

        List<GoodsPurchaseRecordResp> lisr = CopyUtil.copyList(goodsList, GoodsPurchaseRecordResp.class);

        PageResp<GoodsPurchaseRecordResp> pageResp = new PageResp<>();
        pageResp.setTotal(100);
        pageResp.setList(lisr);

        return pageResp;
    }

    public CommonResp particiSupply(ParticiSupplyReq req){
        boolean success=accountMapper.insertSupplyRecord(req.getAccountId(),req.getDesiredgoodsId())==1;
        CommonResp resp=new CommonResp();
        resp.setSuccess(success);
        if(success){
            resp.setMessage("购买成功！");
        }else {
            resp.setMessage("购买失败！");
        }
        return resp;
    }

    public CommonResp getAccountParticingSupply(Integer accountId){
        List<DesiredGoods> list = accountMapper.getSupplyingGoodsByAccountId(accountId);
        CommonResp resp = new CommonResp();
        resp.setContent(list);
        return resp;
    }

    public List<Account> getSupplyer(Integer desiredGoodsId){
        return accountMapper.getSupplyingAccountByDesiredGoodsId(desiredGoodsId);
    }

    public List<Account> getBuyer(Integer goodsId){
        return accountMapper.getBuyersByGoodsId(goodsId);
    }

    public CommonResp quitBuy(QuitBuyReq req){
        boolean success = accountMapper.deletePurchaseRecord(req) == 1;
        CommonResp resp = new CommonResp();
        resp.setSuccess(success);
        if(success){
            resp.setMessage("退出成功！");
        }else {
            resp.setMessage("退出失败！");
        }
        return resp;
    }

    public CommonResp hasEnrolledBuy(Integer buyerId, Integer goodsId){
        CommonResp resp = new CommonResp();
        List<PurchaseRecord> list = accountMapper.selectPurchaseRecordByPid(buyerId, goodsId);
        boolean exist = list.size() > 0;
        if (exist){
            resp.setMessage("参加过了");
            resp.setContent(true);
        }
        else{
            resp.setMessage("没参加过");
            resp.setContent(false);
        }
        return resp;
    }

    public CommonResp updateAvator(String accountid, MultipartFile img){
        Integer accountId = Integer.parseInt(accountid);
        CommonResp resp = new CommonResp();
        UUID timebaseUUID = Generators.timeBasedGenerator().generate();
        String fileName = timebaseUUID.toString()+".jpg";

        //将图片存到文件系统里
        String filePath = picturesPath + fileName;
        try{
            File dest = new File(filePath);
            Files.copy(img.getInputStream(), dest.toPath());
        }catch (Exception e){
            LOG.info(e.getMessage());
        }

        //数据库更新
        accountMapper.updateAvatar(accountId, fileName);
        return resp;
    }

    public CommonResp chargeMoney(Integer accountId, String chargeAmount){
        CommonResp resp = new CommonResp();
        float cm = Float.parseFloat(chargeAmount);
        accountMapper.chargeMoney(accountId, cm);
        return  resp;
    }

    public CommonResp transferMoney(Integer senderid, Integer receiverid, String amount){
        LOG.info("进来了");
        CommonResp resp = new CommonResp();
        Float famount = Float.parseFloat(amount);
        Float smoney = accountMapper.getMoney(senderid);
        if (smoney==null){
            smoney = 0f;
        }
        Float rmoney = accountMapper.getMoney(receiverid);
        if(rmoney==null){
            rmoney = 0f;
        }
        Float afterSenderMoney = 0f;
        Float afterReceiverMoney = 0f;
        if (smoney < famount){
            resp.setMessage("你的钱不够呀");
            resp.setSuccess(false);
            LOG.info("钱不够");
        }
        else{
            afterSenderMoney = smoney - famount;
            afterReceiverMoney = rmoney + famount;
            accountMapper.updateMoney(senderid, afterSenderMoney);
            accountMapper.updateMoney(receiverid, afterReceiverMoney);
            resp.setMessage("钱转过去了");
            resp.setSuccess(true);
            LOG.info("转了");
        }
        return resp;
    }
}
