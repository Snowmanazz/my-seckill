package com.snow.myseckill.service.impl;

import com.snow.myseckill.mapper.SecKillGoodsMapper;

import com.snow.myseckill.service.GoodsService;
import com.snow.myseckill.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private SecKillGoodsMapper goodsMapper;

    @Override
    public GoodsVo findOne(long gid) {
        return goodsMapper.getGoodsVoByGoodsId(gid);
    }

    @Override
    public List<GoodsVo> goodsList() {
        return goodsMapper.listGoodsVo();
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
        Integer reduce = goodsMapper.reduceGoods(gid, count);
        return reduce != null && reduce > 0;
    }

}
