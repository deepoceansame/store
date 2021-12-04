package com.agroup.store.service;

import com.agroup.store.domain.Message;
import com.agroup.store.domain.Messaged;
import com.agroup.store.mapper.MessageMapper;
import com.agroup.store.mapper.MessagedMapper;
import com.agroup.store.req.GoodsSaveReq;
import com.alibaba.fastjson.JSON;
import com.fasterxml.uuid.Generators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.UUID;

@Service
public class MessagedService {
    @Resource
    private MessagedMapper messagedMapper;

    private static final Logger LOG = LoggerFactory.getLogger(GoodsService.class);

    @Value("${picturesPath}")
    private String picturesPath;

    public void addPicMessage(String messagestr, MultipartFile img){
        Messaged message = JSON.parseObject(messagestr, Messaged.class);
        //给图片一个名字
        UUID timebaseUUID = Generators.timeBasedGenerator().generate();
        String fileName = timebaseUUID.toString()+".jpg";
        //把这个名字作为要插入的message的content
        message.setContent(fileName);
        //将图片存到文件系统里
        String filePath = picturesPath + fileName;
        try{
            File dest = new File(filePath);
            Files.copy(img.getInputStream(), dest.toPath());
        }catch (Exception e){
            LOG.info(e.getMessage());
        }
        //数据库插入
        long now = System.currentTimeMillis();
        Timestamp ts = new Timestamp(now);
        message.setSendtime(ts);
        messagedMapper.addMessage(message);
    }
}
