package com.nju.nnt.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author LianJianheng
 * @version 1.0.0
 * @createTime 2022/6/30
 * @Description TODO
 */
@Data
@TableName("image")
public class Image
{
    @TableId("image_url")
    private String imageUrl;
}
