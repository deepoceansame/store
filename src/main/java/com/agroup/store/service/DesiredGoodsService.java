package com.agroup.store.service;

import com.agroup.store.domain.Account;
import com.agroup.store.domain.AccountExample;
import com.agroup.store.domain.DesiredGoods;
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
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Service
public class DesiredGoodsService {

    private static final Logger LOG = LoggerFactory.getLogger(AccountService.class);

    @Resource
    private DesiredGoodsMapper desiredGoodsMapper;

    public CommonResp addDesiredGoods(DesiredGoodsReq req){
        DesiredGoods desiredGoods = CopyUtil.copy(req, DesiredGoods.class);
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
