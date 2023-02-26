package com.snow.myseckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.snow.myseckill.pojo.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {

    @Select("update sk_goods_seckill set stock_count = stock_count - ${count} where goods_id =  #{gid} and stock_count - ${count} >= 0 ")
    int reduceGoods(@Param("gid") long gid, @Param("count") long count);
}
