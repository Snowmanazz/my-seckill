package com.snow.myseckill.vo;


import com.snow.myseckill.pojo.OrderInfo;
import lombok.Data;

@Data
public class OrderDetailVo {
    private GoodsVo goods;
    private OrderInfo order;
}
