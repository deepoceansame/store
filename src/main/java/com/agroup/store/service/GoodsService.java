package com.agroup.store.service;

import com.agroup.store.domain.*;
import com.agroup.store.mapper.AccountMapper;
import com.agroup.store.mapper.GoodsMapper;
import com.agroup.store.mapper.GoodsimageMapper;
import com.agroup.store.mapper.MessageMapper;
import com.agroup.store.req.GetOthersGoodsReq;
import com.agroup.store.req.GoodsReq;
import com.agroup.store.req.GoodsSaveReq;
import com.agroup.store.req.PurchaseFormReq;
import com.agroup.store.resp.CommonResp;
import com.agroup.store.resp.GoodsResp;
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
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class GoodsService {
    @Resource
    private GoodsMapper goodsMapper;

    @Resource
    private GoodsimageMapper goodsimageMapper;

    @Resource
    private AccountMapper accountMapper;

    @Resource
    private MessageMapper messageMapper;

    private static final Logger LOG = LoggerFactory.getLogger(GoodsService.class);

    @Value("${picturesPath}")
    private String picturesPath;

    @Autowired
    JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String senderUsername;

    @Value("${spring.mail.nickname}")
    private String nickname;

    public PageResp<GoodsResp> getOthersGoods(GetOthersGoodsReq req){
        //??????
        GoodsExample goodsExample = new GoodsExample();
        GoodsExample.Criteria criteria1 = goodsExample.createCriteria();
        GoodsExample.Criteria criteria2 = goodsExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria1.andNameLike("%" + req.getName().trim() + "%");
            criteria2.andDescriptionLike("%" + req.getName().trim() + "%");
        }
        if (!ObjectUtils.isEmpty(req.getId())) {
            criteria1.andIdEqualTo(req.getId());
            criteria2.andIdEqualTo(req.getId());
        }
        LOG.info("?????????categoryId:{}",req.getCategoryId());
        if (req.getCategoryId()!=null && req.getCategoryId()!=1){
            criteria1.andCategoryIdEqualTo(req.getCategoryId());
            criteria2.andCategoryIdEqualTo(req.getCategoryId());
        }
        criteria1.andAccountIdNotEqualTo(req.getAccountId());
        criteria2.andAccountIdNotEqualTo(req.getAccountId());

        goodsExample.or(criteria2);
        int page = 1;
        if (!ObjectUtils.isEmpty(req.getPage())){
            page = req.getPage();
        }
        //????????????
        PageHelper.startPage(page, 10);
        LOG.info("????????????page{}",req.getPage());
        List<Goods> lis = goodsMapper.selectByExample(goodsExample);
        //??????????????????
        PageInfo<Goods> pageInfo = new PageInfo<>(lis);
        LOG.info("?????????{}",pageInfo.getTotal());
        LOG.info("?????????{}",pageInfo.getPages());

        List<GoodsResp> lisr = CopyUtil.copyList(lis, GoodsResp.class);

        PageResp<GoodsResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(lisr);
        return pageResp;
    }

    public Integer save(String req, MultipartFile[] imgs) throws IOException {
        LOG.info("?????????String{}",req);
        GoodsSaveReq saveReq = JSON.parseObject(req, GoodsSaveReq.class);
        LOG.info("?????????saveReq{}", saveReq);
        Goods goods = CopyUtil.copy(saveReq, Goods.class);
        UUID timebaseUUID = null;

        if (imgs != null && imgs.length>0){
            MultipartFile img = imgs[0];
            LOG.info("{}", img.getSize());
            timebaseUUID = Generators.timeBasedGenerator().generate();
            LOG.info("??????????????????{}",timebaseUUID.toString());
            String fileName = timebaseUUID.toString();
            String filePath = picturesPath + fileName+".jpg";
            goods.setImg(fileName+".jpg");
            LOG.info("?????????????????????{}",filePath);
            try{
                File dest = new File(filePath);
                Files.copy(img.getInputStream(), dest.toPath());
            } catch (Exception e){
                LOG.info(e.getMessage());
            }
            goodsMapper.insert(goods);
            LOG.info("??????goods????????????ID???{}", goods.getId());
            Goodsimage gi = new Goodsimage();
            gi.setGoodsid(goods.getId());
            gi.setImg(fileName+".jpg");
            goodsimageMapper.insert(gi);
            for (int i=1;i<imgs.length;i++){
                img = imgs[i];
                timebaseUUID = Generators.timeBasedGenerator().generate();
                fileName = timebaseUUID.toString();
                filePath = picturesPath + fileName + ".jpg";
                gi.setImg(fileName+".jpg");
                goodsimageMapper.insert(gi);
                try{
                    File dest = new File(filePath);
                    Files.copy(img.getInputStream(), dest.toPath());
                }catch (Exception e){
                    LOG.info(e.getMessage());
                }
            }
        }
        else{
            goodsMapper.insert(goods);
            LOG.info("??????goods????????????ID???{}", goods.getId());
        }
        return goods.getId();
    }

    public Goods selectById(Integer id){
        GoodsExample goodsExample = new GoodsExample();
        GoodsExample.Criteria criteria = goodsExample.createCriteria();
        criteria.andIdEqualTo(id);
        List<Goods> goodsList = goodsMapper.selectByExample(goodsExample);
        if (CollectionUtils.isEmpty(goodsList)) {
            return null;
        } else {
            return goodsList.get(0);
        }
    }


    public List<Goods> getMyGoods(Integer accountId){
        GoodsExample goodsExample = new GoodsExample();
        GoodsExample.Criteria criteria = goodsExample.createCriteria();
        criteria.andAccountIdEqualTo(accountId);
        List<Goods> goodsList = goodsMapper.selectByExample(goodsExample);
        return goodsList;
    }


    public void shutGoods(Integer goodsid){
        Goods goods = goodsMapper.selectByPrimaryKey(goodsid);
        Account seller = accountMapper.selectByPrimaryKey(goods.getAccountId());
        List<Account> buyers = accountMapper.getBuyersByGoodsId(goodsid);
        sendMailTobuyersForClosingGoods(buyers, seller, goods);
        goodsMapper.deleteByPrimaryKey(goodsid);
    }

    public void sendMailTobuyersForClosingGoods(List<Account> buyers, Account seller, Goods goods) {
        for (Account buyer : buyers) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject("???????????????????????????????????????????????????");
            message.setFrom(nickname + '<' + senderUsername + '>');
            message.setTo(buyer.getMail());
            message.setSentDate(new Date());
            String content = "?????????????????????"
                    + seller.getName() + "?????????????????????????????????????????????" + goods.getName() + "??????";
            message.setText(content);
            // ????????????
            javaMailSender.send(message);

        }

    }

    public CommonResp submitPurchaseForm(PurchaseFormReq req){
        CommonResp resp = new CommonResp();
        Integer buyerId = req.getBuyerId();
        Integer goodsId = req.getGoodsId();
        Date tradingDate = req.getTradingDate();
        String tradingLocation = req.getTradingLocation();
        Float payment = req.getPayment();
        Integer sellerId = goodsMapper.getAccountIdByGoodsId(goodsId);
        boolean success = goodsMapper.updateTradingInf(buyerId, goodsId, tradingDate, tradingLocation, payment) == 1;
        if(success){
            String content = "[????????????]????????????????????????????????????????????????";
            Message message = new Message(buyerId,sellerId,goodsId, new Timestamp(System.currentTimeMillis()), 1, content);
            messageMapper.addMessage(message);
            resp.setSuccess(true);
            resp.setMessage("????????????????????????????????????");
        }else {
            resp.setSuccess(false);
            resp.setMessage("??????????????????");
        }
        return resp;

    }

    public CommonResp<PurchaseRecord> getPurchaseRecord(Integer buyerId, Integer goodsId){
        CommonResp<PurchaseRecord> resp = new CommonResp();
        PurchaseRecord purchaseRecord = accountMapper.selectPurchaseRecordByPid(buyerId, goodsId).get(0);
        resp.setContent(purchaseRecord);
        return resp;
    }

}


