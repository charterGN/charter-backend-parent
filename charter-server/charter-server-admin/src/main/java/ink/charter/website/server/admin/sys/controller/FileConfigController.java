package ink.charter.website.server.admin.sys.controller;

import ink.charter.website.common.core.common.Result;
import ink.charter.website.common.core.entity.sys.SysFileConfigEntity;
import ink.charter.website.common.core.common.PageResult;
import ink.charter.website.domain.admin.api.dto.fileconfig.CreateFileConfigDTO;
import ink.charter.website.domain.admin.api.dto.fileconfig.PageFileConfigDTO;
import ink.charter.website.domain.admin.api.dto.fileconfig.UpdateFileConfigDTO;
import ink.charter.website.domain.admin.api.vo.fileconfig.FileConfigVO;
import ink.charter.website.server.admin.sys.converter.FileConfigConverter;
import ink.charter.website.server.admin.sys.service.FileConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文件配置Controller
 *
 * @author charter
 * @create 2025/11/14
 */
@Tag(name = "文件配置管理", description = "文件配置管理相关接口")
@RestController
@RequestMapping("/sysFileConfig")
@RequiredArgsConstructor
public class FileConfigController {

    private final FileConfigService fileConfigService;
    private final FileConfigConverter fileConfigConverter;

    @Operation(summary = "分页查询文件配置", description = "分页查询文件配置")
    @PreAuthorize("hasAuthority('sys:fileConfig:page')")
    @PostMapping("/page")
    public Result<PageResult<FileConfigVO>> pageFileConfigs(@RequestBody PageFileConfigDTO pageRequest) {
        PageResult<FileConfigVO> pageResult = fileConfigService.pageFileConfigs(pageRequest);
        return Result.success(pageResult);
    }

    @Operation(summary = "根据ID查询文件配置")
    @GetMapping("/get/{id}")
    public Result<FileConfigVO> getById(
            @Parameter(description = "配置ID", required = true) @PathVariable Long id) {
        FileConfigVO fileConfig = fileConfigService.getById(id);
        return Result.success(fileConfig);
    }

    @Operation(summary = "查询启用的文件配置")
    @GetMapping("/enabled")
    public Result<FileConfigVO> getEnabledConfig() {
        FileConfigVO fileConfig = fileConfigService.getEnabledConfig();
        return Result.success(fileConfig);
    }

    @Operation(summary = "查询所有文件配置列表")
    @GetMapping("/list")
    public Result<List<FileConfigVO>> listAll() {
        List<FileConfigVO> list = fileConfigService.listAll();
        return Result.success(list);
    }

    @Operation(summary = "创建文件配置", description = "创建系统文件配置")
    @PreAuthorize("hasAuthority('sys:fileConfig:create')")
    @PostMapping("/create")
    public Result<FileConfigVO> create(@RequestBody @Validated CreateFileConfigDTO createFileConfigDTO) {
        SysFileConfigEntity entity = fileConfigConverter.toEntity(createFileConfigDTO);
        boolean success = fileConfigService.create(entity);
        if (success) {
            FileConfigVO vo = fileConfigConverter.toVO(entity);
            return Result.success(vo);
        }
        return Result.error("创建文件配置失败");
    }

    @Operation(summary = "更新文件配置", description = "更新系统文件配置")
    @PreAuthorize("hasAuthority('sys:fileConfig:update')")
    @PostMapping("/update")
    public Result<FileConfigVO> update(@RequestBody @Validated UpdateFileConfigDTO updateFileConfigDTO) {
        SysFileConfigEntity entity = fileConfigConverter.toEntity(updateFileConfigDTO);
        boolean success = fileConfigService.update(entity);
        if (success) {
            FileConfigVO vo = fileConfigService.getById(updateFileConfigDTO.getId());
            return Result.success(vo);
        }
        return Result.error("更新文件配置失败");
    }

    @Operation(summary = "删除文件配置", description = "删除系统文件配置")
    @PreAuthorize("hasAuthority('sys:fileConfig:delete')")
    @PostMapping("/delete/{id}")
    public Result<Void> deleteById(
            @Parameter(description = "配置ID", required = true) @PathVariable Long id) {
        boolean success = fileConfigService.deleteById(id);
        return success ? Result.success() : Result.error("删除文件配置失败");
    }

    @Operation(summary = "批量删除文件配置", description = "批量删除系统文件配置")
    @PreAuthorize("hasAuthority('sys:fileConfig:batch-delete')")
    @PostMapping("/delete/batch")
    public Result<Void> batchDelete(@RequestBody List<Long> ids) {
        boolean success = fileConfigService.batchDelete(ids);
        return success ? Result.success() : Result.error("批量删除文件配置失败");
    }

    @Operation(summary = "更新文件配置启用状态", description = "更新系统文件配置启用状态")
    @PreAuthorize("hasAuthority('sys:fileConfig:update-enabled')")
    @PostMapping("/enabled/{id}/{enabled}")
    public Result<Void> updateEnabled(
            @Parameter(description = "配置ID", required = true) @PathVariable Long id,
            @Parameter(description = "启用状态：0-禁用，1-启用", required = true) @PathVariable Integer enabled) {
        boolean success = fileConfigService.updateEnabled(id, enabled);
        return success ? Result.success() : Result.error("更新启用状态失败");
    }

}
