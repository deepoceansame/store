package com.agroup.store.mapper;

import com.agroup.store.domain.Goods;
import com.agroup.store.domain.GoodsExample;
import com.agroup.store.domain.Message;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MessageMapper {
    int addMessage(Message message);

    List<Message> getMessageListByThreeId(@Param("senderid") Integer senderid, @Param("receiverid") Integer receiverid, @Param("goodsid") Integer goodsid);
}
