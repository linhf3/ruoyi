package com.ruoyi.security.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.Constant;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.security.algorithm.CoreAlgorithmContet;
import com.ruoyi.security.vo.SecuritiesFutureVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.stereotype.Service;
import com.ruoyi.security.mapper.TbSecuritiesDataMapper;
import com.ruoyi.security.domain.TbSecuritiesData;
import com.ruoyi.security.service.ITbSecuritiesDataService;
import org.springframework.util.CollectionUtils;

/**
 * 证劵交易数据源Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-07-21
 */
@Service
@Slf4j
public class TbSecuritiesDataServiceImpl implements ITbSecuritiesDataService
{
    @Autowired
    private TbSecuritiesDataMapper tbSecuritiesDataMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private CoreAlgorithmContet coreAlgorithmContet;

    /**
     * 查询证劵交易数据源
     * 
     * @param id 证劵交易数据源主键
     * @return 证劵交易数据源
     */
    @Override
    public TbSecuritiesData selectTbSecuritiesDataById(Long id)
    {
        return tbSecuritiesDataMapper.selectTbSecuritiesDataById(id);
    }

    /**
     * 查询证劵交易数据源列表
     * 
     * @param tbSecuritiesData 证劵交易数据源
     * @return 证劵交易数据源
     */
    @Override
    public List<TbSecuritiesData> selectTbSecuritiesDataList(TbSecuritiesData tbSecuritiesData)
    {
        return tbSecuritiesDataMapper.selectTbSecuritiesDataList(tbSecuritiesData);
    }

    /**
     * 新增证劵交易数据源
     * 
     * @param tbSecuritiesData 证劵交易数据源
     * @return 结果
     */
    @Override
    public int insertTbSecuritiesData(TbSecuritiesData tbSecuritiesData)
    {
        redisCache.deleteObject("tbSecuritiesDataList");
        return tbSecuritiesDataMapper.insertTbSecuritiesData(tbSecuritiesData);
    }

    /**
     * 修改证劵交易数据源
     * 
     * @param tbSecuritiesData 证劵交易数据源
     * @return 结果
     */
    @Override
    public int updateTbSecuritiesData(TbSecuritiesData tbSecuritiesData)
    {
        redisCache.deleteObject("tbSecuritiesDataList");
        return tbSecuritiesDataMapper.updateTbSecuritiesData(tbSecuritiesData);
    }

    /**
     * 批量删除证劵交易数据源
     * 
     * @param ids 需要删除的证劵交易数据源主键
     * @return 结果
     */
    @Override
    public int deleteTbSecuritiesDataByIds(Long[] ids)
    {
        redisCache.deleteObject("tbSecuritiesDataList");
        return tbSecuritiesDataMapper.deleteTbSecuritiesDataByIds(ids);
    }

    /**
     * 删除证劵交易数据源信息
     * 
     * @param id 证劵交易数据源主键
     * @return 结果
     */
    @Override
    public int deleteTbSecuritiesDataById(Long id)
    {
        return tbSecuritiesDataMapper.deleteTbSecuritiesDataById(id);
    }

    /**
     * 爬取证劵交易数据
     * @return boolean
     */
    @Override
    public boolean crawl() {
        List<String> listF = Arrays.asList("103", "112", "113", "114", "115");
        LinkedList<TbSecuritiesData> tbSecuritiesDataLinkedList = new LinkedList<>();
        listF.forEach(s -> {
            Map urlMap = new HashMap<>();
            urlMap.put("place", s);
            //发送http请求
            String rx = null;
            try {
                rx = HttpUtils.sendGet(new StrSubstitutor(urlMap).replace(Constant.FUTURESMAINFORCEURL).toString());
            } catch (Exception e) {
                log.error("爬取数据异常：",e);
            }
            Map node = (Map) JSON.parse(rx);
            List<Map<String, Object>> list = (List<Map<String, Object>>) node.get("list");
            //数据集
            System.out.println(list.toArray());
            list.forEach(map -> {
                String name = (String) map.get("name");
                if (name.contains("主") && !name.contains("次")) {
                    TbSecuritiesData tbSecuritiesData = new TbSecuritiesData();
                    if ("103".equals(s)) {
                        name = new StringBuilder(name).append("(美)").toString();
                    }
                    tbSecuritiesData.setName(name);
                    String dm = (String) map.get("dm");
                    tbSecuritiesData.setCode(dm);
                    tbSecuritiesData.setExchangeCode(new StringBuilder(s).append(".").append(dm).toString());
                    tbSecuritiesData.setDeviation(90.0);
                    tbSecuritiesData.setType(1);
                    tbSecuritiesData.setStatus(0);
                    tbSecuritiesData.setAddUser("admin");
                    tbSecuritiesData.setAddTime(new Date());
                    tbSecuritiesDataLinkedList.add(tbSecuritiesData);
                }
            });
        });
        if (!CollectionUtils.isEmpty(tbSecuritiesDataLinkedList)){
            tbSecuritiesDataMapper.deleteAll();
            tbSecuritiesDataMapper.insertList(tbSecuritiesDataLinkedList);
        }
        return true;
    }

