package ink.charter.website.server.admin.sys.controller;

import ink.charter.website.server.admin.sys.service.UserSessionService;
import ink.charter.website.server.admin.sys.converter.UserSessionConverter;
import ink.charter.website.domain.admin.api.dto.session.PageUserSessionDTO;
import ink.charter.website.domain.admin.api.vo.session.SessionCountVO;
import ink.charter.website.domain.admin.api.vo.session.UserSessionVO;
import ink.charter.website.common.log.annotation.OperationLog;
import ink.charter.website.common.log.constant.LogConstant;
import ink.charter.website.common.core.common.PageResult;
import ink.charter.website.common.core.common.Result;
import ink.charter.website.common.core.entity.sys.SysUserSessionEntity;
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
 * 用户会话管理控制器
 *
 * @author charter
 * @create 2025/07/17
 */
@Slf4j
@RestController
@RequestMapping("/sysSession")
@RequiredArgsConstructor
@Validated
@Tag(name = "用户会话管理", description = "用户会话管理相关接口")
public class UserSessionController {

    private final UserSessionService userSessionService;
    private final UserSessionConverter userSessionConverter;

    /**
     * 分页查询用户会话
     */
    @PostMapping("/page")
    @Operation(summary = "分页查询用户会话", description = "分页查询用户会话列表")
    @PreAuthorize("hasAuthority('sys:session:page')")
    public Result<PageResult<UserSessionVO>> pageSessions(@RequestBody PageUserSessionDTO pageRequest) {
        PageResult<SysUserSessionEntity> pageResult = userSessionService.pageSessions(pageRequest);
        List<UserSessionVO> sessionVOs = userSessionConverter.toVOList(pageResult.getRecords());
        PageResult<UserSessionVO> result = PageResult.of(sessionVOs, pageResult.getTotal());
        return Result.success("查询成功", result);
    }

    /**
     * 获取用户的活跃会话列表
     */
    @GetMapping("/user/active")
    @Operation(summary = "获取用户活跃会话", description = "获取指定用户的所有活跃会话")
    public Result<List<UserSessionVO>> getActiveSessionsByUserId(@Parameter(description = "用户ID", required = true) @RequestParam Long userId) {
        List<SysUserSessionEntity> sessions = userSessionService.getActiveSessionsByUserId(userId);
        List<UserSessionVO> sessionVOs = userSessionConverter.toVOList(sessions);
        return Result.success("获取用户活跃会话成功", sessionVOs);
    }

    /**
     * 获取用户的所有会话列表
     */
    @GetMapping("/user/all")
    @Operation(summary = "获取用户所有会话", description = "获取指定用户的所有会话（包括已失效的）")
    public Result<List<UserSessionVO>> getSessionsByUserId(@Parameter(description = "用户ID", required = true) @RequestParam Long userId) {
        List<SysUserSessionEntity> sessions = userSessionService.getSessionsByUserId(userId);
        List<UserSessionVO> sessionVOs = userSessionConverter.toVOList(sessions);
        return Result.success("获取用户会话列表成功", sessionVOs);
    }

    /**
     * 根据会话Token获取会话信息
     */
    @GetMapping("/token")
    @Operation(summary = "获取会话信息", description = "根据会话Token获取会话详细信息")
    public Result<UserSessionVO> getSessionByToken(@Parameter(description = "会话Token", required = true) @RequestParam String sessionToken) {
        SysUserSessionEntity session = userSessionService.getSessionByToken(sessionToken);

        if (session != null) {
            UserSessionVO sessionVO = userSessionConverter.toVO(session);
            return Result.success("获取会话信息成功", sessionVO);
        } else {
            return Result.error("会话不存在");
        }
    }

    /**
     * 使指定会话失效
     */
    @PostMapping("/token/invalidate")
    @Operation(summary = "使会话失效", description = "使指定的会话失效")
    @PreAuthorize("hasAuthority('sys:session:invalidate')")
    @OperationLog(
        module = LogConstant.OptModule.USER,
        type = LogConstant.OptType.DELETE,
        description = "使会话失效"
    )
    public Result<Void> invalidateSession(@Parameter(description = "会话Token", required = true) @RequestParam String sessionToken) {
        SysUserSessionEntity session = userSessionService.getSessionByToken(sessionToken);
        if (session == null) {
            return Result.error("会话不存在");
        }

        userSessionService.invalidateSession(sessionToken);
        return Result.success("会话已失效");
    }

    /**
     * 使用户的所有会话失效
     */
    @PostMapping("/user/invalidateAll")
    @Operation(summary = "使用户所有会话失效", description = "使指定用户的所有会话失效")
    @PreAuthorize("hasAuthority('sys:session:invalidateAll')")
    @OperationLog(
        module = LogConstant.OptModule.USER,
        type = LogConstant.OptType.DELETE,
        description = "使用户所有会话失效"
    )
    public Result<Void> invalidateUserSessions(@Parameter(description = "用户ID", required = true) @RequestParam Long userId) {
        userSessionService.invalidateUserSessions(userId);
        return Result.success("用户所有会话已失效");
    }

    /**
     * 使用户的其他会话失效（除了当前会话）
     */
    @PostMapping("/user/invalidateOthers")
    @Operation(summary = "使用户其他会话失效", description = "使指定用户的其他会话失效（除了当前会话）")
    @PreAuthorize("hasAuthority('sys:session:invalidateOthers')")
    @OperationLog(
        module = LogConstant.OptModule.USER,
        type = LogConstant.OptType.DELETE,
        description = "使用户其他会话失效"
    )
    public Result<Void> invalidateOtherUserSessions(@Parameter(description = "用户ID", required = true) @RequestParam Long userId,
                                                    @Parameter(description = "当前会话Token", required = true) @RequestParam String currentSessionToken) {
        userSessionService.invalidateOtherUserSessions(userId, currentSessionToken);
        return Result.success("用户其他会话已失效");
    }

    /**
     * 统计用户的活跃会话数量
     */
    @GetMapping("/user/count")
    @Operation(summary = "统计用户活跃会话数量", description = "统计指定用户的活跃会话数量")
    public Result<SessionCountVO> countActiveSessionsByUserId(@Parameter(description = "用户ID", required = true) @RequestParam Long userId) {
        int count = userSessionService.countActiveSessionsByUserId(userId);
        SessionCountVO countVO = SessionCountVO.of(userId, count);
        return Result.success("统计用户活跃会话数量成功", countVO);
    }

    /**
     * 清理过期会话
     */
    @PostMapping("/cleanup")
    @Operation(summary = "清理过期会话", description = "清理所有过期的会话")
    @PreAuthorize("hasAuthority('sys:session:cleanup')")
    @OperationLog(
        module = LogConstant.OptModule.SYSTEM,
        type = LogConstant.OptType.DELETE,
        description = "清理过期会话"
    )
    public Result<Void> cleanExpiredSessions() {
        userSessionService.cleanExpiredSessions();
        return Result.success("过期会话清理完成");
    }
}