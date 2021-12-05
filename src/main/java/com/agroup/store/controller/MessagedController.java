package com.agroup.store.controller;

import com.agroup.store.domain.Message;
import com.agroup.store.domain.Messaged;
import com.agroup.store.mapper.MessageMapper;
import com.agroup.store.mapper.MessagedMapper;
import com.agroup.store.req.GetMessageReq;
import com.agroup.store.req.GetMessagedReq;
import com.agroup.store.resp.CommonResp;
import com.agroup.store.service.GoodsService;
import com.agroup.store.service.MessageService;
import com.agroup.store.service.MessagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;

@RestController
public class MessagedController {
    @Resource
    private MessagedMapper messagedMapper;
    @Resource
    private MessagedService messagedService;
    private static final Logger LOG = LoggerFactory.getLogger(GoodsService.class);
    @GetMapping("messaged/test")
    public void tess(Messaged message){
        LOG.info("senderid{}",message.getSenderid());
        LOG.info("receiverid{}",message.getReceiverid());
        message.setContent("这是什么");
        message.setType(1);
        long now = System.currentTimeMillis();
        Timestamp ts = new Timestamp(now);
        message.setSendtime(ts);
        messagedMapper.addMessage(message);

        List<Messaged> list = messagedMapper.getMessageListByThreeId(message.getSenderid(), message.getReceiverid(), message.getDesiredgoodsid());
        LOG.info("{},{}",list.get(0),list.get(0));
    }

    @PostMapping("messaged/addtextmessage")
    public void addTextMessage(@RequestBody Messaged message){
        long now = System.currentTimeMillis();
        Timestamp ts = new Timestamp(now);
        message.setSendtime(ts);
        LOG.info("{}",message.toString());
        messagedMapper.addMessage(message);
    }

    @PostMapping("messaged/addpicmessage")
    public void addPicMessage(@RequestParam("message") String message, MultipartFile img){
        LOG.info("收到的图片的大小{}", img.getSize());
        if (img != null){
            messagedService.addPicMessage(message, img);
        }
    }

    @GetMapping("messaged/getmessage")
    public CommonResp getMessageByThreeId(GetMessagedReq req){
        List<Messaged> list = messagedMapper.getMessageListByThreeId(req.getSenderid(), req.getReceiverid(), req.getDesiredgoodsid());
        for(Messaged mess:list){
            LOG.info("{}",mess.toString());
        }
        CommonResp commonResp = new CommonResp();
        commonResp.setContent(list);
        return commonResp;
    }
}
