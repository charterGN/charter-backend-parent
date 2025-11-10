package ink.charter.website.server.admin.sys.controller;

import ink.charter.website.server.admin.sys.service.DictDataService;
import ink.charter.website.server.admin.sys.service.DictTypeService;
import ink.charter.website.server.admin.sys.converter.DictDataConverter;
import ink.charter.website.domain.admin.api.dto.dict.CreateDictDataDTO;
import ink.charter.website.domain.admin.api.dto.dict.UpdateDictDataDTO;
import ink.charter.website.domain.admin.api.vo.dict.DictDataVO;
import ink.charter.website.common.log.annotation.OperationLog;
import ink.charter.website.common.log.constant.LogConstant;
import ink.charter.website.common.core.common.Result;
import ink.charter.website.common.core.entity.sys.SysDictDataEntity;
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
 * 字典数据管理控制器
 *
 * @author charter
 * @create 2025/09/15
 */
@Slf4j
@RestController
@RequestMapping("/sysDictData")
@RequiredArgsConstructor
@Validated
@Tag(name = "字典数据管理", description = "字典数据管理相关接口")
public class DictDataController {

    private final DictDataService dictDataService;
    private final DictTypeService dictTypeService;
    private final DictDataConverter dictDataConverter;

    /**
     * 查询字典数据
     */
    @GetMapping("/list")
    @Operation(summary = "查询字典数据", description = "查询字典数据列表")
    @OperationLog(
        module = LogConstant.OptModule.DICT,
        type = LogConstant.OptType.SELECT,
        description = "查询字典数据",
        recordParams = false
    )
    public Result<List<DictDataVO>> list(
            @Parameter(description = "字典类型") @RequestParam(required = false) String dictType,
            @Parameter(description = "字典标签") @RequestParam(required = false) String dictLabel,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {
        
        List<SysDictDataEntity> list = dictDataService.list(dictType, dictLabel, status);
        
        List<DictDataVO> voList = list.stream()
                .map(dictDataConverter::toVO)
                .collect(Collectors.toList());

        return Result.success("查询成功", voList);
    }

    /**
     * 根据ID查询字典数据
     */
    @GetMapping("/getById/{id}")
    @Operation(summary = "根据ID查询字典数据", description = "根据ID查询字典数据详细信息")
    @OperationLog(
        module = LogConstant.OptModule.DICT,
        type = LogConstant.OptType.SELECT,
        description = "查询字典数据详情",
        recordParams = false
    )
    public Result<DictDataVO> getById(@Parameter(description = "字典数据ID", required = true) @PathVariable Long id) {
        SysDictDataEntity dictData = dictDataService.getById(id);
        if (dictData == null) {
            return Result.error("字典数据不存在");
        }
        
        DictDataVO dictDataVO = dictDataConverter.toVO(dictData);
        return Result.success("查询成功", dictDataVO);
    }

    /**
     * 根据字典类型查询字典数据列表
     */
    @GetMapping("/listDataByType/{dictType}")
    @Operation(summary = "根据字典类型查询数据", description = "根据字典类型查询字典数据列表")
    @OperationLog(
        module = LogConstant.OptModule.DICT,
        type = LogConstant.OptType.SELECT,
        description = "根据字典类型查询数据",
        recordParams = false
    )
    public Result<List<DictDataVO>> listByDictType(@Parameter(description = "字典类型", required = true) @PathVariable String dictType) {
        List<SysDictDataEntity> dictDataList = dictDataService.listByDictType(dictType);
        List<DictDataVO> dictDataVOs = dictDataList.stream()
                .map(dictDataConverter::toVO)
                .collect(Collectors.toList());
        
        return Result.success("查询成功", dictDataVOs);
    }

    /**
     * 创建字典数据
     */
    @PostMapping("/add")
    @Operation(summary = "创建字典数据", description = "创建新的字典数据")
    @OperationLog(
        module = LogConstant.OptModule.DICT,
        type = LogConstant.OptType.INSERT,
        description = "创建字典数据"
    )
    public Result<DictDataVO> create(@RequestBody @Validated CreateDictDataDTO createDictDataDTO) {
        // 检查字典类型是否存在
        if (dictTypeService.getByDictType(createDictDataDTO.getDictType()) == null) {
            return Result.error("字典类型不存在");
        }
        
        SysDictDataEntity dictData = dictDataConverter.toEntity(createDictDataDTO);
        boolean success = dictDataService.create(dictData);
        
        if (success) {
            DictDataVO dictDataVO = dictDataConverter.toVO(dictData);
            return Result.success("字典数据创建成功", dictDataVO);
        } else {
            return Result.error("字典数据创建失败，字典键值可能已存在");
        }
    }

    /**
     * 更新字典数据
     */
    @PostMapping("/update")
    @Operation(summary = "更新字典数据", description = "更新指定字典数据的信息")
    @OperationLog(
        module = LogConstant.OptModule.DICT,
        type = LogConstant.OptType.UPDATE,
        description = "更新字典数据"
    )
    public Result<DictDataVO> update(@RequestBody @Validated UpdateDictDataDTO updateDictDataDTO) {
        Long id = updateDictDataDTO.getId();
        SysDictDataEntity existingDictData = dictDataService.getById(id);
        if (existingDictData == null) {
            return Result.error("字典数据不存在");
        }
        
        // 检查字典类型是否存在
        if (dictTypeService.getByDictType(updateDictDataDTO.getDictType()) == null) {
            return Result.error("字典类型不存在");
        }
        
        SysDictDataEntity dictData = dictDataConverter.toEntity(updateDictDataDTO);
        boolean success = dictDataService.update(dictData);
        
        if (success) {
            SysDictDataEntity updatedDictData = dictDataService.getById(id);
            DictDataVO dictDataVO = dictDataConverter.toVO(updatedDictData);
            return Result.success("字典数据更新成功", dictDataVO);
        } else {
            return Result.error("字典数据更新失败，字典键值可能已存在");
        }
    }

    /**
     * 删除字典数据
     */
    @PostMapping("/deleteById/{id}")
    @Operation(summary = "删除字典数据", description = "删除指定字典数据")
    @OperationLog(
        module = LogConstant.OptModule.DICT,
        type = LogConstant.OptType.DELETE,
        description = "删除字典数据"
    )
    public Result<Void> deleteById(@Parameter(description = "字典数据ID", required = true) @PathVariable Long id) {
        SysDictDataEntity existingDictData = dictDataService.getById(id);
        if (existingDictData == null) {
            return Result.error("字典数据不存在");
        }
        
        boolean success = dictDataService.deleteById(id);
        if (success) {
            return Result.success("字典数据删除成功");
        } else {
            return Result.error("字典数据删除失败");
        }
    }

    /**
     * 批量删除字典数据
     */
    @PostMapping("/batchDelete")
    @Operation(summary = "批量删除字典数据", description = "批量删除字典数据")
    @OperationLog(
        module = LogConstant.OptModule.DICT,
        type = LogConstant.OptType.DELETE,
        description = "批量删除字典数据"
    )
    public Result<Void> batchDelete(@RequestBody List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Result.error("请选择要删除的字典数据");
        }
        
        boolean success = dictDataService.batchDelete(ids);
        if (success) {
            return Result.success("批量删除成功");
        } else {
            return Result.error("批量删除失败");
        }
    }

    /**
     * 更新字典数据状态
     */
    @PostMapping("/updateStatus/{id}/{status}")
    @Operation(summary = "更新字典数据状态", description = "启用或禁用指定字典数据")
    @OperationLog(
        module = LogConstant.OptModule.DICT,
        type = LogConstant.OptType.UPDATE,
        description = "更新字典数据状态"
    )
    public Result<Void> updateStatus(
            @Parameter(description = "字典数据ID", required = true) @PathVariable Long id,
            @Parameter(description = "状态：0-禁用，1-启用", required = true) @PathVariable Integer status) {
        
        SysDictDataEntity existingDictData = dictDataService.getById(id);
        if (existingDictData == null) {
            return Result.error("字典数据不存在");
        }
        
        boolean success = dictDataService.updateStatus(id, status);
        if (success) {
            String statusText = status == 1 ? "启用" : "禁用";
            return Result.success("字典数据" + statusText + "成功");
        } else {
            return Result.error("状态更新失败");
        }
    }
}