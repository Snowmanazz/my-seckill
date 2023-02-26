package com.snow.myseckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.snow.myseckill.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMappper extends BaseMapper<User> {
}
