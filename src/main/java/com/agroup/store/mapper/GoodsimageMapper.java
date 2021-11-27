package com.agroup.store.mapper;

import com.agroup.store.domain.Goodsimage;

import java.util.List;

public interface GoodsimageMapper {
    int insert(Goodsimage goodsimage);

    List<String> getimages(Integer goodsid);
}
