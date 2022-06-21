package com.nju.nnt.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("message")
public class Message {
    @TableId(value = "msg_id")
    Long msgId;
    String fromId;
    String fromAvatar;
    String fromName;
    String toId;
    String toAvatar;
    String toName;
    String msg;
    String k;
}
