/**
 * 查询包装器增强包
 * <p>
 * 本包提供了对MyBatis-Plus LambdaQueryWrapper和LambdaUpdateWrapper的增强实现，主要特性包括：
 * 
 * <h3>主要功能</h3>
 * <ul>
 *   <li>条件判断后添加查询条件（ifPresent系列方法）</li>
 *   <li>自定义条件判断（if系列方法）</li>
 *   <li>高级条件判断（使用Predicate）</li>
 *   <li>便捷的工具方法</li>
 * </ul>
 * 
 * <h3>查询包装器使用示例</h3>
 * <pre>{@code
 * // 1. 基本使用 - 使用工具类创建查询包装器
 * EnhancedLambdaQueryWrapper<SysUserEntity> queryWrapper = QueryWrappers.lambdaQuery();
 * 
 * // 2. 条件判断后添加查询条件
 * queryWrapper.eqIfPresent(SysUserEntity::getUsername, username)  // 用户名不为空时添加等于条件
 *             .likeIfPresent(SysUserEntity::getNickname, nickname)  // 昵称不为空时添加模糊查询
 *             .inIfPresent(SysUserEntity::getStatus, statusList)    // 状态列表不为空时添加IN条件
 *             .betweenIfPresent(SysUserEntity::getCreateTime, startTime, endTime); // 时间范围查询
 * 
 * // 3. 自定义条件判断
 * queryWrapper.eqIf(isAdmin, SysUserEntity::getRoleId, adminRoleId)  // 当isAdmin为true时添加角色条件
 *             .likeIf(enableFuzzySearch, SysUserEntity::getEmail, email); // 当启用模糊搜索时添加邮箱模糊查询
 * 
 * // 4. 高级条件判断（使用Predicate）
 * queryWrapper.eqIfMatch(val -> val.toString().length() > 3, SysUserEntity::getUsername, username)
 *             .likeIfMatch(val -> !val.contains("@"), SysUserEntity::getNickname, nickname);
 * 
 * // 5. 便捷方法
 * queryWrapper.enabledStatus(SysUserEntity::getStatus, 1)  // 查询启用状态的用户
 *             .timeRangeIfPresent(SysUserEntity::getCreateTime, startTime, endTime)  // 时间范围查询
 *             .orderByDesc(SysUserEntity::getCreateTime);  // 按创建时间降序
 * 
 * // 6. 链式调用（支持混合使用标准方法和增强方法）
 * queryWrapper.eq(SysUserEntity::getIsDeleted, false)
 *             .eqIfPresent(SysUserEntity::getDeptId, deptId)
 *             .like(SysUserEntity::getUsername, "admin")
 *             .likeIfPresent(SysUserEntity::getPhone, phone);
 *
 * }</pre>
 *
 * <h3>更新包装器使用示例</h3>
 * <pre>{@code
 * // 1. 基本使用 - 使用工具类创建更新包装器
 * EnhancedLambdaUpdateWrapper<SysUserEntity> updateWrapper = QueryWrappers.lambdaUpdate();
 * 
 * // 2. 条件判断后设置字段值
 * updateWrapper.setIfPresent(SysUserEntity::getUsername, newUsername)  // 新用户名不为空时设置
 *              .setIfPresent(SysUserEntity::getEmail, newEmail)        // 新邮箱不为空时设置
 *              .setIfPresent(SysUserEntity::getPhone, newPhone)        // 新手机号不为空时设置
 *              .set(SysUserEntity::getUpdateTime, LocalDateTime.now()); // 总是设置更新时间
 * 
 * // 3. 条件判断后添加WHERE条件
 * updateWrapper.eqIfPresent(SysUserEntity::getId, userId)              // 用户ID不为空时添加条件
 *              .eqIfPresent(SysUserEntity::getStatus, 1)               // 只更新启用状态的用户
 *              .neIfPresent(SysUserEntity::getIsDeleted, 1);           // 排除已删除的用户
 * 
 * // 4. 自定义条件判断
 * updateWrapper.setIf(updatePassword, SysUserEntity::getPassword, newPassword)  // 当需要更新密码时设置
 *              .setIf(updateStatus, SysUserEntity::getStatus, newStatus)        // 当需要更新状态时设置
 *              .eqIf(onlyUpdateActive, SysUserEntity::getStatus, 1);            // 当只更新活跃用户时添加条件
 * 
 * // 5. 批量更新示例
 * updateWrapper.set(SysUserEntity::getStatus, 0)                      // 设置状态为禁用
 *              .set(SysUserEntity::getUpdateTime, LocalDateTime.now())  // 设置更新时间
 *              .inIfPresent(SysUserEntity::getId, userIds)              // 根据用户ID列表批量更新
 *              .enabledStatus(SysUserEntity::getIsDeleted, 0);          // 只更新未删除的用户
 * 
 * // 6. 混合使用标准方法和增强方法
 * updateWrapper.set(SysUserEntity::getLastLoginTime, LocalDateTime.now())
 *              .setIfPresent(SysUserEntity::getLastLoginIp, loginIp)
 *              .eq(SysUserEntity::getId, userId)
 *              .eqIfPresent(SysUserEntity::getStatus, 1);
 * }</pre>
 *
 * <h3>方法分类</h3>
 * <ul>
 *   <li><strong>查询包装器方法</strong>：
 *     <ul>
 *       <li>ifPresent系列：当参数不为null（字符串还要求有内容）时才添加查询条件</li>
 *       <li>if系列：当指定的boolean条件为true时才添加查询条件</li>
 *       <li>ifMatch系列：当参数满足Predicate条件时才添加查询条件</li>
 *     </ul>
 *   </li>
 *   <li><strong>更新包装器方法</strong>：
 *     <ul>
 *       <li>setIfPresent系列：当参数不为null（字符串还要求有内容）时才设置字段值</li>
 *       <li>setIf系列：当指定的boolean条件为true时才设置字段值</li>
 *       <li>setIfMatch系列：当参数满足Predicate条件时才设置字段值</li>
 *       <li>WHERE条件方法：与查询包装器相同的条件判断方法</li>
 *     </ul>
 *   </li>
 *   <li><strong>便捷方法</strong>：针对常见业务场景的快捷方法（查询和更新包装器通用）</li>
 * </ul>
 * 
 * <h3>注意事项</h3>
 * <ul>
 *   <li>字符串类型的参数会使用StringUtils.hasText()进行判断，确保不是空字符串</li>
 *   <li>集合类型的参数会判断是否为null且不为空</li>
 *   <li>所有方法都支持链式调用</li>
 *   <li>可以与标准的LambdaQueryWrapper方法混合使用</li>
 * </ul>
 * 
 * @author charter
 * @create 2025/07/19
 * @see ink.charter.website.common.core.wrapper.EnhancedLambdaQueryWrapper
 * @see ink.charter.website.common.core.wrapper.EnhancedLambdaUpdateWrapper
 * @see ink.charter.website.common.core.wrapper.QueryWrappers
 */
package ink.charter.website.common.core.wrapper;