package ink.charter.website.common.core.enums;

/**
 * 文件类型枚举
 * <p>
 * 用于分类和管理不同类型的文件，支持根据文件扩展名自动识别文件类型和MIME类型
 * </p>
 *
 * @author Charter
 * @create 2025/07/19
 */
public enum FileTypeEnum {
    
    // 图片文件
    JPG(1, "JPG图片", "jpg", "image/jpeg"),
    JPEG(1, "JPEG图片", "jpeg", "image/jpeg"),
    PNG(1, "PNG图片", "png", "image/png"),
    GIF(1, "GIF图片", "gif", "image/gif"),
    BMP(1, "BMP图片", "bmp", "image/bmp"),
    WEBP(1, "WebP图片", "webp", "image/webp"),
    SVG(1, "SVG图片", "svg", "image/svg+xml"),
    ICO(1, "图标文件", "ico", "image/x-icon"),
    
    // 文档文件
    PDF(2, "PDF文档", "pdf", "application/pdf"),
    DOC(2, "Word文档(doc)", "doc", "application/msword"),
    DOCX(2, "Word文档(docx)", "docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
    XLS(2, "Excel表格(xls)", "xls", "application/vnd.ms-excel"),
    XLSX(2, "Excel表格(xlsx)", "xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
    PPT(2, "PowerPoint演示文稿(ppt)", "ppt", "application/vnd.ms-powerpoint"),
    PPTX(2, "PowerPoint演示文稿(pptx)", "pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"),
    TXT(2, "文本文件", "txt", "text/plain"),
    RTF(2, "富文本文件", "rtf", "application/rtf"),
    
    // 视频文件
    MP4(3, "MP4视频", "mp4", "video/mp4"),
    AVI(3, "AVI视频", "avi", "video/x-msvideo"),
    MKV(3, "MKV视频", "mkv", "video/x-matroska"),
    MOV(3, "QuickTime视频", "mov", "video/quicktime"),
    WMV(3, "WMV视频", "wmv", "video/x-ms-wmv"),
    FLV(3, "Flash视频", "flv", "video/x-flv"),
    WEBM(3, "WebM视频", "webm", "video/webm"),
    
    // 音频文件
    MP3(4, "MP3音频", "mp3", "audio/mpeg"),
    WAV(4, "WAV音频", "wav", "audio/wav"),
    FLAC(4, "FLAC音频", "flac", "audio/flac"),
    AAC(4, "AAC音频", "aac", "audio/aac"),
    OGG(4, "OGG音频", "ogg", "audio/ogg"),
    
    // 压缩文件
    ZIP(5, "ZIP压缩包", "zip", "application/zip"),
    RAR(5, "RAR压缩包", "rar", "application/vnd.rar"),
    SEVEN_Z(5, "7Z压缩包", "7z", "application/x-7z-compressed"),
    TAR(5, "TAR归档", "tar", "application/x-tar"),
    GZ(5, "GZIP压缩", "gz", "application/gzip"),
    
    // 代码文件
    JAVA(6, "Java源码", "java", "text/x-java-source"),
    JS(6, "JavaScript", "js", "application/javascript"),
    TS(6, "TypeScript", "ts", "application/typescript"),
    HTML(6, "HTML文档", "html", "text/html"),
    HTM(6, "HTML文档", "htm", "text/html"),
    CSS(6, "CSS样式表", "css", "text/css"),
    XML(6, "XML文档", "xml", "application/xml"),
    JSON(6, "JSON数据", "json", "application/json"),
    YAML(6, "YAML配置", "yaml", "application/x-yaml"),
    YML(6, "YAML配置", "yml", "application/x-yaml"),
    PY(6, "Python源码", "py", "text/x-python"),
    PHP(6, "PHP源码", "php", "text/x-php"),
    CPP(6, "C++源码", "cpp", "text/x-c++src"),
    C(6, "C源码", "c", "text/x-csrc"),
    H(6, "C头文件", "h", "text/x-chdr"),
    CS(6, "C#源码", "cs", "text/x-csharp"),
    GO(6, "Go源码", "go", "text/x-go"),
    RS(6, "Rust源码", "rs", "text/x-rust"),
    SQL(6, "SQL脚本", "sql", "application/sql"),
    SH(6, "Shell脚本", "sh", "application/x-sh"),
    BAT(6, "批处理文件", "bat", "application/x-msdos-program"),
    PS1(6, "PowerShell脚本", "ps1", "application/x-powershell"),
    
    // 配置和数据文件
    INI(7, "配置文件", "ini", "text/plain"),
    CONF(7, "配置文件", "conf", "text/plain"),
    CFG(7, "配置文件", "cfg", "text/plain"),
    PROPERTIES(7, "属性文件", "properties", "text/plain"),
    LOG(7, "日志文件", "log", "text/plain"),
    CSV(7, "CSV数据", "csv", "text/csv"),
    TSV(7, "TSV数据", "tsv", "text/tab-separated-values"),
    
    // 字体文件
    TTF(8, "TrueType字体", "ttf", "font/ttf"),
    OTF(8, "OpenType字体", "otf", "font/otf"),
    WOFF(8, "Web字体", "woff", "font/woff"),
    WOFF2(8, "Web字体2", "woff2", "font/woff2"),
    EOT(8, "嵌入式字体", "eot", "application/vnd.ms-fontobject"),
    
    // 其他文件
    OTHER(99, "其他文件", "", "application/octet-stream");
    
    /**
     * 文件类型代码
     */
    private final Integer code;
    
    /**
     * 文件类型名称
     */
    private final String name;
    
    /**
     * 文件扩展名
     */
    private final String extension;
    
    /**
     * MIME类型
     */
    private final String mimeType;
    
    FileTypeEnum(Integer code, String name, String extension, String mimeType) {
        this.code = code;
        this.name = name;
        this.extension = extension.toLowerCase();
        this.mimeType = mimeType;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public String getName() {
        return name;
    }
    
    public String getExtension() {
        return extension;
    }
    
    public String getMimeType() {
        return mimeType;
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
            if (fileType != OTHER && fileType.getExtension().equals(normalizedExtension)) {
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
        
        return this.extension.equals(normalizedExtension);
    }
    
    /**
     * 判断是否为图片文件
     *
     * @return 是否为图片
     */
    public boolean isImage() {
        return this.code == 1;
    }
    
    /**
     * 判断是否为文档文件
     *
     * @return 是否为文档
     */
    public boolean isDocument() {
        return this.code == 2;
    }
    
    /**
     * 判断是否为视频文件
     *
     * @return 是否为视频
     */
    public boolean isVideo() {
        return this.code == 3;
    }
    
    /**
     * 判断是否为音频文件
     *
     * @return 是否为音频
     */
    public boolean isAudio() {
        return this.code == 4;
    }
    
    /**
     * 判断是否为压缩文件
     *
     * @return 是否为压缩包
     */
    public boolean isArchive() {
        return this.code == 5;
    }
    
    /**
     * 判断是否为代码文件
     *
     * @return 是否为代码
     */
    public boolean isCode() {
        return this.code == 6;
    }
    
    /**
     * 判断是否为配置文件
     *
     * @return 是否为配置文件
     */
    public boolean isConfig() {
        return this.code == 7;
    }
    
    /**
     * 判断是否为字体文件
     *
     * @return 是否为字体文件
     */
    public boolean isFont() {
        return this.code == 8;
    }
}