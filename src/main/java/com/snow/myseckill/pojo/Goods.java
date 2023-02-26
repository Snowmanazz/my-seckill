package com.snow.myseckill.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sk_goods")
public class Goods {
    @TableId
    private Long id;
    @TableField("goods_name")
    private String goodsName;
    @TableField("goods_title")
    private String goodsTitle;
    @TableField("goods_img")
    private String goodsImg;
    @TableField("goods_detail")
    private String goodsDetail;
    @TableField("goods_price")
    private Double goodsPrice;
    @TableField("goods_stock")
    private Integer goodsStock;
}
