package ink.charter.website.server.admin.sys.controller;

import ink.charter.website.common.core.common.PageResult;
import ink.charter.website.common.core.common.Result;
import ink.charter.website.common.core.entity.sys.SysOptLogEntity;
import ink.charter.website.common.log.annotation.OperationLog;
import ink.charter.website.common.log.constant.LogConstant;
import ink.charter.website.domain.admin.api.dto.optlog.PageOptLogDTO;
import ink.charter.website.domain.admin.api.vo.optlog.OptLogVO;
import ink.charter.website.server.admin.sys.converter.OptLogConverter;
import ink.charter.website.server.admin.sys.service.OptLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 操作日志管理控制器
 *
 * @author charter
 * @create 2025/11/12
 */
@Slf4j
@RestController
@RequestMapping("/sysOptLog")
@RequiredArgsConstructor
@Validated
@Tag(name = "操作日志管理", description = "操作日志管理相关接口")
public class OptLogController {

    private final OptLogService optLogService;
    private final OptLogConverter optLogConverter;

    /**
     * 分页查询操作日志
     */
    @PostMapping("/page")
    @Operation(summary = "分页查询操作日志", description = "分页查询操作日志列表")
    @PreAuthorize("hasAuthority('sys:optLog:page')")
    public Result<PageResult<OptLogVO>> pageOptLogs(@RequestBody PageOptLogDTO pageRequest) {
        PageResult<SysOptLogEntity> pageResult = optLogService.pageOptLogs(pageRequest);
        List<OptLogVO> optLogVOs = optLogConverter.toVOList(pageResult.getRecords());
        PageResult<OptLogVO> result = PageResult.of(optLogVOs, pageResult.getTotal());
        return Result.success("查询成功", result);
    }

    /**
     * 根据ID查询操作日志
     */
    @GetMapping("get/{id}")
    @Operation(summary = "查询操作日志详情", description = "根据ID查询操作日志详细信息")
    public Result<OptLogVO> getById(@Parameter(description = "日志ID", required = true) @PathVariable Long id) {
        SysOptLogEntity optLog = optLogService.getById(id);
        if (optLog != null) {
            OptLogVO optLogVO = optLogConverter.toVO(optLog);
            return Result.success("查询成功", optLogVO);
        } else {
            return Result.error("操作日志不存在");
        }
    }

    /**
     * 批量删除操作日志
     */
    @PostMapping("/batch/deleted")
    @Operation(summary = "批量删除操作日志", description = "根据ID列表批量删除操作日志")
    @PreAuthorize("hasAuthority('sys:optLog:batch-delete')")
    @OperationLog(
        module = LogConstant.OptModule.SYSTEM,
        type = LogConstant.OptType.DELETE,
        description = "批量删除操作日志"
    )
    public Result<Void> batchDelete(@RequestBody List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Result.error("请选择要删除的日志");
        }
        boolean success = optLogService.batchDelete(ids);
        return success ? Result.success("删除成功") : Result.error("删除失败");
    }

    /**
     * 根据ID删除操作日志
     */
    @PostMapping("/deleted/{id}")
    @Operation(summary = "删除操作日志", description = "根据ID删除操作日志")
    @PreAuthorize("hasAuthority('sys:optLog:delete')")
    @OperationLog(
        module = LogConstant.OptModule.SYSTEM,
        type = LogConstant.OptType.DELETE,
        description = "删除操作日志"
    )
    public Result<Void> deleteById(@Parameter(description = "日志ID", required = true) @PathVariable Long id) {
        boolean success = optLogService.deleteById(id);
        return success ? Result.success("删除成功") : Result.error("删除失败");
    }

    /**
     * 清空操作日志
     */
    @PostMapping("/truncate")
    @Operation(summary = "清空操作日志", description = "清空所有操作日志")
    @PreAuthorize("hasAuthority('sys:optLog:truncate')")
    @OperationLog(
        module = LogConstant.OptModule.SYSTEM,
        type = LogConstant.OptType.DELETE,
        description = "清空操作日志"
    )
    public Result<Void> truncate() {
        boolean success = optLogService.truncate();
        return success ? Result.success("清空成功") : Result.error("清空失败");
    }
}
