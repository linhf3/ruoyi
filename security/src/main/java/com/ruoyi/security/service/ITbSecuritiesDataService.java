package com.ruoyi.security.service;

import java.util.List;
import com.ruoyi.security.domain.TbSecuritiesData;
import com.ruoyi.security.vo.SecuritiesFutureVo;

/**
 * 证劵交易数据源Service接口
 * 
 * @author ruoyi
 * @date 2024-07-21
 */
public interface ITbSecuritiesDataService 
{
    /**
     * 查询证劵交易数据源
     * 
     * @param id 证劵交易数据源主键
     * @return 证劵交易数据源
     */
    public TbSecuritiesData selectTbSecuritiesDataById(Long id);

    /**
     * 查询证劵交易数据源列表
     * 
     * @param tbSecuritiesData 证劵交易数据源
     * @return 证劵交易数据源集合
     */
    public List<TbSecuritiesData> selectTbSecuritiesDataList(TbSecuritiesData tbSecuritiesData);

    /**
     * 新增证劵交易数据源
     * 
     * @param tbSecuritiesData 证劵交易数据源
     * @return 结果
     */
    public int insertTbSecuritiesData(TbSecuritiesData tbSecuritiesData);

    /**
     * 修改证劵交易数据源
     * 
     * @param tbSecuritiesData 证劵交易数据源
     * @return 结果
     */
    public int updateTbSecuritiesData(TbSecuritiesData tbSecuritiesData);

    /**
     * 批量删除证劵交易数据源
     * 
     * @param ids 需要删除的证劵交易数据源主键集合
     * @return 结果
     */
    public int deleteTbSecuritiesDataByIds(Long[] ids);

    /**
     * 删除证劵交易数据源信息
     * 
     * @param id 证劵交易数据源主键
     * @return 结果
     */
    public int deleteTbSecuritiesDataById(Long id);

    /**
     * 爬取证劵交易数据
     */
    boolean crawl();

    /**
     * 查询实时期货分析数据
     */
    List<SecuritiesFutureVo> findList();
}
