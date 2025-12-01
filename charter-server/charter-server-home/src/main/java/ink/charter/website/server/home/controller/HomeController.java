package ink.charter.website.server.home.controller;

import ink.charter.website.common.core.common.Result;
import ink.charter.website.common.core.utils.IpUtils;
import ink.charter.website.domain.home.api.converter.*;
import ink.charter.website.domain.home.api.dto.CreateVisitLogDTO;
import ink.charter.website.domain.home.api.entity.*;
import ink.charter.website.domain.home.api.vo.*;
import ink.charter.website.server.home.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 门户相关控制器，提供门户页面所需的相关接口
 * TODO 待后续看看是否需优化，比如表结构、接口等地方
 *
 * @author charter
 * @create 2025/11/24
 */
@Slf4j
@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
@Validated
@Tag(name = "门户管理", description = "门户页面相关接口")
public class HomeController {

    private final HomeSiteConfigService siteConfigService;
    private final HomeWallpaperService wallpaperService;
    private final HomeSiteLinkService siteLinkService;
    private final HomeSocialLinkService socialLinkService;
    private final HomeHitokotoService hitokotoService;
    private final HomeVisitLogService visitLogService;
    
    private final HomeSiteConfigConverter siteConfigConverter;
    private final HomeWallpaperConverter wallpaperConverter;
    private final HomeSiteLinkConverter siteLinkConverter;
    private final HomeSocialLinkConverter socialLinkConverter;
    private final HomeHitokotoConverter hitokotoConverter;

    // ========================================
    // 一、站点配置相关接口
    // ========================================

    /**
     * 获取站点配置
     */
    @GetMapping("/site-config")
    @Operation(summary = "获取站点配置", description = "获取所有启用的站点配置")
    public Result<List<HomeSiteConfigVO>> getSiteConfig() {
        List<HomeSiteConfigEntity> list = siteConfigService.getAllEnabled();
        List<HomeSiteConfigVO> voList = siteConfigConverter.toVOList(list);
        return Result.success("查询成功", voList);
    }

    /**
     * 获取指定类型配置
     */
    @GetMapping("/site-config/type/{configType}")
    @Operation(summary = "获取指定类型配置", description = "按配置类型获取配置项")
    public Result<List<HomeSiteConfigVO>> getSiteConfigByType(
            @Parameter(description = "配置类型（site/wallpaper/weather/hitokoto/other）", required = true)
            @PathVariable String configType) {
        List<HomeSiteConfigEntity> list = siteConfigService.getByConfigType(configType);
        List<HomeSiteConfigVO> voList = siteConfigConverter.toVOList(list);
        return Result.success("查询成功", voList);
    }

    // ========================================
    // 二、壁纸相关接口
    // ========================================

    /**
     * 随机获取壁纸
     */
    @GetMapping("/wallpaper/random")
    @Operation(summary = "随机获取壁纸", description = "随机返回一张指定类型的壁纸")
    public Result<HomeWallpaperVO> getRandomWallpaper(
            @Parameter(description = "壁纸类型（0默认/1每日一图/2随机风景/3随机动漫）") @RequestParam(required = false) Integer wallpaperType) {
        HomeWallpaperEntity wallpaper = wallpaperService.getRandom(wallpaperType);
        if (wallpaper == null) {
            return Result.error("暂无壁纸数据");
        }
        HomeWallpaperVO vo = wallpaperConverter.toVO(wallpaper);
        return Result.success("查询成功", vo);
    }

    /**
     * 获取默认壁纸
     */
    @GetMapping("/wallpaper/default")
    @Operation(summary = "获取默认壁纸", description = "获取设置为默认的壁纸")
    public Result<HomeWallpaperVO> getDefaultWallpaper() {
        HomeWallpaperEntity wallpaper = wallpaperService.getDefault();
        if (wallpaper == null) {
            return Result.error("暂无默认壁纸");
        }
        HomeWallpaperVO vo = wallpaperConverter.toVO(wallpaper);
        return Result.success("查询成功", vo);
    }

    // ========================================
    // 三、网站链接相关接口
    // ========================================

    /**
     * 获取网站链接列表
     */
    @GetMapping("/site-link/list")
    @Operation(summary = "获取网站链接列表", description = "获取所有启用的网站链接")
    public Result<List<HomeSiteLinkVO>> getSiteLinkList() {
        List<HomeSiteLinkEntity> list = siteLinkService.getAllEnabled();
        List<HomeSiteLinkVO> voList = siteLinkConverter.toVOList(list);
        return Result.success("查询成功", voList);
    }

