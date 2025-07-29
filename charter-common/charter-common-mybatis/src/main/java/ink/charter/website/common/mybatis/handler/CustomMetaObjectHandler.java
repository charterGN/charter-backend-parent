package ink.charter.website.common.mybatis.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MyBatis-Plus 自动填充处理器
 * 用于自动填充创建时间、更新时间、逻辑删除等字段
 *
 * @author charter
 * @create 2025/07/21
 */
@Slf4j
@Component
public class CustomMetaObjectHandler implements MetaObjectHandler {

    /**
     * 插入时自动填充
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.debug("开始插入填充...");
        
        // 只在字段为null时自动填充创建时间
        this.fillStrategy(metaObject, "createTime", LocalDateTime.now());
        
        // 只在字段为null时自动填充更新时间
        this.fillStrategy(metaObject, "updateTime", LocalDateTime.now());
        
        // 只在字段为null时自动填充逻辑删除字段（默认为false，表示未删除）
        this.fillStrategy(metaObject, "isDeleted", false);
    }

    /**
     * 更新时自动填充
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.debug("开始更新填充...");
        
        // 只在字段为null时自动填充更新时间
        this.fillStrategy(metaObject, "updateTime", LocalDateTime.now());
    }
}