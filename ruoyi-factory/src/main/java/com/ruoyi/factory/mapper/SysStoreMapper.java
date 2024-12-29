package com.ruoyi.factory.mapper;

import java.util.List;
import com.ruoyi.factory.domain.SysStore;

/**
 * 库存Mapper接口
 *
 * @author linhognfei
 * @date 2024-12-26
 */
public interface SysStoreMapper
{
    /**
     * 查询库存
     *
     * @param id 库存主键
     * @return 库存
     */
    public SysStore selectSysStoreById(Long id);

    /**
     * 查询库存列表
     *
     * @param sysStore 库存
     * @return 库存集合
     */
    public List<SysStore> selectSysStoreList(SysStore sysStore);

    /**
     * 新增库存
     *
     * @param sysStore 库存
     * @return 结果
     */
    public int insertSysStore(SysStore sysStore);

    /**
     * 修改库存
     *
     * @param sysStore 库存
     * @return 结果
     */
    public int updateSysStore(SysStore sysStore);

    /**
     * 删除库存
     *
     * @param id 库存主键
     * @return 结果
     */
    public int deleteSysStoreById(Long id);

    /**
     * 批量删除库存
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysStoreByIds(Long[] ids);
}
