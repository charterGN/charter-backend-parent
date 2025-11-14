package ink.charter.website.common.file.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import ink.charter.website.common.core.entity.sys.SysFileConfigEntity;
import org.apache.ibatis.annotations.Mapper;

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

}
