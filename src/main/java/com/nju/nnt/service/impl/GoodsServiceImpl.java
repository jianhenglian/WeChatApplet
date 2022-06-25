package com.nju.nnt.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nju.nnt.entity.Goods;
import com.nju.nnt.entity.UserCollect;
import com.nju.nnt.mapper.GoodsMapper;
import com.nju.nnt.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    GoodsMapper goodsMapper;

    @Autowired
    private RestHighLevelClient client;

    @Override
    public void publishGoods(Goods goods) {
        //es建立倒排索引
        String goods_id = String.valueOf(goods.getGoodsId());
        //1.获取操作文档的对象
        IndexRequest request = new IndexRequest("nnt_goods").id(goods_id).source(JSON.toJSONString(goods),XContentType.JSON);
        //添加数据，获取结果
        IndexResponse response = null;
        try {
            response = client.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("es添加文档失败");
            e.printStackTrace();
        }
        log.info("es执行结果:{}",response);
        goodsMapper.insert(goods);
    }

    @Override
    public List<Goods> getGoodsList(int page, int size, Map<String, Object> conditionMap) {
        //2. 构建查询请求对象，指定查询的索引名称
//        SearchRequest searchRequest = new SearchRequest("nnt_goods");
//        //4. 创建查询条件构建器SearchSourceBuilder
//        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//
//        //6. 查询条件
//        QueryBuilder query = QueryBuilders.matchQuery("goodsDetail",conditionMap.get("keyword"));//查询所有文档
//        //5. 指定查询条件
//        sourceBuilder.query(query);
//
//        //3. 添加查询条件构建器 SearchSourceBuilder
//        searchRequest.source(sourceBuilder);
//
//        // 8 . 添加分页信息
//        sourceBuilder.from((page-1)*size);
//        sourceBuilder.size(size);
//
//        //1. 查询,获取查询结果
//        SearchResponse searchResponse = null;
//        try {
//            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
//        } catch (IOException e) {
//            log.error("查询失败");
//            e.printStackTrace();
//        }
//
//        //7. 获取命中对象 SearchHits
//        SearchHits searchHits = searchResponse.getHits();
//        List<Goods> goodsList = new ArrayList<>();
//        //7.2 获取Hits数据  数组
//        SearchHit[] hits = searchHits.getHits();
//        for (SearchHit hit : hits) {
//            //获取json字符串格式的数据
//            String sourceAsString = hit.getSourceAsString();
//            //转为java对象
//            Goods goods = JSON.parseObject(sourceAsString, Goods.class);
//            goods.setImageUrls("http://139.196.157.116:9000/"+goods.getImageUrls().split("-")[0]);
//            goodsList.add(goods);
//        }

        String keyWord = conditionMap.get("keyword").toString();
        log.info("keyword: {}", keyWord);
        List<Goods> goods1 = goodsMapper.selectList(new QueryWrapper<>());
        goods1 = goods1.stream().filter(item -> item.getGoodsDetail().contains(keyWord)).collect(Collectors.toList());

        for (Goods goods : goods1) {
            System.out.println(goods);
        }
        return goods1;
    }


    @Override
    public IPage<Goods> listGoodsPerPage(int page, int size) {
        Page<Goods> items = new Page<>(page,size);
        QueryWrapper<Goods> wapper = new QueryWrapper<>();
        Page<Goods> ress = goodsMapper.selectPage(items, wapper);
        return ress;
    }

    @Override
    public Goods getGoodsDetail(Long goodsId) {
        Goods goods = goodsMapper.selectById(goodsId);
        return goods;
    }

    @Override
    public List<Goods> listAllCollectGoods(List<UserCollect> info, int page, int size)
    {
//        现在这里我们其实根本没有考虑page和size的作用，emm
        List<Goods> result = new ArrayList<>();
        log.info(info.toString());
        info.forEach(item -> {
            Goods goods = goodsMapper.selectById(item.getGoodsId());
            if(goods!=null) result.add(goods);
        });
        log.info(result.toString());
        return result;
    }

    @Override
    public List<Goods> listAllPublishGoods(String userId)
    {
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return goodsMapper.selectList(queryWrapper);
    }

    @Override
    public List<Goods> listAllGoodsByClassify(int classify)
    {
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("classify", classify);
        return goodsMapper.selectList(queryWrapper);
    }

    @Override
    public List<Goods> listAllGoodsByTime()
    {
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("publish_time");
        return goodsMapper.selectList(queryWrapper);
    }
}
