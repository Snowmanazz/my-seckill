package com.snow.myseckill.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sk_order")
public class SeckillOrder {
    @TableId
    private Long id;
    @TableField("user_id")
    private Long userId;
    @TableField("order_od")
    private Long  orderId;
    @TableField("goods_id")
    private Long goodsId;
}
