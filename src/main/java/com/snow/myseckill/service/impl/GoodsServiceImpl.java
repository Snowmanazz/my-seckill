package com.snow.myseckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.snow.myseckill.mapper.GoodsMapper;
import com.snow.myseckill.pojo.Goods;
import com.snow.myseckill.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public Goods findOne(long gid) {
        return goodsMapper.selectById(gid);
    }

    @Override
    public List<Goods> goodsList() {
        return goodsMapper.selectList(new QueryWrapper<>());
    }

    /**
     * 最普通的减少
     *
     * @param gid
     * @param count
     * @return
     */
    @Override
    public boolean reduceGood(long gid, long count) {
        //减少库存
        return goodsMapper.reduceGoods(gid, count) > 0;
    }


}
