package com.agroup.store.controller;

import com.agroup.store.domain.Account;
import com.agroup.store.req.AccountLoginReq;
import com.agroup.store.req.AccountSaveReq;
import com.agroup.store.resp.AccountLoginResp;
import com.agroup.store.resp.CommonResp;
import com.agroup.store.service.AccountService;
import com.agroup.store.util.SnowFlake;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
public class AccountController {

    private static final Logger LOG = LoggerFactory.getLogger(AccountController.class);

    @Resource
    private AccountService accountServiceService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private SnowFlake snowFlake;

    @Resource
    private AccountService accountService;

    @GetMapping("account/list")
    public CommonResp list(){
        return accountService.list();
    }

    @PostMapping("account/save")
    public CommonResp save(@Valid @RequestBody AccountSaveReq req){
        if(!ObjectUtils.isEmpty(req.getRepPassword())){
            //req.setRepPassword(DigestUtils.md5DigestAsHex(req.getRepPassword().getBytes()));
        }
        CommonResp resp = new CommonResp<>();
        accountService.save(req);
        return resp;
    }

    @PostMapping("account/login")
    public CommonResp login(@Valid @RequestBody AccountLoginReq req){
        CommonResp<AccountLoginResp> resp = new CommonResp();
        AccountLoginResp accountLoginResp = accountService.login(req);
        String token = snowFlake.nextId();
        LOG.info("生成token{}", token);
        redisTemplate.opsForValue().set(token, JSONObject.toJSONString(accountLoginResp), 3600 * 2, TimeUnit.SECONDS);
        accountLoginResp.setToken(token);
        LOG.info("accountLOGinREsp.ID: {}", accountLoginResp.getId());
        resp.setContent(accountLoginResp);
        return resp;
    }

    @GetMapping("account/logout/{token}")
    public CommonResp logout(@PathVariable String token){
        CommonResp resp = new CommonResp();
        redisTemplate.delete(token);
        LOG.info("退出登录，从redis中删除了token:{}", token);
        return resp;
    }
}
