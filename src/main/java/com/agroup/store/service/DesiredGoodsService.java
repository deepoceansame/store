package com.agroup.store.service;

import com.agroup.store.domain.*;
import com.agroup.store.mapper.AccountMapper;
import com.agroup.store.mapper.DesiredGoodsMapper;
import com.agroup.store.mapper.MessageMapper;
import com.agroup.store.mapper.MessagedMapper;
import com.agroup.store.req.*;
import com.agroup.store.resp.CommonResp;
import com.agroup.store.resp.PageResp;
import com.agroup.store.util.CopyUtil;
import com.alibaba.fastjson.JSON;
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
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.File;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class DesiredGoodsService {

    private static final Logger LOG = LoggerFactory.getLogger(AccountService.class);

    @Resource
    private DesiredGoodsMapper desiredGoodsMapper;

    @Resource
    private AccountMapper accountMapper;

    @Resource
    private MessagedMapper messagedMapper;

    @Value("${picturesPath}")
    private String picturesPath;

    @Autowired
    JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String senderUsername;

    @Value("${spring.mail.nickname}")
    private String nickname;

    public Integer addDesiredGoods(String req, MultipartFile[] imgs){
        LOG.info("收到了String{}",req);
        GoodsSaveReq saveReq = JSON.parseObject(req, GoodsSaveReq.class);
        LOG.info("转化为saveReq{}", saveReq);
        DesiredGoods goods = CopyUtil.copy(saveReq, DesiredGoods.class);
        UUID timebaseUUID = null;

        if (imgs != null && imgs.length>0){
            MultipartFile img = imgs[0];
            LOG.info("{}", img.getSize());
            timebaseUUID = Generators.timeBasedGenerator().generate();
            LOG.info("列表图片名字{}",timebaseUUID.toString());
            String fileName = timebaseUUID.toString();
            String filePath = picturesPath + fileName+".jpg";
            goods.setImg(fileName+".jpg");
            LOG.info("要保存到的路径{}",filePath);
            try{
                File dest = new File(filePath);
                Files.copy(img.getInputStream(), dest.toPath());
            } catch (Exception e){
                LOG.info(e.getMessage());
            }
            desiredGoodsMapper.insertDesiredGoods(goods);
            LOG.info("插入goods后产生的ID为{}", goods.getId());
            Goodsimage gi = new Goodsimage();
            gi.setGoodsid(goods.getId());
            gi.setImg(fileName+".jpg");
            desiredGoodsMapper.insertDesiredGoodsImg(gi);
            for (int i=1;i<imgs.length;i++){
                img = imgs[i];
                timebaseUUID = Generators.timeBasedGenerator().generate();
                fileName = timebaseUUID.toString();
                filePath = picturesPath + fileName + ".jpg";
                gi.setImg(fileName+".jpg");
                desiredGoodsMapper.insertDesiredGoodsImg(gi);
                try{
                    File dest = new File(filePath);
                    Files.copy(img.getInputStream(), dest.toPath());
                }catch (Exception e){
                    LOG.info(e.getMessage());
                }
            }
        }
        else{
            desiredGoodsMapper.insertDesiredGoods(goods);
            LOG.info("插入desiredgoods后产生的ID为{}", goods.getId());
        }
        return goods.getId();
    }

    public PageResp<DesiredGoods> showDesiredGoodsListByAccountId(@Valid DesiredGoodsListReq req){
        Integer accountId=req.getAccountId();
        int page=1;
        if(req.getPage()!=0){
            page=req.getPage();
        }
//        PageHelper.startPage(page, 3);
//        LOG.info("传过来的page{}",req.getPage());
        List<DesiredGoods> goodsList=desiredGoodsMapper.selectDesiredGoodsByAccountId(accountId);
//        PageInfo<DesiredGoods> pageInfo = new PageInfo<>(goodsList);
//        LOG.info("总行数{}",pageInfo.getTotal());
//        LOG.info("总页数{}",pageInfo.getPages());


        PageResp<DesiredGoods> pageResp = new PageResp<>();
        pageResp.setTotal(goodsList.size());
        pageResp.setList(goodsList);

        return pageResp;
    }

    public DesiredGoods selectById(Integer id){
        return desiredGoodsMapper.selectById(id);
    }

    public List<String> getimages(Integer desiredgoodsid){
        return desiredGoodsMapper.getDesiredGoodsImgs(desiredgoodsid);
    }



    public PageResp getList(DesiredGoodsMainListReq req){
        int page=1;
        if(req.getPage()!=0){
            page=req.getPage();
        }
        if (req.getKeyword()!=null && req.getKeyword().trim().isEmpty()){
            req.setKeyword(null);
        }
        if (req.getKeyword()!=null){
            req.setKeyword(req.getKeyword().trim());
        }
        PageHelper.startPage(page, 10);
        LOG.info("传过来的page{}",req.getPage());
        List<DesiredGoods> list =  desiredGoodsMapper.selectList(req);
        PageInfo<DesiredGoods> pageInfo = new PageInfo<>(list);
        LOG.info("总行数{}",pageInfo.getTotal());
        LOG.info("总页数{}",pageInfo.getPages());

        PageResp<DesiredGoods> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

        return pageResp;
    }

    public void shutGoods(Integer desiredGoodsId){
        DesiredGoods desiredGoods = desiredGoodsMapper.selectById(desiredGoodsId);
        Account buyer = accountMapper.selectByPrimaryKey(desiredGoods.getAccountId());
        List<Account> suppliers = accountMapper.getSupplyingAccountByDesiredGoodsId(desiredGoodsId);
        desiredGoodsMapper.deleteByPrimaryKey(desiredGoodsId);
        sendMailToSuppliersForClosingDesiredGoods(suppliers, buyer, desiredGoods);
    }

    public void sendMailToSuppliersForClosingDesiredGoods(List<Account> suppliers, Account buyer, DesiredGoods desiredGoods) {
        // 设置邮件发送者，这个跟application.yml中设置的要一致
        for (Account supplier : suppliers) {
            // 构建一个邮件对象
            SimpleMailMessage message = new SimpleMailMessage();
            // 设置邮件主题
            message.setSubject("通知：有买家删除了您愿意提供的求购");
            message.setFrom(nickname + '<' + senderUsername + '>');
            // 设置邮件接收者，可以有多个接收者，中间用逗号隔开，以下类似
            // message.setTo("10*****16@qq.com","12****32*qq.com");
            message.setTo(supplier.getMail());
            // 设置邮件抄送人，可以有多个抄送人
//        message.setCc("12****32*qq.com");
//        // 设置隐秘抄送人，可以有多个
//        message.setBcc("7******9@qq.com");
//        // 设置邮件发送日期
            message.setSentDate(new Date());
            // 设置邮件的正文

            String content = "买家（用户名："
                    + buyer.getName() + "）关闭了一件求购商品（商品名：" + desiredGoods.getName() + "）。";
            message.setText(content);
            // 发送邮件
            javaMailSender.send(message);

        }

    }

    public CommonResp submitSupplyForm(SupplyFormReq req){
        CommonResp resp = new CommonResp();
        Integer sellerId = req.getSellerId();
        Integer desiredGoodsId = req.getDesiredGoodsId();
        Date tradingDate = req.getTradingDate();
        String tradingLocation = req.getTradingLocation();
        Float payment = req.getPayment();
        Integer buyerId = desiredGoodsMapper.getAccountIdByDesiredGoodsId(desiredGoodsId);
        boolean success = desiredGoodsMapper.updateTradingInf(sellerId, desiredGoodsId, tradingDate, tradingLocation, payment) == 1;
        if(success){
            String content = "[购买通知]买家已提交求购订单，请您进行确认。";
            Messaged messaged = new Messaged(buyerId,sellerId,desiredGoodsId, new Timestamp(System.currentTimeMillis()), 1, content);
            messagedMapper.addMessage(messaged);
            resp.setSuccess(true);
            resp.setMessage("成功创建求购订单，待买家确认");
        }else {
            resp.setSuccess(false);
            resp.setMessage("创建求购订单失败");
        }
        return resp;

    }


    public CommonResp<SupplyRecord> getSupplyRecord(Integer sellerId, Integer goodsId){
        CommonResp<SupplyRecord> resp = new CommonResp();
        SupplyRecord supplyRecord = accountMapper.selectSupplyRecordByPid(sellerId, goodsId).get(0);
        resp.setContent(supplyRecord);
        return resp;
    }

}
