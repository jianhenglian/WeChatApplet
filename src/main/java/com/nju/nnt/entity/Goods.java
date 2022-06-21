package com.nju.nnt.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
@TableName("goods")
public class Goods {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "goods_id")
    private Long goodsId;
    private String imageUrls;
    private Integer classify;
    private Double price;
    private Integer campus;
    private String weixin;
    private String phone;
    private String goodsDetail;
    private String userId;
    private Date publishTime;
}
