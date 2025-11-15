package ink.charter.website.server.admin.sys.controller;

import ink.charter.website.common.core.common.PageResult;
import ink.charter.website.common.core.common.Result;
import ink.charter.website.common.core.entity.sys.SysFilesEntity;
import ink.charter.website.common.log.annotation.OperationLog;
import ink.charter.website.common.log.constant.LogConstant;
import ink.charter.website.domain.admin.api.dto.files.CreateFilesDTO;
import ink.charter.website.domain.admin.api.dto.files.PageFilesDTO;
import ink.charter.website.domain.admin.api.dto.files.UpdateFilesDTO;
import ink.charter.website.domain.admin.api.vo.files.FilesVO;
import ink.charter.website.server.admin.sys.converter.FilesConverter;
import ink.charter.website.server.admin.sys.service.FilesService;
import ink.charter.website.common.file.service.CharterFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 文件管理控制器
 *
 * @author charter
 * @create 2025/11/15
 */
@Slf4j
@RestController
@RequestMapping("/sysFiles")
@RequiredArgsConstructor
@Validated
@Tag(name = "文件管理", description = "文件管理相关接口")
public class FilesController {

    private final FilesService filesService;
    private final FilesConverter filesConverter;
    private final CharterFileService charterFileService;

    /**
     * 上传文件
     */
    @PostMapping("/upload")
    @Operation(summary = "上传文件", description = "上传文件到charter_website路径")
    @OperationLog(
        module = LogConstant.OptModule.FILE,
        type = LogConstant.OptType.INSERT,
        description = "上传文件"
    )
    public Result<FilesVO> uploadFile(
            @Parameter(description = "文件", required = true)
            @RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return Result.error("文件不能为空");
        }

