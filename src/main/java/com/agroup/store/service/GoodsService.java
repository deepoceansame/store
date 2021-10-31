package com.agroup.store.service;

import com.agroup.store.domain.Goods;
import com.agroup.store.domain.GoodsExample;
import com.agroup.store.mapper.GoodsMapper;
import com.agroup.store.req.GoodsReq;
import com.agroup.store.resp.GoodsResp;
import com.agroup.store.resp.PageResp;
import com.agroup.store.util.CopyUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
@Service
public class GoodsService {
    @Resource
    private GoodsMapper goodsMapper;
    private static final Logger LOG = LoggerFactory.getLogger(GoodsService.class);

    public PageResp<GoodsResp> list(GoodsReq req){
        //筛查
        GoodsExample goodsExample = new GoodsExample();
        GoodsExample.Criteria criteria = goodsExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }

        //启动分页
        PageHelper.startPage(req.getPage(), 10);
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
}
