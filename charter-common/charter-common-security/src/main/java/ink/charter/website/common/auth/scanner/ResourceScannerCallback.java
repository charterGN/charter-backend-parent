package ink.charter.website.common.auth.scanner;

import ink.charter.website.common.core.entity.sys.SysResourceEntity;

import java.util.List;

/**
 * 资源扫描器回调接口
 * 由具体的业务模块实现，用于将扫描到的资源同步到数据库
 *
 * @author charter
 * @create 2025/11/12
 */
public interface ResourceScannerCallback {

    /**
     * 同步资源到数据库
     *
     * @param scannedResources 扫描到的资源列表
     */
    void syncResources(List<SysResourceEntity> scannedResources);
}
