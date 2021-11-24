package com.agroup.store.controller;

import com.agroup.store.domain.Message;
import com.agroup.store.mapper.MessageMapper;
import com.agroup.store.req.GetMessageReq;
import com.agroup.store.resp.CommonResp;
import com.agroup.store.service.GoodsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;

@RestController
public class MessageController {
    @Resource
    private MessageMapper messageMapper;

    private static final Logger LOG = LoggerFactory.getLogger(GoodsService.class);
    @GetMapping("message/test")
    public void tess(Message message){
        LOG.info("senderid{}",message.getSenderid());
        LOG.info("receiverid{}",message.getReceiverid());
        message.setContent("这是什么");
        message.setType(1);
        long now = System.currentTimeMillis();
        Timestamp ts = new Timestamp(now);
        message.setSendtime(ts);
        messageMapper.addMessage(message);

        List<Message> list = messageMapper.getMessageListByThreeId(message.getSenderid(), message.getReceiverid(), message.getGoodsid());
        LOG.info("{},{}",list.get(0),list.get(0));
    }

    @PostMapping("message/addtextmessage")
    public void addTextMessage(@RequestBody Message message){
        long now = System.currentTimeMillis();
        Timestamp ts = new Timestamp(now);
        message.setSendtime(ts);
        LOG.info("{}",message.toString());
        messageMapper.addMessage(message);
    }

    @PostMapping("message/addpicmessage")
    public void addPicMessage(@RequestParam String message, @RequestParam MultipartFile img){

    }

    @GetMapping("message/getmessage")
    public CommonResp getMessageByThreeId(GetMessageReq req){
        List<Message> list = messageMapper.getMessageListByThreeId(req.getSenderid(), req.getReceiverid(), req.getGoodsid());
        for(Message mess:list){
            LOG.info("{}",mess.toString());
        }
        CommonResp commonResp = new CommonResp();
        commonResp.setContent(list);
        return commonResp;
    }
}
