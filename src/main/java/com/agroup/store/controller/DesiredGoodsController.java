package com.agroup.store.controller;

import com.agroup.store.domain.Account;
import com.agroup.store.domain.DesiredGoods;
import com.agroup.store.domain.Goods;
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
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
public class DesiredGoodsController {

    private static final Logger LOG = LoggerFactory.getLogger(AccountController.class);

    @Resource
    private DesiredGoodsService desiredGoodsService;

    //添加求购
    @PostMapping("desiredGoods/addDesiredGoods")
    public CommonResp addDesiredGoods(@Valid @RequestParam("desiredGoods") String req, MultipartFile[] imgs){
        CommonResp resp = new CommonResp();
        resp.setContent(desiredGoodsService.addDesiredGoods(req, imgs));
        return resp;
    }

    @GetMapping("desiredgoods/getList")
    public CommonResp getList(DesiredGoodsMainListReq req){
        CommonResp resp = new CommonResp();
        resp.setContent(desiredGoodsService.getList(req));
        return resp;
    }

    //展示本用户的求购列表
    @GetMapping("desiredGoods/showDesiredGoodsListByAccountId")
    public CommonResp showDesiredGoodsListByAccountId(@Valid DesiredGoodsListReq req){
        CommonResp<PageResp<DesiredGoods>> resp = new CommonResp<>();
        resp.setContent(desiredGoodsService.showDesiredGoodsListByAccountId(req));
        return resp;
    }

    @GetMapping("desiredGoods/getbyid/{id}")
    public CommonResp getById(@PathVariable Integer id){
        DesiredGoods goods = desiredGoodsService.selectById(id);
        CommonResp resp = new CommonResp();
        resp.setContent(goods);
        return resp;
    }

    @GetMapping("desiredGoods/getimages")
    public CommonResp getImages(@RequestParam("desiredgoodsid") Integer desiredgoodsid){
        List<String> lis = desiredGoodsService.getimages(desiredgoodsid);
        CommonResp resp = new CommonResp();
        resp.setContent(lis);
        return resp;
    }

    @PostMapping("desiredGoods/deleteinquirybypid")
    public CommonResp shutGoods(@RequestParam Integer desiredgoodsId){
        desiredGoodsService.shutGoods(desiredgoodsId);
        CommonResp resp = new CommonResp();
        return resp;
    }

    @PostMapping("desiredGoods/submitSupplyForm")
    public CommonResp submitSupplyForm(SupplyFormReq req){
        return desiredGoodsService.submitSupplyForm(req);
    }

    @GetMapping("desiredGoods/getSupplyRecord")
    public CommonResp getSupplyRecord(Integer sellerId, Integer desiredGoodsId){
        return desiredGoodsService.getSupplyRecord(sellerId, desiredGoodsId);
    }
}
