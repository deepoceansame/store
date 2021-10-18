package com.agroup.store.controller;

import com.agroup.store.req.GoodsReq;
import com.agroup.store.resp.CommonResp;
import com.agroup.store.resp.GoodsResp;
import com.agroup.store.service.GoodsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class GoodsController {

    @Resource
    GoodsService goodsService = new GoodsService();

    @GetMapping("goods/list")
    public CommonResp list(GoodsReq req){
        CommonResp<List<GoodsResp>> resp = new CommonResp<>();
        resp.setContent(goodsService.list(req));
        return resp;
    }
}
