package com.snow.myseckill.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("sk_order_info")
public class OrderInfo {
    @TableId
    private Long id;
    @TableField("user_id")
    private Long userId;
    @TableField("goods_id")
    private Long goodsId;
    @TableField("delivery_addr_id")
    private Long  deliveryAddrId;
    @TableField("goods_name")
    private String goodsName;
    @TableField("goods_count")
    private Integer goodsCount;
    @TableField("goods_price")
    private Double goodsPrice;
    @TableField("order_channel")
    private Integer orderChannel;
    private Integer status;
    @TableField("create_date")
    private Date createDate;
    @TableField("pay_date")
    private Date payDate;
}