    /**
     * 网站链接点击计数
     */
    @PostMapping("/site-link/{id}/click")
    @Operation(summary = "网站链接点击计数", description = "增加链接的点击次数")
    public Result<Void> incrementSiteLinkClick(
            @Parameter(description = "链接ID", required = true) @PathVariable Long id) {
        boolean success = siteLinkService.incrementClickCount(id);
        if (success) {
            return Result.success("操作成功");
        }
        return Result.error("操作失败");
    }

    // ========================================
    // 四、社交链接相关接口
    // ========================================

    /**
     * 获取社交链接列表
     */
    @GetMapping("/social-link/list")
    @Operation(summary = "获取社交链接列表", description = "获取所有启用的社交链接")
    public Result<List<HomeSocialLinkVO>> getSocialLinkList() {
        List<HomeSocialLinkEntity> list = socialLinkService.getAllEnabled();
        List<HomeSocialLinkVO> voList = socialLinkConverter.toVOList(list);
        return Result.success("查询成功", voList);
    }

    // ========================================
    // 五、一言相关接口
    // ========================================

    /**
     * 随机获取一言
     */
    @GetMapping("/hitokoto/random")
    @Operation(summary = "随机获取一言", description = "根据条件随机返回一条一言")
    public Result<HomeHitokotoVO> getRandomHitokoto(
            @Parameter(description = "一言类型（多个用逗号分隔，如：a,d,i）") @RequestParam(required = false) String type,
            @Parameter(description = "最小长度") @RequestParam(required = false) Integer minLength,
            @Parameter(description = "最大长度") @RequestParam(required = false) Integer maxLength) {
        
        // 解析类型参数
        List<String> types = null;
        if (StringUtils.hasText(type)) {
            types = Arrays.stream(type.split(","))
                    .map(String::trim)
                    .filter(t -> !t.isEmpty())
                    .collect(Collectors.toList());
        }
        
        HomeHitokotoEntity hitokoto = hitokotoService.getRandom(types, minLength, maxLength);
        if (hitokoto == null) {
            return Result.error("暂无一言数据");
        }
        HomeHitokotoVO vo = hitokotoConverter.toVO(hitokoto);
        return Result.success("查询成功", vo);
    }

    /**
     * 一言展示计数
     */
    @PostMapping("/hitokoto/{id}/view")
    @Operation(summary = "一言展示计数", description = "增加一言的展示次数")
    public Result<Void> incrementHitokotoView(
            @Parameter(description = "一言ID", required = true) @PathVariable Long id) {
        boolean success = hitokotoService.incrementViewCount(id);
        if (success) {
            return Result.success("操作成功");
        }
        return Result.error("操作失败");
    }

    /**
     * 一言点赞
     */
    @PostMapping("/hitokoto/{id}/like")
    @Operation(summary = "一言点赞", description = "增加一言的点赞次数")
    public Result<Void> incrementHitokotoLike(
            @Parameter(description = "一言ID", required = true) @PathVariable Long id) {
        boolean success = hitokotoService.incrementLikeCount(id);
        if (success) {
            return Result.success("操作成功");
        }
        return Result.error("操作失败");
    }

    // ========================================
    // 六、访问统计相关接口
    // ========================================

    /**
     * 记录访问日志
     */
    @PostMapping("/visit-log")
    @Operation(summary = "记录访问日志", description = "记录用户访问信息")
    public Result<String> createVisitLog(@RequestBody @Validated CreateVisitLogDTO dto, HttpServletRequest request) {
        // 从请求中获取真实IP地址
        String clientIp = IpUtils.getClientIp(request);
        dto.setVisitIp(clientIp);
        
        Long id = visitLogService.create(dto);
        // 将Long类型ID转换为String返回，避免前端精度丢失
        return Result.success("记录成功", String.valueOf(id));
    }

    /**
     * 更新访问停留时长
     */
    @PutMapping("/visit-log/{id}/duration")
    @Operation(summary = "更新访问停留时长", description = "更新访问日志的停留时长")
    public Result<Void> updateVisitDuration(
            @Parameter(description = "日志ID", required = true) @PathVariable String id,
            @Parameter(description = "停留时长（秒）", required = true) @RequestParam Integer stayDuration) {
        // 将String类型ID转换为Long
        Long logId = Long.valueOf(id);
        boolean success = visitLogService.updateStayDuration(logId, stayDuration);
        if (success) {
            return Result.success("更新成功");
        }
        return Result.error("更新失败");
    }
}
