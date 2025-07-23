package ink.charter.website.common.core.entity.base;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 实体基类
 * 
 * @author charter
 * @create 2025/07/17
 */
@Data
public class BaseEntity implements Serializable {
  
  @Schema(description = "id")
  @TableId(value = "id", type = IdType.ASSIGN_ID)
  private Long id;

  @Schema(description = "创建时间")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @TableField(value = "create_time", fill = FieldFill.INSERT)
  private LocalDateTime createTime;

  @Schema(description = "更新时间")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
  private LocalDateTime updateTime;

  @Schema(description = "逻辑删除")
  @JsonIgnore
  @TableLogic(value = "0", delval = "1")
  private Boolean isDeleted;

  @Schema(description = "其他参数")
  @TableField(exist = false)
  private Map<String,Object> param = new HashMap<>();
}
