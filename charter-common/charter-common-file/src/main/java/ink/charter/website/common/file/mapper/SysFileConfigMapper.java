package ink.charter.website.common.file.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ink.charter.website.common.core.entity.sys.SysFileConfigEntity;
import ink.charter.website.common.mybatis.wrapper.QueryWrappers;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 文件配置Mapper
 *
 * @author charter
 * @create 2025/11/14
 */
@Mapper
public interface SysFileConfigMapper extends BaseMapper<SysFileConfigEntity> {

    /**
     * 查询启用的文件配置
     *
     * @return 启用的文件配置
     */
    default SysFileConfigEntity selectEnabledConfig() {
        return selectOne(Wrappers.<SysFileConfigEntity>lambdaQuery()
                .eq(SysFileConfigEntity::getEnabled, 1)
                .last("LIMIT 1"));
    }

    /**
     * 分页查询文件配置
     *
     * @param page 分页参数
     * @param configName 配置名称
     * @param storageType 存储类型
     * @param enabled 启用状态
     * @return 分页结果
     */
    default Page<SysFileConfigEntity> pageFileConfigs(Page<SysFileConfigEntity> page, String configName, String storageType, Integer enabled) {
        return selectPage(page, QueryWrappers.<SysFileConfigEntity>lambdaQuery()
            .likeIfPresent(SysFileConfigEntity::getConfigName, configName)
            .eqIfPresent(SysFileConfigEntity::getStorageType, storageType)
            .eqIfPresent(SysFileConfigEntity::getEnabled, enabled)
            .orderByDesc(SysFileConfigEntity::getCreateTime));
    }

    /**
     * 查询所有文件配置列表
     *
     * @return 文件配置列表
     */
    default List<SysFileConfigEntity> listAll() {
        return selectList(QueryWrappers.<SysFileConfigEntity>lambdaQuery()
            .orderByDesc(SysFileConfigEntity::getCreateTime));
    }

    /**
     * 更新文件配置启用状态
     *
     * @param id 配置ID
     * @param enabled 启用状态
     * @return 更新行数
     */
    default int updateEnabled(Long id, Integer enabled) {
        if (id == null || enabled == null) {
            return 0;
        }
        return update(null, QueryWrappers.<SysFileConfigEntity>lambdaUpdate()
            .set(SysFileConfigEntity::getEnabled, enabled)
            .eq(SysFileConfigEntity::getId, id));
    }

    /**
     * 禁用所有文件配置
     *
     * @return 更新行数
     */
    default int disableAll() {
        return update(null, QueryWrappers.<SysFileConfigEntity>lambdaUpdate()
            .set(SysFileConfigEntity::getEnabled, 0));
    }

    /**
     * 批量删除文件配置
     *
     * @param ids 配置ID列表
     * @return 删除行数
     */
    default int deleteByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return 0;
        }
        return delete(QueryWrappers.<SysFileConfigEntity>lambdaQuery()
            .in(SysFileConfigEntity::getId, ids));
    }

}
