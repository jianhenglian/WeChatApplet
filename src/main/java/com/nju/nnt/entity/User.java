package com.nju.nnt.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
@TableName("user")
public class User {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "open_id")
    private String openId;
    private String gender;
    private String nickname;
    private String avatarUrl;
}
