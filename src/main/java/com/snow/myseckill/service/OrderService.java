package com.snow.myseckill.service;

import com.snow.myseckill.pojo.Goods;
import com.snow.myseckill.pojo.OrderInfo;
import com.snow.myseckill.pojo.SeckillOrder;
import com.snow.myseckill.pojo.User;

public interface OrderService {
    SeckillOrder getSeckillOrder(long uid, long gid);

    OrderInfo getOrderInfo(long oid);

    OrderInfo createOrder(User user, Goods goods);

}
