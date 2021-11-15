package com.agroup.store.service;

import com.agroup.store.domain.Goods;
import com.agroup.store.domain.GoodsExample;
import com.agroup.store.mapper.GoodsMapper;
import com.agroup.store.req.GoodsReq;
import com.agroup.store.req.GoodsSaveReq;
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
        if (!ObjectUtils.isEmpty(req.getId())) {
            criteria.andIdEqualTo(req.getId());
        }
        int page = 1;
        if (!ObjectUtils.isEmpty(req.getPage())){
            page = req.getPage();
        }
        //启动分页
        PageHelper.startPage(page, 10);
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

    public void save(GoodsSaveReq req) {
        LOG.info("{}",req);
        Goods goods = CopyUtil.copy(req, Goods.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            // 新增
            goodsMapper.insert(goods);
        } else {
            // 更新
            goodsMapper.updateByPrimaryKey(goods);
        }
    }
}
