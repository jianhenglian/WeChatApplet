package com.nju.nnt.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nju.nnt.common.*;
import com.nju.nnt.entity.Goods;
import com.nju.nnt.entity.User;
import com.nju.nnt.service.GoodsService;
import com.nju.nnt.websocket.WSServer;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.HashAttributeSet;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    GoodsService goodsService;




    @RequestMapping("/upload")
    public Response upLoadImage(@RequestParam("file") MultipartFile file, @RequestParam("token")String token){
        log.info("用户token为{}",token);
        User infoFromToken;
        try {
            infoFromToken = JWTUtil.getInfoFromToken(token);
        } catch (Exception e) {
            return Response.error("token无效，请重新登录");
        }
        log.info("用户信息为：{}",infoFromToken);
        String url;
        try {
           url = MinIOUtils.upLoadFile(file,infoFromToken);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("文件上传失败");
            return Response.error("文件上传失败");
        }

        return Response.success(url,"文件上传成功");
    }

    @RequestMapping("/publish")
    public Response publishGoods(@RequestBody JSONObject data){
        log.info("check params");
        String lossParams = CheckParams.check(data,new String[]{
                "imageValue", "classify","price"
                ,"campus","weixin","phone","goodsDetail"
                ,"token"
        });
        if (!"".equals(lossParams)){
            log.error("参数类型不匹配,缺少参数："+lossParams);
            return Response.error("参数类型不匹配,缺少参数："+lossParams);
        }

        log.info("接收到的数据为：{}",data);

        JSONArray imageValue = data.getJSONArray("imageValue");
        StringBuilder imageUrlsBuilder = new StringBuilder();
        for (Object o : imageValue) {
            if(((String)o).equals("null")){
                continue;
            }
            imageUrlsBuilder.append((String) o).append("-");
        }
        log.info("商品图片url为: {}",imageValue);
        User user = null;
        try {
            user = JWTUtil.getInfoFromToken(data.getString("token"));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("token无效,请登录");
            return Response.error(401,"token无效,请登录");
        }

        Goods goods = new Goods();
        goods.setImageUrls(imageUrlsBuilder.substring(0,imageUrlsBuilder.length()-1));
        goods.setGoodsDetail(data.getString("goodsDetail"));
        goods.setCampus(data.getInteger("campus"));
        goods.setPrice(Double.valueOf(data.getString("price")));
        goods.setClassify(data.getInteger("classify"));
        goods.setWeixin(data.getString("weixin"));
        goods.setPhone(data.getString("phone"));
        goods.setUserId(user.getOpenId());
        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
        goods.setGoodsId(idWorker.nextId());
        goods.setPublishTime(new Date(System.currentTimeMillis()));
        goodsService.publishGoods(goods);
        return Response.success("发布成功");
    }

    @RequestMapping("/listAllGoods")
    public Response listAllGoods(@RequestBody JSONObject data){
        log.info("check params");
        String lossParams = CheckParams.check(data,new String[]{
                "page"
        });
        log.info("请求的页码:{}",data);
        if (!"".equals(lossParams)){
            log.error("参数类型不匹配,缺少参数："+lossParams);
            return Response.error("参数类型不匹配,缺少参数："+lossParams);
        }
        int page = data.getInteger("page");
        int pageSize = 10;
        IPage<Goods> goodsIPage = goodsService.listGoodsPerPage(page, pageSize);
        log.info("货物列表:{}",goodsIPage);

        return Response.success(goodsIPage);
    }

    @RequestMapping( "/imageUrl")
    public void infoHe(HttpServletResponse response,@RequestBody JSONObject data) {
        log.info("check params");
        String lossParams = CheckParams.check(data,new String[]{
                "imageUrl"
        });
        if (!"".equals(lossParams)){
            log.error("参数类型不匹配,缺少参数："+lossParams);
            return;
        }
        log.info("data = {}",data);
        String urls = data.getString("imageUrl");

        String url = urls.split("-")[0];
        log.info("url = {}",url);
        String bucketName = url.split("/")[0];
        String imagePath = url.split("/")[1];
        InputStream in = null;
        try{
            //从minio文件服务器上获取图片流
            in = MinIOUtils.getImageStream(bucketName,imagePath);
            response.setContentType("image/png");
            OutputStream out = response.getOutputStream();
            byte[] buff = new byte[100];
            int rc = 0;
            while ((rc = in.read(buff, 0, 100)) > 0) {
                out.write(buff, 0, rc);
            }
            out.flush();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @RequestMapping( "/detail")
    public Response getGoodsDetail(@RequestParam Long goodsId){
        Goods goodsDetail = goodsService.getGoodsDetail(goodsId);
        log.info("goodsDetail: {}",goodsDetail);
        return Response.success(goodsDetail);
    }

    @RequestMapping("/getGoodsList")
    public Response getGoodsList(@RequestBody JSONObject data){
        log.info("check params");
        String lossParams = CheckParams.check(data,new String[]{
                "query","pagenum","pagesize"
        });
        if (!"".equals(lossParams)){
            log.error("参数类型不匹配,缺少参数："+lossParams);
            return Response.error("参数类型不匹配,缺少参数："+lossParams);
        }

        log.info("接收到的数据为：{}",data);
        String keyword = data.getString("query");
        int pageNum = data.getInteger("pagenum");
        int pageSize = data.getInteger("pagesize");
        Map<String,Object> map = new HashMap<>();
        map.put("keyword",keyword);
        List<Goods> goodsList = goodsService.getGoodsList(pageNum, pageSize, map);
        return Response.success(goodsList);
    }

    @RequestMapping("/getAllPublish")
    public Response getAllPublish(@RequestParam String userId, @RequestParam int page)
    {
        log.info("userId: {}", userId);
        log.info("page: {}", page);
        return Response.success(goodsService.listAllPublishGoods(userId));
    }


    @RequestMapping("/getAllGoodsByClassify")
    public Response getAllPublish(@RequestParam int classIndex, @RequestParam int page)
    {
        log.info("classify: {}", classIndex);
        return Response.success(goodsService.listAllGoodsByClassify(classIndex));
    }

    @RequestMapping("/getAllGoodsOrderByTime")
    public Response getAllGoodsOrderByTime()
    {
        return Response.success(goodsService.listAllGoodsByTime());
    }

}
