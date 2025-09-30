package ink.charter.website.server.admin.sys.controller;

import ink.charter.website.server.admin.sys.service.DictTypeService;
import ink.charter.website.server.admin.sys.converter.DictTypeConverter;
import ink.charter.website.domain.admin.api.dto.dict.CreateDictTypeDTO;
import ink.charter.website.domain.admin.api.dto.dict.UpdateDictTypeDTO;
import ink.charter.website.domain.admin.api.vo.dict.DictTypeVO;
import ink.charter.website.common.log.annotation.OperationLog;
import ink.charter.website.common.log.constant.LogConstant;
import ink.charter.website.common.core.common.Result;
import ink.charter.website.common.core.entity.sys.SysDictTypeEntity;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 字典类型管理控制器
 *
 * @author charter
 * @create 2025/09/15
 */
@Slf4j
@RestController
@RequestMapping("/sysDictType")
@RequiredArgsConstructor
@Validated
@Tag(name = "字典类型管理", description = "字典类型管理相关接口")
public class DictTypeController {

    private final DictTypeService dictTypeService;
    private final DictTypeConverter dictTypeConverter;

    /**
     * 分页查询字典类型
     */
    @GetMapping("/listPage")
    @Operation(summary = "分页查询字典类型", description = "分页查询字典类型列表")
    @OperationLog(
        module = LogConstant.OptModule.DICT,
        type = LogConstant.OptType.SELECT,
        description = "分页查询字典类型",
        recordParams = false
    )
    public Result<Page<DictTypeVO>> listPage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNo,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "字典名称") @RequestParam(required = false) String dictName,
            @Parameter(description = "字典类型") @RequestParam(required = false) String dictType,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {
        
        Page<SysDictTypeEntity> page = dictTypeService.listPage(pageNo, pageSize, dictName, dictType, status);
        
        Page<DictTypeVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<DictTypeVO> voList = page.getRecords().stream()
                .map(dictTypeConverter::toVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);
        
        return Result.success("查询成功", voPage);
    }

    /**
     * 根据ID查询字典类型
     */
    @GetMapping("/getById/{id}")
    @Operation(summary = "根据ID查询字典类型", description = "根据ID查询字典类型详细信息")
    @OperationLog(
        module = LogConstant.OptModule.DICT,
        type = LogConstant.OptType.SELECT,
        description = "查询字典类型详情",
        recordParams = false
    )
    public Result<DictTypeVO> getById(@Parameter(description = "字典类型ID", required = true) @PathVariable Long id) {
        SysDictTypeEntity dictType = dictTypeService.getById(id);
        if (dictType == null) {
            return Result.error("字典类型不存在");
        }
        
        DictTypeVO dictTypeVO = dictTypeConverter.toVO(dictType);
        return Result.success("查询成功", dictTypeVO);
    }

    /**
     * 创建字典类型
     */
    @PostMapping("/add")
    @Operation(summary = "创建字典类型", description = "创建新的字典类型")
    @OperationLog(
        module = LogConstant.OptModule.DICT,
        type = LogConstant.OptType.INSERT,
        description = "创建字典类型"
    )
    public Result<DictTypeVO> create(@RequestBody @Validated CreateDictTypeDTO createDictTypeDTO) {
        SysDictTypeEntity dictType = dictTypeConverter.toEntity(createDictTypeDTO);
        boolean success = dictTypeService.create(dictType);
        
        if (success) {
            DictTypeVO dictTypeVO = dictTypeConverter.toVO(dictType);
            return Result.success("字典类型创建成功", dictTypeVO);
        } else {
            return Result.error("字典类型创建失败，字典类型可能已存在");
        }
    }

    /**
     * 更新字典类型
     */
    @PostMapping("/update")
    @Operation(summary = "更新字典类型", description = "更新指定字典类型的信息")
    @OperationLog(
        module = LogConstant.OptModule.DICT,
        type = LogConstant.OptType.UPDATE,
        description = "更新字典类型"
    )
    public Result<DictTypeVO> update(@RequestBody @Validated UpdateDictTypeDTO updateDictTypeDTO) {
        Long id = updateDictTypeDTO.getId();
        SysDictTypeEntity existingDictType = dictTypeService.getById(id);
        if (existingDictType == null) {
            return Result.error("字典类型不存在");
        }
        
        SysDictTypeEntity dictType = dictTypeConverter.toEntity(updateDictTypeDTO);
        boolean success = dictTypeService.update(dictType);
        
        if (success) {
            SysDictTypeEntity updatedDictType = dictTypeService.getById(id);
            DictTypeVO dictTypeVO = dictTypeConverter.toVO(updatedDictType);
            return Result.success("字典类型更新成功", dictTypeVO);
        } else {
            return Result.error("字典类型更新失败，字典类型可能已存在");
        }
    }

    /**
     * 删除字典类型
     */
    @PostMapping("/deleteById/{id}")
    @Operation(summary = "删除字典类型", description = "删除指定字典类型")
    @OperationLog(
        module = LogConstant.OptModule.DICT,
        type = LogConstant.OptType.DELETE,
        description = "删除字典类型"
    )
    public Result<Void> deleteById(@Parameter(description = "字典类型ID", required = true) @PathVariable Long id) {
        SysDictTypeEntity existingDictType = dictTypeService.getById(id);
        if (existingDictType == null) {
            return Result.error("字典类型不存在");
        }
        
        boolean success = dictTypeService.deleteById(id);
        if (success) {
            return Result.success("字典类型删除成功");
        } else {
            return Result.error("字典类型删除失败");
        }
    }

    /**
     * 批量删除字典类型
     */
    @PostMapping("/batchDelete")
    @Operation(summary = "批量删除字典类型", description = "批量删除字典类型")
    @OperationLog(
        module = LogConstant.OptModule.DICT,
        type = LogConstant.OptType.DELETE,
        description = "批量删除字典类型"
    )
    public Result<Void> batchDelete(@RequestBody List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Result.error("请选择要删除的字典类型");
        }
        
        boolean success = dictTypeService.batchDelete(ids);
        if (success) {
            return Result.success("批量删除成功");
        } else {
            return Result.error("批量删除失败");
        }
    }

    /**
     * 更新字典类型状态
     */
    @PostMapping("/updateStatus/{id}/{status}")
    @Operation(summary = "更新字典类型状态", description = "启用或禁用指定字典类型")
    @OperationLog(
        module = LogConstant.OptModule.DICT,
        type = LogConstant.OptType.UPDATE,
        description = "更新字典类型状态"
    )
    public Result<Void> updateStatus(
            @Parameter(description = "字典类型ID", required = true) @PathVariable Long id,
            @Parameter(description = "状态：0-禁用，1-启用", required = true) @PathVariable Integer status) {
        
        SysDictTypeEntity existingDictType = dictTypeService.getById(id);
        if (existingDictType == null) {
            return Result.error("字典类型不存在");
        }
        
        boolean success = dictTypeService.updateStatus(id, status);
        if (success) {
            String statusText = status == 1 ? "启用" : "禁用";
            return Result.success("字典类型" + statusText + "成功");
        } else {
            return Result.error("状态更新失败");
        }
    }

    /**
     * 查询所有字典类型列表
     */
    @GetMapping("/listDictType")
    @Operation(summary = "查询所有字典类型", description = "查询所有字典类型列表")
    @OperationLog(
        module = LogConstant.OptModule.DICT,
        type = LogConstant.OptType.SELECT,
        description = "查询所有字典类型",
        recordParams = false
    )
    public Result<List<DictTypeVO>> listAll() {
        List<SysDictTypeEntity> dictTypes = dictTypeService.listAll();
        List<DictTypeVO> dictTypeVOs = dictTypes.stream()
                .map(dictTypeConverter::toVO)
                .collect(Collectors.toList());
        
        return Result.success("查询成功", dictTypeVOs);
    }
}