        try {
            // 使用 CharterFileService 上传文件到 charter_website 路径
            SysFilesEntity fileEntity = charterFileService.uploadFile(file, "charter_website");
            
            if (fileEntity == null) {
                return Result.error("文件上传失败");
            }

            // 转换为 VO 返回
            FilesVO filesVO = filesConverter.toVO(fileEntity);
            return Result.success("文件上传成功", filesVO);
        } catch (Exception e) {
            log.error("文件上传失败", e);
            return Result.error("文件上传失败：" + e.getMessage());
        }
    }

    /**
     * 分页查询文件
     */
    @PostMapping("/page")
    @Operation(summary = "分页查询文件", description = "分页查询文件列表")
    @PreAuthorize("hasAuthority('sys:files:page')")
    @OperationLog(
        module = LogConstant.OptModule.FILE,
        type = LogConstant.OptType.SELECT,
        description = "分页查询文件"
    )
    public Result<PageResult<FilesVO>> pageFiles(@RequestBody PageFilesDTO pageRequest) {
        PageResult<FilesVO> pageResult = filesService.pageFiles(pageRequest);
        return Result.success(pageResult);
    }

    /**
     * 根据ID查询文件
     */
    @GetMapping("/get/{id}")
    @Operation(summary = "根据ID查询文件", description = "根据ID查询文件详情")
    @OperationLog(
        module = LogConstant.OptModule.FILE,
        type = LogConstant.OptType.SELECT,
        description = "查询文件详情"
    )
    public Result<FilesVO> getById(
            @Parameter(description = "文件ID", required = true)
            @PathVariable Long id) {
        FilesVO filesVO = filesService.getById(id);
        if (filesVO == null) {
            return Result.error("文件不存在");
        }
        return Result.success(filesVO);
    }

    /**
     * 根据MD5查询文件
     */
    @GetMapping("/md5/{fileMd5}")
    @Operation(summary = "根据MD5查询文件", description = "根据MD5值查询文件")
    @OperationLog(
        module = LogConstant.OptModule.FILE,
        type = LogConstant.OptType.SELECT,
        description = "根据MD5查询文件"
    )
    public Result<FilesVO> getByMd5(
            @Parameter(description = "文件MD5值", required = true)
            @PathVariable String fileMd5) {
        FilesVO filesVO = filesService.getByMd5(fileMd5);
        return Result.success(filesVO);
    }

    /**
     * 根据用户ID查询文件列表
     */
    @GetMapping("/user/{uploadUserId}")
    @Operation(summary = "根据用户ID查询文件", description = "根据用户ID查询文件列表")
    @OperationLog(
        module = LogConstant.OptModule.FILE,
        type = LogConstant.OptType.SELECT,
        description = "查询用户文件列表"
    )
    public Result<List<FilesVO>> listByUserId(
            @Parameter(description = "上传用户ID", required = true)
            @PathVariable Long uploadUserId) {
        List<FilesVO> list = filesService.listByUserId(uploadUserId);
        return Result.success(list);
    }

    /**
     * 根据文件类型查询文件列表
     */
    @GetMapping("/type/{fileType}")
    @Operation(summary = "根据文件类型查询文件", description = "根据文件类型查询文件列表")
    @OperationLog(
        module = LogConstant.OptModule.FILE,
        type = LogConstant.OptType.SELECT,
        description = "查询指定类型文件列表"
    )
    public Result<List<FilesVO>> listByFileType(
            @Parameter(description = "文件类型", required = true)
            @PathVariable String fileType) {
        List<FilesVO> list = filesService.listByFileType(fileType);
        return Result.success(list);
    }

    /**
     * 创建文件
     */
    @PostMapping("/create")
    @Operation(summary = "创建文件", description = "创建文件记录")
    @OperationLog(
        module = LogConstant.OptModule.FILE,
        type = LogConstant.OptType.INSERT,
        description = "创建文件"
    )
    public Result<FilesVO> create(@RequestBody @Validated CreateFilesDTO createFilesDTO) {
        // 检查MD5是否已存在
        FilesVO existingFile = filesService.getByMd5(createFilesDTO.getFileMd5());
        if (existingFile != null) {
            return Result.error("文件已存在（MD5重复）");
        }

        SysFilesEntity entity = filesConverter.toEntity(createFilesDTO);
        boolean success = filesService.create(entity);
        if (!success) {
            return Result.error("创建文件失败");
        }

        FilesVO filesVO = filesService.getById(entity.getId());
        return Result.success("创建文件成功", filesVO);
    }

    /**
     * 更新文件
     */
    @PostMapping("/update")
    @Operation(summary = "更新文件", description = "更新文件信息")
    @PreAuthorize("hasAuthority('sys:files:update')")
    @OperationLog(
        module = LogConstant.OptModule.FILE,
        type = LogConstant.OptType.UPDATE,
        description = "更新文件"
    )
    public Result<FilesVO> update(@RequestBody @Validated UpdateFilesDTO updateFilesDTO) {
        FilesVO existingFile = filesService.getById(updateFilesDTO.getId());
        if (existingFile == null) {
            return Result.error("文件不存在");
        }

        SysFilesEntity entity = filesConverter.toEntity(updateFilesDTO);
        boolean success = filesService.update(entity);
        if (!success) {
            return Result.error("更新文件失败");
        }

        FilesVO filesVO = filesService.getById(updateFilesDTO.getId());
        return Result.success("更新文件成功", filesVO);
    }

    /**
     * 删除文件
     */
    @PostMapping("/delete/{id}")
    @Operation(summary = "删除文件", description = "根据ID删除文件")
    @PreAuthorize("hasAuthority('sys:files:delete')")
    @OperationLog(
        module = LogConstant.OptModule.FILE,
        type = LogConstant.OptType.DELETE,
        description = "删除文件"
    )
    public Result<Void> deleteById(
            @Parameter(description = "文件ID", required = true)
            @PathVariable Long id) {
        FilesVO existingFile = filesService.getById(id);
        if (existingFile == null) {
            return Result.error("文件不存在");
        }

        boolean success = filesService.deleteById(id);
        if (!success) {
            return Result.error("删除文件失败");
        }

        return Result.success("删除文件成功");
    }

    /**
     * 批量删除文件
     */
    @PostMapping("/delete/batch")
    @Operation(summary = "批量删除文件", description = "批量删除文件")
    @PreAuthorize("hasAuthority('sys:files:batch-delete')")
    @OperationLog(
        module = LogConstant.OptModule.FILE,
        type = LogConstant.OptType.DELETE,
        description = "批量删除文件"
    )
    public Result<Void> batchDelete(@RequestBody List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Result.error("文件ID列表不能为空");
        }

        boolean success = filesService.batchDelete(ids);
        if (!success) {
            return Result.error("批量删除文件失败");
        }

        return Result.success("批量删除文件成功");
    }

    /**
     * 更新文件状态
     */
    @PostMapping("/status/{id}/{status}")
    @Operation(summary = "更新文件状态", description = "更新文件状态")
    @PreAuthorize("hasAuthority('sys:files:update-status')")
    @OperationLog(
        module = LogConstant.OptModule.FILE,
        type = LogConstant.OptType.UPDATE,
        description = "更新文件状态"
    )
    public Result<Void> updateStatus(
            @Parameter(description = "文件ID", required = true) @PathVariable Long id,
            @Parameter(description = "状态：0-不可用，1-可用", required = true) @PathVariable Integer status) {
        FilesVO existingFile = filesService.getById(id);
        if (existingFile == null) {
            return Result.error("文件不存在");
        }

        if (status != 0 && status != 1) {
            return Result.error("状态值无效");
        }

        boolean success = filesService.updateStatus(id, status);
        if (!success) {
            return Result.error("更新文件状态失败");
        }

        return Result.success("更新文件状态成功");
    }

    /**
     * 更新文件最后访问时间
     */
    @PostMapping("/access/{id}")
    @Operation(summary = "更新文件访问时间", description = "更新文件最后访问时间")
    @OperationLog(
        module = LogConstant.OptModule.FILE,
        type = LogConstant.OptType.UPDATE,
        description = "更新文件访问时间"
    )
    public Result<Void> updateLastAccessTime(
            @Parameter(description = "文件ID", required = true)
            @PathVariable Long id) {
        FilesVO existingFile = filesService.getById(id);
        if (existingFile == null) {
            return Result.error("文件不存在");
        }

        boolean success = filesService.updateLastAccessTime(id, LocalDateTime.now());
        if (!success) {
            return Result.error("更新文件访问时间失败");
        }

        return Result.success("更新文件访问时间成功");
    }

}
