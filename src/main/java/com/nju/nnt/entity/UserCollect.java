package com.nju.nnt.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author LianJianheng
 * @version 1.0.0
 * @createTime 2022/6/13
 * @Description TODO
 */
@AllArgsConstructor
@Data
@TableName("user_collect_goods")
public class UserCollect
{
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String openId;
    private Long goodsId;
}
