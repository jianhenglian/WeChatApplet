package com.nju.nnt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nju.nnt.entity.Message;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageMapper extends BaseMapper<Message> {
}
