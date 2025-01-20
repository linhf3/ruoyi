package com.ruoyi.security.service.impl;

import java.util.*;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.Constant;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.security.vo.SecuritiesFutureVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.security.mapper.TbSecuritiesDataMapper;
import com.ruoyi.security.domain.TbSecuritiesData;
import com.ruoyi.security.service.ITbSecuritiesDataService;
import org.springframework.util.CollectionUtils;

/**
 * 证劵交易Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-01-12
 */
@Service
@Slf4j
public class TbSecuritiesDataServiceImpl implements ITbSecuritiesDataService 
{
    @Autowired
    private TbSecuritiesDataMapper tbSecuritiesDataMapper;

    @Autowired
    private RedisCache redisCache;

    /**
     * 查询证劵交易
     * 
     * @param id 证劵交易主键
     * @return 证劵交易
     */
    @Override
    public TbSecuritiesData selectTbSecuritiesDataById(Long id)
    {
        return tbSecuritiesDataMapper.selectTbSecuritiesDataById(id);
    }

    /**
     * 查询证劵交易列表
     * 
     * @param tbSecuritiesData 证劵交易
     * @return 证劵交易
     */
    @Override
    public List<TbSecuritiesData> selectTbSecuritiesDataList(TbSecuritiesData tbSecuritiesData)
    {
        return tbSecuritiesDataMapper.selectTbSecuritiesDataList(tbSecuritiesData);
    }

    /**
     * 新增证劵交易
     * 
     * @param tbSecuritiesData 证劵交易
     * @return 结果
     */
    @Override
    public int insertTbSecuritiesData(TbSecuritiesData tbSecuritiesData)
    {
        redisCache.deleteObject("tbSecuritiesDataList");
        return tbSecuritiesDataMapper.insertTbSecuritiesData(tbSecuritiesData);
    }

    /**
     * 修改证劵交易
     * 
     * @param tbSecuritiesData 证劵交易
     * @return 结果
     */
    @Override
    public int updateTbSecuritiesData(TbSecuritiesData tbSecuritiesData)
    {
        redisCache.deleteObject("tbSecuritiesDataList");
        return tbSecuritiesDataMapper.updateTbSecuritiesData(tbSecuritiesData);
    }

    /**
     * 批量删除证劵交易
     * 
     * @param ids 需要删除的证劵交易主键
     * @return 结果
     */
    @Override
    public int deleteTbSecuritiesDataByIds(Long[] ids)
    {
        redisCache.deleteObject("tbSecuritiesDataList");
        return tbSecuritiesDataMapper.deleteTbSecuritiesDataByIds(ids);
    }

    /**
     * 删除证劵交易信息
     * 
     * @param id 证劵交易主键
     * @return 结果
     */
    @Override
    public int deleteTbSecuritiesDataById(Long id)
    {
        redisCache.deleteObject("tbSecuritiesDataList");
        return tbSecuritiesDataMapper.deleteTbSecuritiesDataById(id);
    }

    /**
     * 爬取证劵交易数据
     * @return boolean
     */
    @Override
    public boolean crawl() {
        List<String> listF = Arrays.asList("103", "112", "113", "114", "115");
        //List<String> listF = Arrays.asList("115");
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
                if ((name.contains("主") && !name.contains("次")) || name.contains("250") || name.contains("碱")) {
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
                    tbSecuritiesData.setStatus(1);
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
        List<SecuritiesFutureVo> securitiesFutureVoList = redisCache.getCacheMapValue("money","securitiesFutureVoList");
        return CollectionUtils.isEmpty(securitiesFutureVoList)?new ArrayList<>():securitiesFutureVoList;
    }
}
