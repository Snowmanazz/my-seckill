package com.snow.myseckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.snow.myseckill.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface SecKillGoodsMapper extends BaseMapper<GoodsVo> {

    @Select("select g.*, sg.stock_count, sg.start_date, sg.end_date, sg.seckill_price, sg.version from sk_goods_seckill sg left join sk_goods g on sg.goods_id = g.id")
    List<GoodsVo> listGoodsVo();

    @Update("update sk_goods_seckill set stock_count = stock_count - ${count} where goods_id = ${gid} and stock_count - ${count} >= 0 ")
    Integer reduceGoods(@Param("gid") long gid, @Param("count") long count);

    @Select("select g.*, sg.stock_count, sg.start_date, sg.end_date, sg.seckill_price, sg.version  from sk_goods_seckill sg left join sk_goods g  on sg.goods_id = g.id where g.id = #{goodsId}")
    GoodsVo getGoodsVoByGoodsId(@Param("goodsId") long goodsId);


}
