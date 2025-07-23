package ink.charter.website.common.file.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * 上传模式枚举，后续拓展根据这个策略写即可
 *
 * @author charter
 * @create 2025/07/16
 */
@Getter
@AllArgsConstructor
public enum UploadModeEnum {

    OSS("oss", "ossUploadStrategyImpl");

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
