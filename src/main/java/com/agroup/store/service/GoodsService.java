package com.agroup.store.service;

import com.agroup.store.domain.Goods;
import com.agroup.store.domain.GoodsExample;
import com.agroup.store.mapper.GoodsMapper;
import com.agroup.store.req.GoodsReq;
import com.agroup.store.resp.CommonResp;
import com.agroup.store.resp.GoodsResp;
import com.agroup.store.util.CopyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
@Service
public class GoodsService {
    @Resource
    private GoodsMapper goodsMapper;

    public List<GoodsResp> list(GoodsReq req){
        GoodsExample goodsExample = new GoodsExample();
        GoodsExample.Criteria criteria = goodsExample.createCriteria();
        criteria.andNameLike("%"+req.getName()+"%");
        List<Goods> lis = goodsMapper.selectByExample(goodsExample);
        List<GoodsResp> lisr = CopyUtil.copyList(lis, GoodsResp.class);
        return lisr;
    }
}
