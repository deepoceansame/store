package com.agroup.store.controller;

import com.agroup.store.domain.Account;
import com.agroup.store.domain.DesiredGoods;
import com.agroup.store.req.*;
import com.agroup.store.resp.AccountLoginResp;
import com.agroup.store.resp.CommonResp;
import com.agroup.store.resp.GoodsPurchaseRecordResp;
import com.agroup.store.resp.PageResp;
import com.agroup.store.service.AccountService;
import com.agroup.store.service.DesiredGoodsService;
import com.agroup.store.util.SnowFlake;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

@RestController
public class DesiredGoodsController {

    private static final Logger LOG = LoggerFactory.getLogger(AccountController.class);

    @Resource
    private DesiredGoodsService desiredGoodsService;

    //添加求购
    @GetMapping("desireGoods/addDesiredGoods")
    public CommonResp addDesiredGoods(@Valid DesiredGoodsReq req){
        return desiredGoodsService.addDesiredGoods(req);
    }

    //展示本用户的求购列表
    @GetMapping("desireGoods/showDesiredGoodsListByAccountId")
    public CommonResp showDesiredGoodsListByAccountId(@Valid DesiredGoodsListReq req){
        CommonResp<PageResp<DesiredGoods>> resp = new CommonResp<>();
        resp.setContent(desiredGoodsService.showDesiredGoodsListByAccountId(req));
        return resp;
    }

}
