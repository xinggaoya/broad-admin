package com.broad.web.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.broad.framework.web.controller.BaseController;
import com.broad.framework.web.entity.ResultData;
import com.broad.framework.web.page.TableDataInfo;
import com.broad.system.entity.SysDictType;
import com.broad.system.service.SysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 字典类型表(SysDictType)表控制层
 *
 * @author XingGao
 * @since 2022-10-13 15:00:02
 */
@RestController
@RequestMapping("sysDictType")
public class SysDictTypeController extends BaseController {
    /**
     * 服务对象
     */
    @Autowired
    private SysDictTypeService sysDictTypeService;

    /**
     * 分页查询所有数据
     *
     * @param sysDictType 查询实体
     * @return 所有数据
     */
    @GetMapping
    public TableDataInfo selectAll(SysDictType sysDictType) {
        startPage();
        return getDataTable(this.sysDictTypeService.list(new QueryWrapper<>(sysDictType)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public ResultData selectOne(@PathVariable Serializable id) {
        return ResultData.success(this.sysDictTypeService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param sysDictType 实体对象
     * @return 新增结果
     */
    @PostMapping
    public ResultData insert(@RequestBody SysDictType sysDictType) {
        return ResultData.success(this.sysDictTypeService.save(sysDictType));
    }

    /**
     * 修改数据
     *
     * @param sysDictType 实体对象
     * @return 修改结果
     */
    @PutMapping
    public ResultData update(@RequestBody SysDictType sysDictType) {
        return ResultData.success(this.sysDictTypeService.updateById(sysDictType));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public ResultData delete(@RequestParam("idList") List<Long> idList) {
        return ResultData.success(this.sysDictTypeService.removeByIds(idList));
    }
}
