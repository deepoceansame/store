package com.agroup.store.service;

import com.agroup.store.domain.*;
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
}
