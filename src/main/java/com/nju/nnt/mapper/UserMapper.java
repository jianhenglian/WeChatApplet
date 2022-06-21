package com.nju.nnt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nju.nnt.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User>{
}
