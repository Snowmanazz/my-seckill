package com.snow.myseckill.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("sk_goods_seckill")
public class SeckillGoods {
    @TableId
    private Long id;
    @TableField("goods_id")
    private Long goodsId;
    @TableField("stock_count")
    private Integer stockCount;
    @TableField("start_date")
    private Date startDate;
    @TableField("end_date")
    private Date endDate;
    private int version;
}
