package com.agroup.store.service;

import com.agroup.store.domain.Goods;
import com.agroup.store.domain.GoodsExample;
import com.agroup.store.mapper.GoodsMapper;
import com.agroup.store.req.GoodsReq;
import com.agroup.store.req.GoodsSaveReq;
import com.agroup.store.resp.GoodsResp;
import com.agroup.store.resp.PageResp;
import com.agroup.store.util.CopyUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class GoodsService {
    @Resource
    private GoodsMapper goodsMapper;
    private static final Logger LOG = LoggerFactory.getLogger(GoodsService.class);

    @Value("${picturesPath}")
    private String picturesPath;

    public PageResp<GoodsResp> list(GoodsReq req){
        //筛查
        GoodsExample goodsExample = new GoodsExample();
        GoodsExample.Criteria criteria = goodsExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }
        if (!ObjectUtils.isEmpty(req.getId())) {
            criteria.andIdEqualTo(req.getId());
        }
        int page = 1;
        if (!ObjectUtils.isEmpty(req.getPage())){
            page = req.getPage();
        }
        //启动分页
        PageHelper.startPage(page, 3);
        LOG.info("传过来的page{}",req.getPage());
        List<Goods> lis = goodsMapper.selectByExample(goodsExample);
        //获取分页信息
        PageInfo<Goods> pageInfo = new PageInfo<>(lis);
        LOG.info("总行数{}",pageInfo.getTotal());
        LOG.info("总页数{}",pageInfo.getPages());

        List<GoodsResp> lisr = CopyUtil.copyList(lis, GoodsResp.class);

        PageResp<GoodsResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(lisr);
        return pageResp;
    }

    public Integer save(String req, MultipartFile[] imgs) throws IOException {
        LOG.info("收到了String{}",req);
        GoodsSaveReq saveReq = JSON.parseObject(req, GoodsSaveReq.class);
        Goods goods = CopyUtil.copy(saveReq, Goods.class);
        LOG.info("转化为saveReq{}", saveReq);
        for (MultipartFile img: imgs){
            LOG.info("{}", img.getSize());
            String fileName = img.getOriginalFilename();
            String filePath = picturesPath + fileName;
            goods.setImg(fileName);
            LOG.info("要保存到的路径{}",filePath);
            try{
                File dest = new File(filePath);
                Files.copy(img.getInputStream(), dest.toPath());
            } catch (Exception e){
                LOG.info(e.getMessage());
            }
        }
        if (saveReq.getId() == null){
            goodsMapper.insert(goods);
            LOG.info("插入goods后产生的ID为{}", goods.getId());
        }
        else{
            goodsMapper.updateByPrimaryKeySelective(goods);
        }
        return goods.getId();
    }
}
