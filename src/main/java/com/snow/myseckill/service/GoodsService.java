package com.snow.myseckill.service;

import com.snow.myseckill.vo.GoodsVo;

import java.util.List;

public interface GoodsService {
    GoodsVo findOne(long gid);

    boolean reduceGood(long gid, long count);

    List<GoodsVo> goodsList();

}
