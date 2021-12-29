package com.agroup.store.service;

import com.agroup.store.domain.*;
import com.agroup.store.exception.BusinessException;
import com.agroup.store.exception.BusinessExceptionCode;
import com.agroup.store.mapper.*;
import com.agroup.store.req.*;
import com.agroup.store.resp.*;
import com.agroup.store.util.CopyUtil;
import com.fasterxml.uuid.Generators;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.File;
import java.nio.file.Files;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class AccountService {

    private static final Logger LOG = LoggerFactory.getLogger(AccountService.class);

    @Value("${picturesPath}")
    private String picturesPath;

    @Resource
    private AccountMapper accountMapper;

    @Resource
    private DesiredGoodsMapper desiredGoodsMapper;

    @Resource
    private GoodsMapper goodsMapper;

    @Resource
    private MessageMapper messageMapper;

    @Resource
    private MessagedMapper messagedMapper;

    @Autowired
    JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String senderUsername;

    @Value("${spring.mail.nickname}")
    private String nickname;

    public CommonResp list() {
        CommonResp<List<Account>> resp = new CommonResp<>();
        resp.setContent(accountMapper.selectByExample(null));
        return resp;
    }

    public void save(AccountSaveReq req) {
        Account account = CopyUtil.copy(req, Account.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            // 新增
            Account accountDB = selectByMail(req.getMail());
            if (ObjectUtils.isEmpty(accountDB)) {
                accountMapper.insert(account);
            } else {
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

    public Account selectById(Integer id) {
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

    public AccountLoginResp login(AccountLoginReq req) {
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

    public CommonResp requestGoods(RequestGoodsReq req) {
            CommonResp resp = new CommonResp();
            Integer buyerId = req.getAccountId();
            Goods goods = goodsMapper.selectByPrimaryKey(req.getGoodId());
            Account buyer = accountMapper.selectByPrimaryKey(buyerId);
            Account seller = accountMapper.selectByPrimaryKey(goods.getAccountId());
            if(buyer.getCreditPoint() <= 0){
                resp.setSuccess(false);
                resp.setMessage("您的信誉积分不足，无法参与购买！");
                return resp;
            }else if(seller.getCreditPoint() <= 0){
                resp.setSuccess(false);
                resp.setMessage("卖家的信誉积分不足，暂时无法购买！");
                return resp;
            }
            boolean success = accountMapper.insertPurchaseRecordWithoutEffect(buyerId, req.getGoodId()) == 1;
            resp.setSuccess(success);
            if (success) {
                resp.setMessage("成功参与");
                sendMailToSellerForNewBuyer(buyer, seller, goods);
            } else {
                resp.setMessage("参与过了！");
            }
            return resp;
    }

    public CommonResp cancelPurRec(Integer buyerId, Integer goodsId){
        Integer sellerId = goodsMapper.getAccountIdByGoodsId(goodsId);
        PurchaseRecord purchaseRecord = accountMapper.selectPurchaseRecordByPid(buyerId, goodsId).get(0);
        Float payment = purchaseRecord.getPayment();
        Goods goods = goodsMapper.selectByPrimaryKey(goodsId);
        Float price = goods.getPrice();
        Boolean success = goodsMapper.updateTradingEffectToFalse(buyerId, goodsId) == 1;
        CommonResp resp = new CommonResp();
        resp.setSuccess(success);
        if (success) {
            resp.setMessage("成功取消交易");
            String content = "";
            if (payment.equals(price)) {
                content = "[交易失败]卖家取消了订单 （卖家信誉分-1）";
                accountMapper.decrease1CreditPoint(sellerId);
            } else if (payment < 0.8 * price) {
                content = "[交易失败]卖家取消了订单 （买家信誉分-1）";
                accountMapper.decrease1CreditPoint(buyerId);
            } else {
                content = "[交易失败]卖家取消了订单";
            }
            Message message = new Message(sellerId, buyerId, goodsId, new Timestamp(System.currentTimeMillis()), 1, content);
            messageMapper.addMessage(message);
        } else {
            resp.setMessage("取消失败");
        }
        return resp;
    }

    public CommonResp cancelSupRec(Integer sellerId, Integer desiredGoodsId){
        Integer buyerId = desiredGoodsMapper.getAccountIdByDesiredGoodsId(desiredGoodsId);
        SupplyRecord supplyRecord = accountMapper.selectSupplyRecordByPid(sellerId, desiredGoodsId).get(0);
        Float payment = supplyRecord.getPayment();
        DesiredGoods desiredGoods = desiredGoodsMapper.selectById(desiredGoodsId);
        Float price = desiredGoods.getPrice();
        Boolean success = desiredGoodsMapper.updateTradingEffectToFalse(sellerId, desiredGoodsId) == 1;
        CommonResp resp =new CommonResp();
        resp.setSuccess(success);
        if(success){
            resp.setMessage("成功取消交易");
            String content = "";
            if (payment < price) {
                content = "[交易失败]卖家取消了订单 （买家信誉分-1）";
                accountMapper.decrease1CreditPoint(buyerId);
            } else {
                content = "[交易失败]卖家取消了订单";
            }
            Messaged messaged = new Messaged(sellerId, buyerId, desiredGoodsId, new Timestamp(System.currentTimeMillis()), 1, content);
            messagedMapper.addMessage(messaged);
        }else {
            resp.setMessage("取消失败");
        }
        return resp;
    }

    public CommonResp purchaseGoods(Integer buyerId, Integer goodsId) {
        Integer sellerId = goodsMapper.getAccountIdByGoodsId(goodsId);
        PurchaseRecord purchaseRecord = accountMapper.selectPurchaseRecordByPid(buyerId, goodsId).get(0);
        Float payment = purchaseRecord.getPayment();
        CommonResp resp = transferMoney(buyerId, sellerId, payment.toString());
        CommonResp resp1 = new CommonResp();
        resp1.setSuccess(resp.getSuccess());
        if(resp.getSuccess()){
            boolean success = accountMapper.updatePurchaseRecord(buyerId, goodsId) == 1;
            resp1.setSuccess(success);
            if(success){
                resp1.setMessage("交易成功");
                String content = "[交易成功]卖家确认订单，交易成功！（双方信誉分+1）";
                accountMapper.increase1CreditPoint(buyerId);
                accountMapper.increase1CreditPoint(sellerId);
                Message message = new Message(sellerId,buyerId,goodsId, new Timestamp(System.currentTimeMillis()), 1, content);
                messageMapper.addMessage(message);
            }else {
                resp1.setMessage("交易失败");
                String content = "[交易失败]原因不明！";
                Message message = new Message(sellerId,buyerId,goodsId, new Timestamp(System.currentTimeMillis()), 1, content);
                messageMapper.addMessage(message);
            }
        }else {
            goodsMapper.updateTradingEffectToFalse(buyerId, goodsId);
            accountMapper.decrease1CreditPoint(buyerId);
            resp1.setMessage("买家余额不足，订单取消！");
            String content = "[交易失败]您的余额不足，请充值！（买家信誉分-1)";
            Message message = new Message(sellerId,buyerId,goodsId, new Timestamp(System.currentTimeMillis()), 1, content);
            messageMapper.addMessage(message);
        }

        return resp1;
    }

    public CommonResp confirmSupplyRecord(Integer sellerId, Integer desiredGoodsId) {
        Integer buyerId = desiredGoodsMapper.getAccountIdByDesiredGoodsId(desiredGoodsId);
        SupplyRecord supplyRecord = accountMapper.selectSupplyRecordByPid(sellerId, desiredGoodsId).get(0);
        Float payment = supplyRecord.getPayment();
        CommonResp resp = transferMoney(buyerId, sellerId, payment.toString());
        CommonResp resp1 = new CommonResp();
        resp1.setSuccess(resp.getSuccess());
        if(resp.getSuccess()){
            boolean success = accountMapper.updateSupplyRecord(sellerId, desiredGoodsId) == 1;
            resp1.setSuccess(success);
            if(success){
                resp1.setMessage("交易成功");
                String content = "[交易成功]卖家确认求购订单，交易成功！（双方信誉分+1）";
                accountMapper.increase1CreditPoint(buyerId);
                accountMapper.increase1CreditPoint(sellerId);
                Messaged messaged = new Messaged(sellerId, buyerId, desiredGoodsId,new Timestamp(System.currentTimeMillis()), 1, content);
                messagedMapper.addMessage(messaged);
            }else {
                resp1.setMessage("交易失败");
                String content = "[交易失败]原因不明！";
                Messaged messaged = new Messaged(sellerId,buyerId,desiredGoodsId, new Timestamp(System.currentTimeMillis()), 1, content);
                messagedMapper.addMessage(messaged);
            }
        }else {
            desiredGoodsMapper.updateTradingEffectToFalse(sellerId, desiredGoodsId);
            accountMapper.decrease1CreditPoint(buyerId);
            resp1.setMessage("买家余额不足，订单取消！");
            String content = "[交易失败]您的余额不足，请充值！（买家信誉分-1)";
            Messaged messaged = new Messaged(sellerId,buyerId, desiredGoodsId, new Timestamp(System.currentTimeMillis()), 1, content);
            messagedMapper.addMessage(messaged);
        }

        return resp1;
    }



    public PageResp<GoodsPurchaseRecordResp> showPurchaseRecordsByAccountId(PurchaseRecordReq req) {
        Integer accountId = req.getAccountId();
        int page = 1;
        if (req.getPage() != 0) {
            page = req.getPage();
        }
        //PageHelper.startPage(page, 3);
        LOG.info("传过来的page{}", req.getPage());
        List<GoodsPurchaseRecordResp> goodsList = accountMapper.selectGoodsByAccountId(accountId);
//        PageInfo<GoodsPurchaseRecordResp> pageInfo = new PageInfo<>(goodsList);
//        LOG.info("总行数{}",pageInfo.getTotal());
//        LOG.info("总页数{}",pageInfo.getPages());

        List<GoodsPurchaseRecordResp> lisr = CopyUtil.copyList(goodsList, GoodsPurchaseRecordResp.class);

        PageResp<GoodsPurchaseRecordResp> pageResp = new PageResp<>();
        pageResp.setTotal(100);
        pageResp.setList(lisr);

        return pageResp;
    }

    public CommonResp particiSupply(ParticiSupplyReq req) {
        CommonResp resp = new CommonResp();
        DesiredGoods desiredGoods = desiredGoodsMapper.selectById(req.getDesiredgoodsId());
        Account seller = accountMapper.selectByPrimaryKey(req.getAccountId());
        Account buyer = accountMapper.selectByPrimaryKey(desiredGoods.getAccountId());
        if(seller.getCreditPoint() <= 0){
            resp.setSuccess(false);
            resp.setMessage("您的信誉积分不足，无法参与求购！");
            return resp;
        }else if(buyer.getCreditPoint() <= 0){
            resp.setSuccess(false);
            resp.setMessage("买家的信誉积分不足，暂时无法参与求购！");
            return resp;
        }
        boolean success = accountMapper.insertSupplyRecord(req.getAccountId(), req.getDesiredgoodsId()) == 1;
        resp.setSuccess(success);
        if (success) {
            resp.setMessage("成功参与");
            sendMailToBuyerForNewSupplier(buyer, seller, desiredGoods);
        } else {
            resp.setMessage("失败！");
        }
        return resp;
    }

    public CommonResp getAccountParticingSupply(Integer accountId) {
        List<DesiredGoods> list = accountMapper.getSupplyingGoodsByAccountId(accountId);
        CommonResp resp = new CommonResp();
        resp.setContent(list);
        return resp;
    }

    public List<Account> getSupplyer(Integer desiredGoodsId) {
        return accountMapper.getSupplyingAccountByDesiredGoodsId(desiredGoodsId);
    }

    public List<Account> getBuyer(Integer goodsId) {
        return accountMapper.getBuyersByGoodsId(goodsId);
    }

    public CommonResp<Integer> getAccountByGoodsId(Integer goodsId) {
        CommonResp<Integer> resp = new CommonResp();
        resp.setContent(accountMapper.getAccountIdByGoodsId(goodsId));
        return resp;
    }

    public CommonResp<Integer> getAccountByDesiredGoodsId(Integer desiredGoodsId) {
        CommonResp<Integer> resp = new CommonResp();
        resp.setContent(accountMapper.getAccountIdByDesiredGoodsId(desiredGoodsId));
        return resp;
    }

    public CommonResp quitBuy(QuitBuyReq req) {
        boolean success = accountMapper.deletePurchaseRecord(req) == 1;
        CommonResp resp = new CommonResp();
        resp.setSuccess(success);
        if (success) {
            resp.setMessage("退出成功！");
        } else {
            resp.setMessage("退出失败！");
        }
        return resp;
    }

    public CommonResp hasEnrolledBuy(Integer buyerId, Integer goodsId) {
        CommonResp resp = new CommonResp();
        List<PurchaseRecord> list = accountMapper.selectPurchaseRecordByPid(buyerId, goodsId);
        boolean exist = list.size() > 0;
        if (exist) {
            resp.setMessage("参加过了");
            resp.setContent(true);
        } else {
            resp.setMessage("没参加过");
            resp.setContent(false);
        }
        return resp;
    }

    public CommonResp updateAvator(String accountid, MultipartFile img) {
        Integer accountId = Integer.parseInt(accountid);
        CommonResp resp = new CommonResp();
        UUID timebaseUUID = Generators.timeBasedGenerator().generate();
        String fileName = timebaseUUID.toString() + ".jpg";

        //将图片存到文件系统里
        String filePath = picturesPath + fileName;
        try {
            File dest = new File(filePath);
            Files.copy(img.getInputStream(), dest.toPath());
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        //数据库更新
        accountMapper.updateAvatar(accountId, fileName);
        return resp;
    }

    public CommonResp chargeMoney(Integer accountId, String chargeAmount) {
        CommonResp resp = new CommonResp();
        float cm = Float.parseFloat(chargeAmount);
        accountMapper.chargeMoney(accountId, cm);
        return resp;
    }

    public CommonResp getMoney(Integer id){
        CommonResp commonResp = new CommonResp();
        commonResp.setContent(accountMapper.getMoney(id));
        return commonResp;
    }

    public CommonResp transferMoney(Integer senderid, Integer receiverid, String amount) {
        LOG.info("进来了");
        CommonResp resp = new CommonResp();
        Float famount = Float.parseFloat(amount);
        Float smoney = accountMapper.getMoney(senderid);
        if (smoney == null) {
            smoney = 0f;
        }
        Float rmoney = accountMapper.getMoney(receiverid);
        if (rmoney == null) {
            rmoney = 0f;
        }
        Float afterSenderMoney = 0f;
        Float afterReceiverMoney = 0f;
        if (smoney < famount) {
            resp.setMessage("你的钱不够 余额" + smoney);
            resp.setSuccess(false);
            LOG.info("钱不够");
        } else {
            afterSenderMoney = smoney - famount;
            afterReceiverMoney = rmoney + famount;
            accountMapper.updateMoney(senderid, afterSenderMoney);
            accountMapper.updateMoney(receiverid, afterReceiverMoney);
            resp.setMessage("钱转过去了 余额" + afterSenderMoney);
            resp.setSuccess(true);
            LOG.info("转了");
            Account sender = accountMapper.selectByPrimaryKey(senderid);
            Account receiver = accountMapper.selectByPrimaryKey(receiverid);
            sendMailWhenTransferMoney(sender, receiver, famount);
        }
        return resp;
    }

    public void sendMailWhenTransferMoney(Account sender, Account receiver, Float amount) {
        // 构建一个邮件对象
        SimpleMailMessage message = new SimpleMailMessage();
        // 设置邮件主题
        message.setSubject("通知：有人转账给你");
        // 设置邮件发送者，这个跟application.yml中设置的要一致
        message.setFrom(nickname + '<' + senderUsername + '>');
        // 设置邮件接收者，可以有多个接收者，中间用逗号隔开，以下类似
        // message.setTo("10*****16@qq.com","12****32*qq.com");
        message.setTo(receiver.getMail());
        // 设置邮件抄送人，可以有多个抄送人
//        message.setCc("12****32*qq.com");
//        // 设置隐秘抄送人，可以有多个
//        message.setBcc("7******9@qq.com");
//        // 设置邮件发送日期
        message.setSentDate(new Date());
        // 设置邮件的正文

        String content = "用户（用户名：" + sender.getName() + "）向你转账"+amount;
        message.setText(content);
        // 发送邮件
        javaMailSender.send(message);
    }

    public void sendMailToBuyerForNewSupplier(Account buyer, Account seller, DesiredGoods desiredGoods) {
        // 构建一个邮件对象
        SimpleMailMessage message = new SimpleMailMessage();
        // 设置邮件主题
        message.setSubject("通知：有新卖家愿意提供您的求购");
        // 设置邮件发送者，这个跟application.yml中设置的要一致
        message.setFrom(nickname + '<' + senderUsername + '>');
        // 设置邮件接收者，可以有多个接收者，中间用逗号隔开，以下类似
        // message.setTo("10*****16@qq.com","12****32*qq.com");
        message.setTo(buyer.getMail());
        // 设置邮件抄送人，可以有多个抄送人
//        message.setCc("12****32*qq.com");
//        // 设置隐秘抄送人，可以有多个
//        message.setBcc("7******9@qq.com");
//        // 设置邮件发送日期
        message.setSentDate(new Date());
        // 设置邮件的正文

        String content = "卖家（用户名：" + seller.getName() + "）愿意提供您的求购商品（商品名：" + desiredGoods.getName()
                + "）\n 联系邮箱：" + seller.getMail();
        message.setText(content);
        // 发送邮件
        javaMailSender.send(message);
    }

    public void sendMailToSellerForNewBuyer(Account buyer, Account seller, Goods goods) {
        // 构建一个邮件对象
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("通知：有新买家愿意购买您的商品");
        message.setFrom(nickname + '<' + senderUsername + '>');
        message.setTo(seller.getMail());
        message.setSentDate(new Date());
        String content = "买家（用户名：" + buyer.getName() + "）愿意购买您的商品（商品名：" + goods.getName()
                + "）\n 联系邮箱：" + buyer.getMail();
        message.setText(content);
        javaMailSender.send(message);
    }
}
