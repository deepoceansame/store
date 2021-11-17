package com.agroup.store.controller;

import com.agroup.store.req.GoodsReq;
import com.agroup.store.req.GoodsSaveReq;
import com.agroup.store.resp.CommonResp;
import com.agroup.store.resp.GoodsResp;
import com.agroup.store.resp.PageResp;
import com.agroup.store.service.GoodsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
public class GoodsController {
    private static final Logger LOG = LoggerFactory.getLogger(GoodsService.class);
    @Resource
    GoodsService goodsService = new GoodsService();

    @GetMapping("goods/list")
    public CommonResp list(GoodsReq req){
        CommonResp<PageResp<GoodsResp>> resp = new CommonResp<>();
        resp.setContent(goodsService.list(req));
        return resp;
    }

    @PostMapping("goods/save")
    public CommonResp save(@RequestParam("goods") String saveReq, @RequestParam("img") MultipartFile[] imgs) throws IOException {
        Integer goodsId = goodsService.save(saveReq, imgs);
        CommonResp resp = new CommonResp<>();
        resp.setContent(goodsId);
        return resp;
    }
}
