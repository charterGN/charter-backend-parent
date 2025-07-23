/**
 * 通用工具类包
 * 
 * <p>本包提供了项目开发中常用的工具类，包括但不限于：</p>
 * 
 * <h3>核心工具类</h3>
 * <ul>
 *   <li>{@link ink.charter.website.common.core.utils.JsonUtils} - JSON序列化和反序列化工具</li>
 *   <li>{@link ink.charter.website.common.core.utils.CryptoUtils} - 加密解密工具</li>
 *   <li>{@link ink.charter.website.common.core.utils.IdGenerator} - ID生成工具</li>
 *   <li>{@link ink.charter.website.common.core.utils.IpUtils} - IP地址处理工具</li>
 *   <li>{@link ink.charter.website.common.core.utils.DateTimeUtils} - 日期时间处理工具</li>
 *   <li>{@link ink.charter.website.common.core.utils.StringUtils} - 字符串处理工具</li>
 *   <li>{@link ink.charter.website.common.core.utils.FileUtils} - 文件操作工具</li>
 *   <li>{@link ink.charter.website.common.core.utils.ValidationUtils} - 数据验证工具</li>
 * </ul>
 * 
 * <h3>设计原则</h3>
 * <ul>
 *   <li><strong>避免重复</strong>：不重复实现已有依赖（如Hutool、Spring等）中的功能</li>
 *   <li><strong>线程安全</strong>：所有工具类都是线程安全的</li>
 *   <li><strong>异常处理</strong>：合理处理异常，避免工具类抛出未处理的异常</li>
 *   <li><strong>性能优化</strong>：注重性能，避免不必要的对象创建</li>
 *   <li><strong>易于使用</strong>：提供简洁明了的API，方便开发者使用</li>
 * </ul>
 * 
 * <h3>使用示例</h3>
 * 
 * <h4>JSON处理</h4>
 * <pre>{@code
 * // 对象转JSON
 * String json = JsonUtils.toJson(user);
 * 
 * // JSON转对象
 * User user = JsonUtils.fromJson(json, User.class);
 * 
 * // JSON转List
 * List<User> users = JsonUtils.fromJsonToList(json, User.class);
 * }</pre>
 * 
 * <h4>加密解密</h4>
 * <pre>{@code
 * // MD5加密
 * String md5 = CryptoUtils.md5("password");
 * 
 * // AES加密
 * String encrypted = CryptoUtils.aesEncrypt("data", "key");
 * String decrypted = CryptoUtils.aesDecrypt(encrypted, "key");
 * 
 * // 生成随机盐
 * String salt = CryptoUtils.generateSalt();
 * }</pre>
 * 
 * <h4>ID生成</h4>
 * <pre>{@code
 * // 生成UUID
 * String uuid = IdGenerator.uuid();
 * 
 * // 生成雪花ID
 * long snowflakeId = IdGenerator.snowflakeId();
 * 
 * // 生成订单号
 * String orderNo = IdGenerator.generateOrderNo();
 * }</pre>
 * 
 * <h4>IP处理</h4>
 * <pre>{@code
 * // 获取客户端IP
 * String clientIp = IpUtils.getClientIp(request);
 * 
 * // 验证IP格式
 * boolean isValid = IpUtils.isValidIPv4("192.168.1.1");
 * 
 * // 判断是否为内网IP
 * boolean isInternal = IpUtils.isInternalIp("192.168.1.1");
 * }</pre>
 * 
 * <h4>日期时间处理</h4>
 * <pre>{@code
 * // 格式化当前时间
 * String now = DateTimeUtils.format(DateTimeUtils.now());
 * 
 * // 解析日期字符串
 * LocalDateTime dateTime = DateTimeUtils.parseDateTime("2025-07-19 10:30:00");
 * 
 * // 计算时间差
 * long days = DateTimeUtils.daysBetween(startDate, endDate);
 * }</pre>
 * 
 * <h4>字符串处理</h4>
 * <pre>{@code
 * // 驼峰命名转换
 * String camelCase = StringUtils.toCamelCase("user_name");
 * 
 * // 验证邮箱格式
 * boolean isEmail = StringUtils.isValidEmail("user@example.com");
 * 
 * // 生成随机字符串
 * String random = StringUtils.randomAlphanumeric(10);
 * }</pre>
 * 
 * <h4>文件操作</h4>
 * <pre>{@code
 * // 读取文件内容
 * String content = FileUtils.readFileToString("/path/to/file.txt");
 * 
 * // 写入文件
 * boolean success = FileUtils.writeStringToFile("/path/to/file.txt", "content");
 * 
 * // 获取文件扩展名
 * String extension = FileUtils.getFileExtension("document.pdf");
 * }</pre>
 * 
 * <h4>数据验证</h4>
 * <pre>{@code
 * // 验证邮箱
 * boolean isValidEmail = ValidationUtils.isValidEmail("user@example.com");
 * 
 * // 验证手机号
 * boolean isValidPhone = ValidationUtils.isValidPhone("13800138000");
 * 
 * // 验证身份证号
 * boolean isValidIdCard = ValidationUtils.isValidIdCard("110101199001011234");
 * }</pre>
 * 
 * <h3>注意事项</h3>
 * <ul>
 *   <li>所有工具类都是final类，不允许继承</li>
 *   <li>所有工具类都有私有构造函数，不允许实例化</li>
 *   <li>所有公共方法都是静态方法</li>
 *   <li>对于可能失败的操作，会返回null或false，而不是抛出异常</li>
 *   <li>对于输入参数，会进行null检查和有效性验证</li>
 * </ul>
 * 
 * @author charter
 * @create 2025/07/19
 * @version 1.0
 */
package ink.charter.website.common.core.utils;