    @Override
    public List<SecuritiesFutureVo> findList() {
        //1.查询有效配置
        List<TbSecuritiesData> tbSecuritiesDataList = redisCache.getCacheList("tbSecuritiesDataList");
        if (CollectionUtils.isEmpty(tbSecuritiesDataList)){
            TbSecuritiesData tbSecuritiesData = new TbSecuritiesData();
            tbSecuritiesData.setType(1);
            tbSecuritiesData.setStatus(0);
            tbSecuritiesDataList = tbSecuritiesDataMapper.selectTbSecuritiesDataList(tbSecuritiesData);
            if (!CollectionUtils.isEmpty(tbSecuritiesDataList)){
                redisCache.setCacheList("tbSecuritiesDataList",tbSecuritiesDataList);
            }
        }
        if (CollectionUtils.isEmpty(tbSecuritiesDataList)){
            return null;
        }

        //2.循环爬数据并计算封装到list中
        List<SecuritiesFutureVo> list = new LinkedList<>();
        long startTime = System.currentTimeMillis();
        for (TbSecuritiesData tbSecuritiesData : tbSecuritiesDataList) {
            //拼接地址
            Map urlMap = new HashMap<>();
            urlMap.put("futuresUrl", tbSecuritiesData.getExchangeCode());
            //发送http请求
            String rx = HttpUtils.sendGet(new StrSubstitutor(urlMap).replace(Constant.futuresUrl));
            Map node = (Map) JSON.parse(rx);
            //数据集
            Map map = (Map) node.get("data");
            List trendsM = (List) map.get("trends");
            //白天开盘，无数据的情况（晚上）
            if (CollectionUtils.isEmpty(trendsM)) {
                continue;
            }
            SecuritiesFutureVo securitiesFutureVo = new SecuritiesFutureVo();
            securitiesFutureVo.setCode(tbSecuritiesData.getCode());
            securitiesFutureVo.setName(tbSecuritiesData.getName());
            securitiesFutureVo.setExchangeCode(tbSecuritiesData.getExchangeCode());
            Map<String, Object> reMap = coreAlgorithmContet.deviationTheDayRate("futuresCoreAlgorithm", map);
            String proportion = (String) reMap.get("proportion");
            Double proportionDouble = Double.valueOf(proportion.replace("+", "").replace("%", ""));
            securitiesFutureVo.setProportion(proportion);
            Double dailySpread = (Double) reMap.get("dailySpread");
            Double price = (Double) reMap.get("price");
            securitiesFutureVo.setDailySpread(dailySpread);
            securitiesFutureVo.setPrice(price);
            securitiesFutureVo.setUndulate(tbSecuritiesData.getUndulate());
            //获取策略，上下偏离
            Double deviation = null == tbSecuritiesData.getDeviation() || 0 == tbSecuritiesData.getDeviation()?100:tbSecuritiesData.getDeviation();
            //点数振幅
            //Double undulate = null == tbSecuritiesData.getUndulate() || 0 == tbSecuritiesData.getUndulate()?0:tbSecuritiesData.getUndulate();
            if (proportionDouble <= -deviation) {
                securitiesFutureVo.setPositiveNegativeFlag(-1);
                reMap.put("positiveNegativeFlag", -1);
            } else if (proportionDouble >= deviation) {
                securitiesFutureVo.setPositiveNegativeFlag(1);
            } else {
                securitiesFutureVo.setPositiveNegativeFlag(0);
            }
            list.add(securitiesFutureVo);
        }
        List<SecuritiesFutureVo> collectList = list.stream().sorted(Comparator.comparing(SecuritiesFutureVo::getDailySpread,Comparator.reverseOrder())).collect(Collectors.toList());
        long endTime = System.currentTimeMillis();
        log.debug("执行时长：{}", endTime - startTime);
        log.debug("期货：{}", collectList);
        return collectList;
    }
}
