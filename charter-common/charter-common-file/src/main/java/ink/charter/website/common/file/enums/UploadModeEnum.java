package ink.charter.website.common.file.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * 上传模式枚举
 *
 * @author charter
 * @create 2025/07/16
 */
@Getter
@AllArgsConstructor
public enum UploadModeEnum {

    /**
     * 阿里云OSS
     */
    OSS("oss", "ossUploadStrategyImpl"),

    /**
     * MinIO
     */
    MINIO("minio", "minioUploadStrategyImpl"),

    /**
     * 华为云OBS
     */
    HUAWEI("huawei", "huaweiUploadStrategyImpl"),

    /**
     * 腾讯云COS
     */
    TENCENT("tencent", "tencentUploadStrategyImpl"),

    /**
     * 七牛云Kodo
     */
    QINIU("qiniu", "qiniuUploadStrategyImpl");

    private final String mode;

    private final String strategy;

    public static String getStrategy(String mode) {
        for (UploadModeEnum value : UploadModeEnum.values()) {
            if (value.getMode().equals(mode)) {
                return value.getStrategy();
            }
        }
        return null;
    }

}
