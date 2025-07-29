package ink.charter.website.common.web.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 应用启动完成监听器
 * 在应用启动完成后打印美观的提示信息
 *
 * @author charter
 * @create 2025/07/29
 */
@Slf4j
@Component
public class ApplicationStartupListener implements ApplicationListener<ApplicationReadyEvent> {

    @Value("${server.port:8080}")
    private String port;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @Value("${charter.version:1.0.0}")
    private String version;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Environment env = event.getApplicationContext().getEnvironment();
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("无法获取本机IP地址，使用localhost");
        }
        
        String applicationName = env.getProperty("spring.application.name", "Charter Application");
        String activeProfiles = String.join(", ", env.getActiveProfiles());
        if (activeProfiles.isEmpty()) {
            activeProfiles = "default";
        }
        
        // 构建访问URL
        String localUrl = String.format("%s://localhost:%s%s", protocol, port, contextPath);
        String externalUrl = String.format("%s://%s:%s%s", protocol, hostAddress, port, contextPath);
        
        // 构建Swagger文档URL
        String swaggerUrl = String.format("%s://localhost:%s%s/doc.html", protocol, port, contextPath);
        
        // 打印启动成功信息
        printStartupInfo(applicationName, version, activeProfiles, localUrl, externalUrl, swaggerUrl);
    }
    
    private void printStartupInfo(String appName, String version, String profiles, 
                                 String localUrl, String externalUrl, String swaggerUrl) {
        System.out.println("\u001B[90m" + "=".repeat(60) + "\u001B[0m");
        System.out.println("\u001B[32m🚀 Charter Platform 启动成功！\u001B[0m\n");
        
        // 应用信息
        System.out.println("\u001B[33m📋 应用信息:\u001B[0m");
        System.out.println("   ├─ 应用名称: " + appName);
        System.out.println("   ├─ 版本信息: v" + version);
        System.out.println("   └─ 运行环境: " + profiles);
        System.out.println();
        
        // 访问地址
        System.out.println("\u001B[34m🌐 访问地址:\u001B[0m");
        System.out.println("   ├─ 🏠 本地访问: \u001B[36m" + localUrl + "\u001B[0m");
        System.out.println("   ├─ 🌍 外部访问: \u001B[36m" + externalUrl + "\u001B[0m");
        System.out.println("   └─ 📚 接口文档: \u001B[36m" + swaggerUrl + "\u001B[0m");
        System.out.println();
        
        System.out.println("\u001B[35m✨ 心有星海，自羡鱼游! ✨\u001B[0m");
        System.out.println("\u001B[90m" + "=".repeat(60) + "\u001B[0m");
        System.out.println();
    }
}