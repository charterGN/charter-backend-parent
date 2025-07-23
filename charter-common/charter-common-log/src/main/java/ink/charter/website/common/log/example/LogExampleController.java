package ink.charter.website.common.log.example;

import ink.charter.website.common.log.annotation.OperationLog;
import ink.charter.website.common.log.constant.LogConstant;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
/**
 * 操作日志使用示例
 * 注意：这是一个示例类，实际项目中应该放在具体的业务模块中
 *
 * @author charter
 * @create 2025/07/17
 */
//@RestController
//@RequestMapping("/api-admin/example")
public class LogExampleController {

    /**
     * 用户登录示例
     */
    @PostMapping("/login")
    @OperationLog(
        module = LogConstant.OptModule.USER,
        type = LogConstant.OptType.LOGIN,
        description = "用户登录系统",
        recordParams = true,
        recordResponse = false,
        async = true
    )
    public Map<String, Object> login(@RequestBody Map<String, String> loginRequest) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "登录成功");
        result.put("token", "mock-jwt-token");
        return result;
    }

    /**
     * 用户信息查询示例
     */
    @GetMapping("/user/{id}")
    @OperationLog(
        module = LogConstant.OptModule.USER,
        type = LogConstant.OptType.SELECT,
        description = "查询用户信息",
        recordParams = true,
        recordResponse = true,
        async = false
    )
    public Map<String, Object> getUserInfo(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("username", "testuser");
        result.put("email", "test@example.com");
        return result;
    }

    /**
     * 用户信息更新示例
     */
    @PostMapping("/user/{id}")
    @OperationLog(
        module = LogConstant.OptModule.USER,
        type = LogConstant.OptType.UPDATE,
        description = "更新用户信息",
        recordParams = true,
        recordResponse = true,
        async = true
    )
    public Map<String, Object> updateUser(@PathVariable Long id, @RequestBody Map<String, Object> userInfo) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "用户信息更新成功");
        result.put("id", id);
        return result;
    }

    /**
     * 用户删除示例
     */
    @PostMapping("/user/{id}")
    @OperationLog(
        module = LogConstant.OptModule.USER,
        type = LogConstant.OptType.DELETE,
        description = "删除用户",
        recordParams = true,
        recordResponse = false,
        async = true
    )
    public Map<String, Object> deleteUser(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "用户删除成功");
        return result;
    }

    /**
     * 批量操作示例
     */
    @PostMapping("/users/batch")
    @OperationLog(
        module = LogConstant.OptModule.USER,
        type = LogConstant.OptType.OTHER,
        description = "批量处理用户数据",
        recordParams = true,
        recordResponse = true,
        async = true
    )
    public Map<String, Object> batchOperation(@RequestBody Map<String, Object> batchRequest) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "批量操作完成");
        result.put("processedCount", 10);
        return result;
    }
}