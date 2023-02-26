package com.snow.myseckill.service;

import com.snow.myseckill.pojo.Goods;

import java.util.List;

public interface GoodsService {
    Goods findOne(long gid);

    boolean reduceGood(long gid, long count);

    List<Goods> goodsList();

}
