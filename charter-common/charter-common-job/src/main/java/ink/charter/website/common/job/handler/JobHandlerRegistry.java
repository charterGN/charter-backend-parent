package ink.charter.website.common.job.handler;

import ink.charter.website.common.job.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务处理器注册中心
 * 负责管理所有的任务处理器
 *
 * @author charter
 * @create 2025/07/30
 */
@Slf4j
public class JobHandlerRegistry implements ApplicationContextAware, InitializingBean {
    
    private ApplicationContext applicationContext;
    
    /**
     * 任务处理器缓存
     * key: 处理器名称, value: 处理器实例
     */
    private final Map<String, AbstractJobHandler> handlerMap = new ConcurrentHashMap<>();
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        // 扫描并注册所有的任务处理器
        scanAndRegisterHandlers();
    }
    
    /**
     * 扫描并注册任务处理器
     */
    private void scanAndRegisterHandlers() {
        Map<String, Object> handlerBeans = applicationContext.getBeansWithAnnotation(JobHandler.class);
        
        for (Map.Entry<String, Object> entry : handlerBeans.entrySet()) {
            Object bean = entry.getValue();
            if (bean instanceof AbstractJobHandler) {
                AbstractJobHandler handler = (AbstractJobHandler) bean;
                JobHandler annotation = bean.getClass().getAnnotation(JobHandler.class);
                
                String handlerName = annotation.value();
                if (handlerName == null || handlerName.trim().isEmpty()) {
                    log.warn("任务处理器[{}]的名称为空，跳过注册", bean.getClass().getName());
                    continue;
                }
                
                // 检查是否重复注册
                if (handlerMap.containsKey(handlerName)) {
                    log.error("任务处理器名称[{}]重复，已存在处理器[{}]，跳过注册[{}]", 
                            handlerName, 
                            handlerMap.get(handlerName).getClass().getName(),
                            bean.getClass().getName());
                    continue;
                }
                
                // 注册处理器
                handlerMap.put(handlerName, handler);
                log.info("注册任务处理器: {} -> {}", handlerName, bean.getClass().getName());
            } else {
                log.warn("标注@JobHandler的类[{}]不是AbstractJobHandler的子类，跳过注册", 
                        bean.getClass().getName());
            }
        }
        
        log.info("任务处理器注册完成，共注册{}个处理器", handlerMap.size());
    }
    
    /**
     * 获取任务处理器
     *
     * @param handlerName 处理器名称
     * @return 处理器实例
     */
    public AbstractJobHandler getHandler(String handlerName) {
        return handlerMap.get(handlerName);
    }
    
    /**
     * 检查处理器是否存在
     *
     * @param handlerName 处理器名称
     * @return true-存在，false-不存在
     */
    public boolean hasHandler(String handlerName) {
        return handlerMap.containsKey(handlerName);
    }
    
    /**
     * 获取所有注册的处理器
     *
     * @return 处理器映射
     */
    public Map<String, AbstractJobHandler> getAllHandlers() {
        return Map.copyOf(handlerMap);
    }
    
    /**
     * 获取处理器数量
     *
     * @return 处理器数量
     */
    public int getHandlerCount() {
        return handlerMap.size();
    }
}