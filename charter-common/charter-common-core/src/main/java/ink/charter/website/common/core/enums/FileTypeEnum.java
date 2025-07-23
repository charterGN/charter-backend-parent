package ink.charter.website.common.core.enums;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 文件类型枚举
 * <p>
 * 用于分类和管理不同类型的文件，支持根据文件扩展名自动识别文件类型
 * </p>
 *
 * @author Charter
 * @create 2025/07/19
 */
public enum FileTypeEnum {
    
    /**
     * 图片文件
     */
    IMAGE(1, "图片", "jpg,jpeg,png,gif,bmp,webp,svg,ico"),
    
    /**
     * 文档文件
     */
    DOCUMENT(2, "文档", "pdf,doc,docx,xls,xlsx,ppt,pptx,txt,rtf,odt,ods,odp"),
    
    /**
     * 视频文件
     */
    VIDEO(3, "视频", "mp4,avi,mkv,mov,wmv,flv,webm,m4v,3gp,rmvb"),
    
    /**
     * 音频文件
     */
    AUDIO(4, "音频", "mp3,wav,flac,aac,ogg,wma,m4a,ape"),
    
    /**
     * 压缩文件
     */
    ARCHIVE(5, "压缩包", "zip,rar,7z,tar,gz,bz2,xz,jar,war"),
    
    /**
     * 代码文件
     */
    CODE(6, "代码", "java,js,html,css,php,py,cpp,c,h,xml,json,sql,sh,bat"),
    
    /**
     * 其他文件
     */
    OTHER(99, "其他", "");
    
    /**
     * 文件类型代码
     */
    private final Integer code;
    
    /**
     * 文件类型名称
     */
    private final String name;
    
    /**
     * 支持的文件扩展名（逗号分隔）
     */
    private final String extensions;
    
    /**
     * 支持的文件扩展名集合（缓存）
     */
    private final Set<String> extensionSet;
    
    FileTypeEnum(Integer code, String name, String extensions) {
        this.code = code;
        this.name = name;
        this.extensions = extensions;
        this.extensionSet = extensions.isEmpty() ? 
            Set.of() : 
            Arrays.stream(extensions.split(","))
                  .map(String::trim)
                  .map(String::toLowerCase)
                  .collect(Collectors.toSet());
    }
    
    public Integer getCode() {
        return code;
    }
    
    public String getName() {
        return name;
    }
    
    public String getExtensions() {
        return extensions;
    }
    
    public Set<String> getExtensionSet() {
        return extensionSet;
    }
    
    /**
     * 根据代码获取文件类型枚举
     *
     * @param code 文件类型代码
     * @return 对应的枚举，如果不存在则返回null
     */
    public static FileTypeEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (FileTypeEnum fileType : values()) {
            if (fileType.getCode().equals(code)) {
                return fileType;
            }
        }
        return null;
    }
    
    /**
     * 根据文件扩展名获取文件类型
     *
     * @param extension 文件扩展名（不包含点号）
     * @return 对应的文件类型枚举，如果无法识别则返回OTHER
     */
    public static FileTypeEnum getByExtension(String extension) {
        if (extension == null || extension.trim().isEmpty()) {
            return OTHER;
        }
        
        String normalizedExtension = extension.toLowerCase().trim();
        // 移除可能存在的点号前缀
        if (normalizedExtension.startsWith(".")) {
            normalizedExtension = normalizedExtension.substring(1);
        }
        
        for (FileTypeEnum fileType : values()) {
            if (fileType != OTHER && fileType.getExtensionSet().contains(normalizedExtension)) {
                return fileType;
            }
        }
        
        return OTHER;
    }
    
    /**
     * 判断指定扩展名是否属于当前文件类型
     *
     * @param extension 文件扩展名
     * @return 是否匹配
     */
    public boolean matches(String extension) {
        if (extension == null || this == OTHER) {
            return false;
        }
        
        String normalizedExtension = extension.toLowerCase().trim();
        if (normalizedExtension.startsWith(".")) {
            normalizedExtension = normalizedExtension.substring(1);
        }
        
        return extensionSet.contains(normalizedExtension);
    }
    
    /**
     * 判断是否为图片文件
     *
     * @return 是否为图片
     */
    public boolean isImage() {
        return this == IMAGE;
    }
    
    /**
     * 判断是否为文档文件
     *
     * @return 是否为文档
     */
    public boolean isDocument() {
        return this == DOCUMENT;
    }
    
    /**
     * 判断是否为视频文件
     *
     * @return 是否为视频
     */
    public boolean isVideo() {
        return this == VIDEO;
    }
    
    /**
     * 判断是否为音频文件
     *
     * @return 是否为音频
     */
    public boolean isAudio() {
        return this == AUDIO;
    }
    
    /**
     * 判断是否为压缩文件
     *
     * @return 是否为压缩包
     */
    public boolean isArchive() {
        return this == ARCHIVE;
    }
    
    /**
     * 判断是否为代码文件
     *
     * @return 是否为代码
     */
    public boolean isCode() {
        return this == CODE;
    }
}