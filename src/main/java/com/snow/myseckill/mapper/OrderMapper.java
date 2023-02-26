package com.snow.myseckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.snow.myseckill.pojo.OrderInfo;
import com.snow.myseckill.pojo.SeckillOrder;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderMapper extends BaseMapper<OrderInfo> {

    @Select("select * from sk_order where user_id = #{userId} and goods_id = #{goodsId}")
    SeckillOrder getOrderByugid(@Param("userId") long userId, @Param("goodsId") long goodsId);

    @Insert("insert into sk_order (user_id, goods_id, order_id)values(#{userId}, #{goodsId}, #{orderId})")
    int insertSeckillOrder(SeckillOrder order);
}
