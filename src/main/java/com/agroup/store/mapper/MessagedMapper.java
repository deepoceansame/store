package com.agroup.store.mapper;

import com.agroup.store.domain.Messaged;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MessagedMapper {
    int addMessage(Messaged messaged);

    List<Messaged> getMessageListByThreeId(@Param("senderid") Integer senderid, @Param("receiverid") Integer receiverid, @Param("desiredgoodsid") Integer desiredgoodsid);
}
