package com.ruoyi.factory.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.factory.domain.SysStore;
import com.ruoyi.factory.service.ISysStoreService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 库存Controller
 *
 * @author linhognfei
 * @date 2024-12-26
 */
@RestController
@RequestMapping("/factory/store")
public class SysStoreController extends BaseController
{
    @Autowired
    private ISysStoreService sysStoreService;

    /**
     * 查询库存列表
     */
    @PreAuthorize("@ss.hasPermi('factory:store:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysStore sysStore)
    {
        startPage();
        List<SysStore> list = sysStoreService.selectSysStoreList(sysStore);
        return getDataTable(list);
    }

    /**
     * 导出库存列表
     */
    @PreAuthorize("@ss.hasPermi('factory:store:export')")
    @Log(title = "库存", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysStore sysStore)
    {
        List<SysStore> list = sysStoreService.selectSysStoreList(sysStore);
        ExcelUtil<SysStore> util = new ExcelUtil<SysStore>(SysStore.class);
        util.exportExcel(response, list, "库存数据");
    }

    /**
     * 获取库存详细信息
     */
    @PreAuthorize("@ss.hasPermi('factory:store:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(sysStoreService.selectSysStoreById(id));
    }

    /**
     * 新增库存
     */
    @PreAuthorize("@ss.hasPermi('factory:store:add')")
    @Log(title = "库存", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysStore sysStore)
    {
        return toAjax(sysStoreService.insertSysStore(sysStore));
    }

    /**
     * 修改库存
     */
    @PreAuthorize("@ss.hasPermi('factory:store:edit')")
    @Log(title = "库存", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysStore sysStore)
    {
        return toAjax(sysStoreService.updateSysStore(sysStore));
    }

    /**
     * 删除库存
     */
    @PreAuthorize("@ss.hasPermi('factory:store:remove')")
    @Log(title = "库存", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sysStoreService.deleteSysStoreByIds(ids));
    }
}
