package com.snow.myseckill.service.impl;

import com.snow.myseckill.mapper.OrderMapper;
import com.snow.myseckill.pojo.Goods;
import com.snow.myseckill.pojo.OrderInfo;
import com.snow.myseckill.pojo.SeckillOrder;
import com.snow.myseckill.pojo.User;
import com.snow.myseckill.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public SeckillOrder getSeckillOrder(long uid, long gid) {
        return orderMapper.getOrderByugid(uid, gid);
    }

    @Override
    public OrderInfo getOrderInfo(long oid) {
        return orderMapper.selectById(oid);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderInfo createOrder(User user, Goods goods) {
        log.info("开始创建订单---------");
        log.info("user-[{}],\ngoods-[{}]", user, goods);
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getGoodsPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        orderMapper.insert(orderInfo);
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setGoodsId(goods.getId());
        seckillOrder.setOrderId(orderInfo.getId());
        seckillOrder.setUserId(user.getId());
        orderMapper.insertSeckillOrder(seckillOrder);
        return orderInfo;
    }
}
