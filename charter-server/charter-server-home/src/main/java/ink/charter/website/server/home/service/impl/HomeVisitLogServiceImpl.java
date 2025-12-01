package ink.charter.website.server.home.service.impl;

import ink.charter.website.common.core.utils.IpUtils;
import ink.charter.website.domain.home.api.dto.CreateVisitLogDTO;
import ink.charter.website.domain.home.api.entity.HomeVisitLogEntity;
import ink.charter.website.domain.home.api.repository.HomeVisitLogRepository;
import ink.charter.website.server.home.service.HomeVisitLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.github.jarod.qqwry.IPZone;

import java.time.LocalDateTime;

/**
 * 访问日志服务实现类
 *
 * @author charter
 * @create 2025/11/24
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HomeVisitLogServiceImpl implements HomeVisitLogService {

    private final HomeVisitLogRepository visitLogRepository;

    @Override
    public Long create(CreateVisitLogDTO dto) {
        HomeVisitLogEntity entity = new HomeVisitLogEntity();
        
        // 基本信息
        entity.setVisitIp(dto.getVisitIp());
        entity.setUserAgent(dto.getUserAgent());
        entity.setReferer(dto.getReferer());
        entity.setVisitPage(dto.getVisitPage());
        entity.setSessionId(dto.getSessionId());
        entity.setVisitTime(LocalDateTime.now());
        
        // 解析IP地址信息
        if (StringUtils.hasText(dto.getVisitIp())) {
            parseIpInfo(entity, dto.getVisitIp());
        }
        
        // 解析User-Agent信息
        if (StringUtils.hasText(dto.getUserAgent())) {
            parseUserAgent(entity, dto.getUserAgent());
        }
        
        // 判断是否为新访客（根据IP判断）
        entity.setIsNewVisitor(isNewVisitor(dto.getVisitIp()));
        
        visitLogRepository.create(entity);
        return entity.getId();
    }

    @Override
    public boolean updateStayDuration(Long id, Integer stayDuration) {
        return visitLogRepository.updateStayDuration(id, stayDuration);
    }

    /**
     * 判断是否为新访客
     * 根据IP地址判断，如果该IP之前没有访问记录则为新访客
     *
     * @param visitIp 访问IP
     * @return 是否为新访客（1是 0否）
     */
    private Integer isNewVisitor(String visitIp) {
        // IP为空，默认为新访客
        if (!StringUtils.hasText(visitIp)) {
            return 1;
        }
        
        // 检查该IP是否存在访问记录
        boolean exists = visitLogRepository.existsByIp(visitIp);
        return exists ? 0 : 1;
    }

    /**
     * 解析IP地址信息
     */
    private void parseIpInfo(HomeVisitLogEntity entity, String ip) {
        try {
            // 判断是否为内网IP
            if (IpUtils.isInternalIp(ip)) {
                entity.setVisitAddress("内网");
                return;
            }
            
            // 使用纯真IP数据库解析地址
            IPZone ipZone = IpUtils.getIpLocationDetail(ip);
            if (ipZone != null) {
                String mainInfo = ipZone.getMainInfo();
                String subInfo = ipZone.getSubInfo();
                
                // 设置完整地址
                entity.setVisitAddress(IpUtils.getIpLocation(ip));
                
                // 简单解析省市信息（可以根据实际情况优化）
                if (StringUtils.hasText(mainInfo)) {
                    // 尝试提取省份和城市
                    parseLocationInfo(entity, mainInfo, subInfo);
                }
            }
        } catch (Exception e) {
            log.warn("解析IP地址失败: {}", ip, e);
        }
    }

    /**
     * 解析地理位置信息
     */
    private void parseLocationInfo(HomeVisitLogEntity entity, String mainInfo, String subInfo) {
        // 简单的省市解析逻辑
        if (mainInfo.contains("省")) {
            int index = mainInfo.indexOf("省");
            entity.setVisitProvince(mainInfo.substring(0, index + 1));
            
            if (mainInfo.length() > index + 1) {
                String cityPart = mainInfo.substring(index + 1);
                if (cityPart.contains("市")) {
                    entity.setVisitCity(cityPart.substring(0, cityPart.indexOf("市") + 1));
                }
            }
        } else if (mainInfo.contains("市")) {
            // 直辖市
            int index = mainInfo.indexOf("市");
            String city = mainInfo.substring(0, index + 1);
            entity.setVisitProvince(city);
            entity.setVisitCity(city);
        }
        
        // 设置国家（默认中国，可以根据IP段判断）
        entity.setVisitCountry("中国");
    }

    /**
     * 解析User-Agent信息
     */
    private void parseUserAgent(HomeVisitLogEntity entity, String userAgent) {
        try {
            String lowerUA = userAgent.toLowerCase();
            
            // 解析浏览器
            if (lowerUA.contains("edg")) {
                entity.setBrowser("Edge");
            } else if (lowerUA.contains("chrome")) {
                entity.setBrowser("Chrome");
            } else if (lowerUA.contains("firefox")) {
                entity.setBrowser("Firefox");
            } else if (lowerUA.contains("safari") && !lowerUA.contains("chrome")) {
                entity.setBrowser("Safari");
            } else if (lowerUA.contains("opera") || lowerUA.contains("opr")) {
                entity.setBrowser("Opera");
            } else {
                entity.setBrowser("Other");
            }
            
            // 解析操作系统
            if (lowerUA.contains("windows")) {
                entity.setOs("Windows");
            } else if (lowerUA.contains("mac")) {
                entity.setOs("MacOS");
            } else if (lowerUA.contains("linux")) {
                entity.setOs("Linux");
            } else if (lowerUA.contains("android")) {
                entity.setOs("Android");
            } else if (lowerUA.contains("iphone") || lowerUA.contains("ipad")) {
                entity.setOs("iOS");
            } else {
                entity.setOs("Other");
            }
            
            // 解析设备类型
            if (lowerUA.contains("mobile") || lowerUA.contains("android") || lowerUA.contains("iphone")) {
                entity.setDeviceType("Mobile");
            } else if (lowerUA.contains("tablet") || lowerUA.contains("ipad")) {
                entity.setDeviceType("Tablet");
            } else {
                entity.setDeviceType("PC");
            }
            
        } catch (Exception e) {
            log.warn("解析User-Agent失败: {}", userAgent, e);
        }
    }
}
