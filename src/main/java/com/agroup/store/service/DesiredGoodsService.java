package com.agroup.store.service;

import com.agroup.store.domain.Account;
import com.agroup.store.domain.AccountExample;
import com.agroup.store.domain.DesiredGoods;
import com.agroup.store.domain.Goods;
import com.agroup.store.exception.BusinessException;
import com.agroup.store.exception.BusinessExceptionCode;
import com.agroup.store.mapper.AccountMapper;
import com.agroup.store.mapper.DesiredGoodsMapper;
import com.agroup.store.req.*;
import com.agroup.store.resp.AccountLoginResp;
import com.agroup.store.resp.CommonResp;
import com.agroup.store.resp.GoodsPurchaseRecordResp;
import com.agroup.store.resp.PageResp;
import com.agroup.store.util.CopyUtil;
import com.alibaba.fastjson.JSON;
import com.fasterxml.uuid.Generators;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

@Service
public class DesiredGoodsService {

    private static final Logger LOG = LoggerFactory.getLogger(AccountService.class);

    @Resource
    private DesiredGoodsMapper desiredGoodsMapper;

    @Value("${picturesPath}")
    private String picturesPath;


    public CommonResp addDesiredGoods(String req, MultipartFile[] imgs){
        LOG.info("收到了String{}",req);
        DesiredGoodsReq saveReq = JSON.parseObject(req, DesiredGoodsReq.class);
        DesiredGoods desiredGoods = CopyUtil.copy(saveReq, DesiredGoods.class);
        LOG.info("转化为saveReq{}", saveReq);
        UUID timebaseUUID = null;
        for (MultipartFile img: imgs){
            LOG.info("{}", img.getSize());
            timebaseUUID = Generators.timeBasedGenerator().generate();
            LOG.info("生成的图片名字{}",timebaseUUID.toString());
            String fileName = timebaseUUID.toString();
            String filePath = picturesPath + fileName+".jpg";
            desiredGoods.setImg(fileName+".jpg");
            LOG.info("要保存到的路径{}",filePath);
            try{
                File dest = new File(filePath);
                Files.copy(img.getInputStream(), dest.toPath());
            } catch (Exception e){
                LOG.info(e.getMessage());
            }
        }
        boolean success=desiredGoodsMapper.insertDesiredGoods(desiredGoods)==1;
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
        List<DesiredGoods> goodsList=desiredGoodsMapper.selectDesiredGoodsByAccountId(accountId);
        PageInfo<DesiredGoods> pageInfo = new PageInfo<>(goodsList);
        LOG.info("总行数{}",pageInfo.getTotal());
        LOG.info("总页数{}",pageInfo.getPages());


        PageResp<DesiredGoods> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(goodsList);

        return pageResp;
    }

}
