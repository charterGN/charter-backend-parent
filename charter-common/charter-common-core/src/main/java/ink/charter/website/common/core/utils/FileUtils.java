package ink.charter.website.common.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 文件工具类
 * 提供文件操作相关的实用功能
 *
 * @author charter
 * @create 2025/07/19
 */
@Slf4j
public final class FileUtils {

    // 常用文件扩展名
    public static final Set<String> IMAGE_EXTENSIONS = Set.of(
        "jpg", "jpeg", "png", "gif", "bmp", "webp", "svg", "ico"
    );
    
    public static final Set<String> DOCUMENT_EXTENSIONS = Set.of(
        "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt", "rtf"
    );
    
    public static final Set<String> VIDEO_EXTENSIONS = Set.of(
        "mp4", "avi", "mkv", "mov", "wmv", "flv", "webm", "m4v"
    );
    
    public static final Set<String> AUDIO_EXTENSIONS = Set.of(
        "mp3", "wav", "flac", "aac", "ogg", "wma", "m4a"
    );
    
    public static final Set<String> ARCHIVE_EXTENSIONS = Set.of(
        "zip", "rar", "7z", "tar", "gz", "bz2", "xz"
    );

    // 文件大小单位
    private static final long KB = 1024L;
    private static final long MB = KB * 1024L;
    private static final long GB = MB * 1024L;
    private static final long TB = GB * 1024L;

    private FileUtils() {
        // 工具类，禁止实例化
    }

    // ==================== 文件基础操作 ====================

    /**
     * 检查文件是否存在
     *
     * @param filePath 文件路径
     * @return 是否存在
     */
    public static boolean exists(String filePath) {
        if (!StringUtils.hasText(filePath)) {
            return false;
        }
        return Files.exists(Paths.get(filePath));
    }

    /**
     * 检查是否为文件
     *
     * @param filePath 文件路径
     * @return 是否为文件
     */
    public static boolean isFile(String filePath) {
        if (!StringUtils.hasText(filePath)) {
            return false;
        }
        Path path = Paths.get(filePath);
        return Files.exists(path) && Files.isRegularFile(path);
    }

    /**
     * 检查是否为目录
     *
     * @param dirPath 目录路径
     * @return 是否为目录
     */
    public static boolean isDirectory(String dirPath) {
        if (!StringUtils.hasText(dirPath)) {
            return false;
        }
        Path path = Paths.get(dirPath);
        return Files.exists(path) && Files.isDirectory(path);
    }

    /**
     * 创建目录（包括父目录）
     *
     * @param dirPath 目录路径
     * @return 是否创建成功
     */
    public static boolean createDirectories(String dirPath) {
        if (!StringUtils.hasText(dirPath)) {
            return false;
        }
        
        try {
            Files.createDirectories(Paths.get(dirPath));
            return true;
        } catch (IOException e) {
            log.error("创建目录失败: {}", dirPath, e);
            return false;
        }
    }

    /**
     * 删除文件或目录
     *
     * @param path 文件或目录路径
     * @return 是否删除成功
     */
    public static boolean delete(String path) {
        if (!StringUtils.hasText(path)) {
            return false;
        }
        
        try {
            Path filePath = Paths.get(path);
            if (Files.isDirectory(filePath)) {
                // 递归删除目录
                Files.walkFileTree(filePath, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        Files.delete(file);
                        return FileVisitResult.CONTINUE;
                    }
                    
                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        Files.delete(dir);
                        return FileVisitResult.CONTINUE;
                    }
                });
            } else {
                Files.deleteIfExists(filePath);
            }
            return true;
        } catch (IOException e) {
            log.error("删除文件失败: {}", path, e);
            return false;
        }
    }

    /**
     * 复制文件
     *
     * @param sourcePath 源文件路径
     * @param targetPath 目标文件路径
     * @return 是否复制成功
     */
    public static boolean copy(String sourcePath, String targetPath) {
        if (!StringUtils.hasText(sourcePath) || !StringUtils.hasText(targetPath)) {
            return false;
        }
        
        try {
            Path source = Paths.get(sourcePath);
            Path target = Paths.get(targetPath);
            
            // 确保目标目录存在
            createDirectories(target.getParent().toString());
            
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            log.error("复制文件失败: {} -> {}", sourcePath, targetPath, e);
            return false;
        }
    }

    /**
     * 移动文件
     *
     * @param sourcePath 源文件路径
     * @param targetPath 目标文件路径
     * @return 是否移动成功
     */
    public static boolean move(String sourcePath, String targetPath) {
        if (!StringUtils.hasText(sourcePath) || !StringUtils.hasText(targetPath)) {
            return false;
        }
        
        try {
            Path source = Paths.get(sourcePath);
            Path target = Paths.get(targetPath);
            
            // 确保目标目录存在
            createDirectories(target.getParent().toString());
            
            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            log.error("移动文件失败: {} -> {}", sourcePath, targetPath, e);
            return false;
        }
    }

    // ==================== 文件信息 ====================

    /**
     * 获取文件大小
     *
     * @param filePath 文件路径
     * @return 文件大小（字节），如果文件不存在返回-1
     */
    public static long getFileSize(String filePath) {
        if (!isFile(filePath)) {
            return -1L;
        }
        
        try {
            return Files.size(Paths.get(filePath));
        } catch (IOException e) {
            log.error("获取文件大小失败: {}", filePath, e);
            return -1L;
        }
    }

    /**
     * 获取文件扩展名（不包含点号）
     *
     * @param fileName 文件名
     * @return 文件扩展名（不包含点）
     */
    public static String getFileExtension(String fileName) {
        String extensionWithDot = getFileExtensionWithDot(fileName);
        if (extensionWithDot.isEmpty()) {
            return "";
        }
        return extensionWithDot.substring(1); // 去掉开头的点号
    }

    /**
     * 获取文件扩展名（包含点号）
     *
     * @param fileName 文件名
     * @return 文件扩展名（包含点号，如 ".jpg"）
     */
    public static String getFileExtensionWithDot(String fileName) {
        if (!StringUtils.hasText(fileName)) {
            return "";
        }
        
        int lastDotIndex = fileName.lastIndexOf('.');
        // 检查点号是否在有效位置（不在开头，不在结尾，不是隐藏文件的唯一点号）
        if (lastDotIndex <= 0 || lastDotIndex == fileName.length() - 1) {
            return "";
        }
        
        return fileName.substring(lastDotIndex);
    }

    /**
     * 获取文件名（不包含扩展名）
     * <p>
     * 支持处理各种复杂情况：
     * <ul>
     * <li>普通文件："document.pdf" -> "document"</li>
     * <li>多点文件："archive.tar.gz" -> "archive.tar"</li>
     * <li>隐藏文件：".gitignore" -> ".gitignore"（保持原样）</li>
     * <li>带路径："path/file.txt" -> "file"</li>
     * <li>无扩展名："README" -> "README"</li>
     * <li>仅点号结尾："file." -> "file."</li>
     * <li>路径以分隔符结尾："path/" -> ""</li>
     * </ul>
     * </p>
     *
     * @param fileName 文件名或文件路径
     * @return 文件名（不包含扩展名），如果输入为空则返回空字符串
     */
    public static String getFileNameWithoutExtension(String fileName) {
        if (!StringUtils.hasText(fileName)) {
            return "";
        }
        
        int len = fileName.length();
        
        // 如果路径以分隔符结尾，去掉末尾的分隔符
        if (isFileSeparator(fileName.charAt(len - 1))) {
            len--;
            if (len == 0) {
                return "";
            }
        }
        
        int begin = 0;
        int end = len;
        
        // 从后往前遍历，同时查找扩展名和路径分隔符
        for (int i = len - 1; i >= 0; i--) {
            char c = fileName.charAt(i);
            
            // 找到第一个点号（从后往前），标记为扩展名开始位置
            if (len == end && '.' == c) {
                // 检查点号是否在有效位置（不在开头，不是隐藏文件的唯一点号）
                if (i > 0) {
                    end = i;
                }
            }
            
            // 找到路径分隔符，标记为文件名开始位置
            if (isFileSeparator(c)) {
                begin = i + 1;
                break;
            }
        }
        
        return fileName.substring(begin, end);
    }
    
    /**
     * （私有方法）判断字符是否为文件路径分隔符
     * 
     * @param c 字符
     * @return 是否为路径分隔符
     */
    private static boolean isFileSeparator(char c) {
        return c == '/' || c == '\\';
    }

    /**
     * 格式化文件大小
     *
     * @param size 文件大小（字节）
     * @return 格式化后的文件大小
     */
    public static String formatFileSize(long size) {
        if (size < 0) {
            return "未知";
        }
        
        if (size < KB) {
            return size + " B";
        } else if (size < MB) {
            return String.format("%.2f KB", (double) size / KB);
        } else if (size < GB) {
            return String.format("%.2f MB", (double) size / MB);
        } else if (size < TB) {
            return String.format("%.2f GB", (double) size / GB);
        } else {
            return String.format("%.2f TB", (double) size / TB);
        }
    }

    // ==================== 文件类型判断 ====================

    /**
     * 判断是否为图片文件
     *
     * @param fileName 文件名
     * @return 是否为图片文件
     */
    public static boolean isImageFile(String fileName) {
        String extension = getFileExtension(fileName);
        return IMAGE_EXTENSIONS.contains(extension);
    }

    /**
     * 判断是否为文档文件
     *
     * @param fileName 文件名
     * @return 是否为文档文件
     */
    public static boolean isDocumentFile(String fileName) {
        String extension = getFileExtension(fileName);
        return DOCUMENT_EXTENSIONS.contains(extension);
    }

    /**
     * 判断是否为视频文件
     *
     * @param fileName 文件名
     * @return 是否为视频文件
     */
    public static boolean isVideoFile(String fileName) {
        String extension = getFileExtension(fileName);
        return VIDEO_EXTENSIONS.contains(extension);
    }

    /**
     * 判断是否为音频文件
     *
     * @param fileName 文件名
     * @return 是否为音频文件
     */
    public static boolean isAudioFile(String fileName) {
        String extension = getFileExtension(fileName);
        return AUDIO_EXTENSIONS.contains(extension);
    }

    /**
     * 判断是否为压缩文件
     *
     * @param fileName 文件名
     * @return 是否为压缩文件
     */
    public static boolean isArchiveFile(String fileName) {
        String extension = getFileExtension(fileName);
        return ARCHIVE_EXTENSIONS.contains(extension);
    }

    // ==================== 文件读写 ====================

    /**
     * 读取文件内容为字符串
     *
     * @param filePath 文件路径
     * @return 文件内容
     */
    public static String readFileToString(String filePath) {
        if (!isFile(filePath)) {
            return null;
        }
        
        try {
            return Files.readString(Paths.get(filePath), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("读取文件失败: {}", filePath, e);
            return null;
        }
    }

    /**
     * 读取文件内容为字节数组
     *
     * @param filePath 文件路径
     * @return 文件内容字节数组
     */
    public static byte[] readFileToBytes(String filePath) {
        if (!isFile(filePath)) {
            return null;
        }
        
        try {
            return Files.readAllBytes(Paths.get(filePath));
        } catch (IOException e) {
            log.error("读取文件失败: {}", filePath, e);
            return null;
        }
    }

    /**
     * 读取文件内容为行列表
     *
     * @param filePath 文件路径
     * @return 文件行列表
     */
    public static List<String> readFileToLines(String filePath) {
        if (!isFile(filePath)) {
            return Collections.emptyList();
        }
        
        try {
            return Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("读取文件失败: {}", filePath, e);
            return Collections.emptyList();
        }
    }

    /**
     * 写入字符串到文件
     *
     * @param filePath 文件路径
     * @param content  内容
     * @return 是否写入成功
     */
    public static boolean writeStringToFile(String filePath, String content) {
        if (!StringUtils.hasText(filePath) || content == null) {
            return false;
        }
        
        try {
            Path path = Paths.get(filePath);
            createDirectories(path.getParent().toString());
            Files.writeString(path, content, StandardCharsets.UTF_8);
            return true;
        } catch (IOException e) {
            log.error("写入文件失败: {}", filePath, e);
            return false;
        }
    }

    /**
     * 写入字节数组到文件
     *
     * @param filePath 文件路径
     * @param bytes    字节数组
     * @return 是否写入成功
     */
    public static boolean writeBytesToFile(String filePath, byte[] bytes) {
        if (!StringUtils.hasText(filePath) || bytes == null) {
            return false;
        }
        
        try {
            Path path = Paths.get(filePath);
            createDirectories(path.getParent().toString());
            Files.write(path, bytes);
            return true;
        } catch (IOException e) {
            log.error("写入文件失败: {}", filePath, e);
            return false;
        }
    }

    /**
     * 追加内容到文件
     *
     * @param filePath 文件路径
     * @param content  内容
     * @return 是否追加成功
     */
    public static boolean appendToFile(String filePath, String content) {
        if (!StringUtils.hasText(filePath) || content == null) {
            return false;
        }
        
        try {
            Path path = Paths.get(filePath);
            createDirectories(path.getParent().toString());
            Files.writeString(path, content, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            return true;
        } catch (IOException e) {
            log.error("追加文件失败: {}", filePath, e);
            return false;
        }
    }

    // ==================== MultipartFile 处理 ====================

    /**
     * 保存MultipartFile到指定路径
     *
     * @param file     MultipartFile对象
     * @param filePath 保存路径
     * @return 是否保存成功
     */
    public static boolean saveMultipartFile(MultipartFile file, String filePath) {
        if (file == null || file.isEmpty() || !StringUtils.hasText(filePath)) {
            return false;
        }
        
        try {
            Path path = Paths.get(filePath);
            createDirectories(path.getParent().toString());
            file.transferTo(path.toFile());
            return true;
        } catch (IOException e) {
            log.error("保存文件失败: {}", filePath, e);
            return false;
        }
    }

    /**
     * 生成唯一文件名
     *
     * @param originalFileName 原始文件名
     * @return 唯一文件名
     */
    public static String generateUniqueFileName(String originalFileName) {
        if (!StringUtils.hasText(originalFileName)) {
            return UUID.randomUUID().toString();
        }
        
        String extension = getFileExtension(originalFileName);
        String uniqueName = UUID.randomUUID().toString();
        
        if (StringUtils.hasText(extension)) {
            return uniqueName + "." + extension;
        }
        
        return uniqueName;
    }

    // ==================== 文件安全 ====================

    /**
     * 计算文件MD5值
     *
     * @param filePath 文件路径
     * @return MD5值
     */
    public static String calculateMD5(String filePath) {
        if (!isFile(filePath)) {
            return null;
        }
        
        byte[] bytes = readFileToBytes(filePath);
        if (bytes == null) {
            return null;
        }
        
        return calculateMD5(bytes);
    }


    /**
     * 计算MultipartFile的MD5值
     *
     * @param file MultipartFile对象
     * @return MD5值
     */
    public static String calculateMD5(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            return calculateMD5(file.getBytes());
        } catch (IOException e) {
            log.error("计算MD5失败", e);
            return null;
        }
    }

    /**
     * 计算字节数组的MD5值
     *
     * @param data 字节数组
     * @return MD5值
     */
    public static String calculateMD5(byte[] data) {
        if (data == null) {
            return null;
        }
        
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(data);
            return bytesToHexString(digest);
        } catch (NoSuchAlgorithmException e) {
            log.error("计算MD5失败", e);
            return null;
        }
    }

    /**
     * （私有方法）将字节数组转换为十六进制字符串
     *
     * @param bytes 字节数组
     * @return 十六进制字符串
     */
    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * 验证文件名是否安全（防止路径遍历攻击）
     *
     * @param fileName 文件名
     * @return 是否安全
     */
    public static boolean isSafeFileName(String fileName) {
        if (!StringUtils.hasText(fileName)) {
            return false;
        }
        
        // 检查是否包含路径分隔符
        if (fileName.contains("/") || fileName.contains("\\")) {
            return false;
        }
        
        // 检查是否包含相对路径
        if (fileName.contains("..") || fileName.startsWith(".")) {
            return false;
        }
        
        // 检查是否包含特殊字符
        String[] dangerousChars = {"<", ">", ":", "\"", "|", "?", "*"};
        for (String dangerousChar : dangerousChars) {
            if (fileName.contains(dangerousChar)) {
                return false;
            }
        }
        
        return true;
    }

    // ==================== 目录操作 ====================

    /**
     * 列出目录下的所有文件
     *
     * @param dirPath 目录路径
     * @return 文件列表
     */
    public static List<String> listFiles(String dirPath) {
        if (!isDirectory(dirPath)) {
            return Collections.emptyList();
        }
        
        try (Stream<Path> paths = Files.list(Paths.get(dirPath))) {
            return paths.filter(Files::isRegularFile)
                       .map(Path::toString)
                       .sorted()
                       .toList();
        } catch (IOException e) {
            log.error("列出文件失败: {}", dirPath, e);
            return Collections.emptyList();
        }
    }

    /**
     * 递归列出目录下的所有文件
     *
     * @param dirPath 目录路径
     * @return 文件列表
     */
    public static List<String> listFilesRecursively(String dirPath) {
        if (!isDirectory(dirPath)) {
            return Collections.emptyList();
        }
        
        try (Stream<Path> paths = Files.walk(Paths.get(dirPath))) {
            return paths.filter(Files::isRegularFile)
                       .map(Path::toString)
                       .sorted()
                       .toList();
        } catch (IOException e) {
            log.error("递归列出文件失败: {}", dirPath, e);
            return Collections.emptyList();
        }
    }

    /**
     * 计算目录大小
     *
     * @param dirPath 目录路径
     * @return 目录大小（字节）
     */
    public static long calculateDirectorySize(String dirPath) {
        if (!isDirectory(dirPath)) {
            return 0L;
        }
        
        try (Stream<Path> paths = Files.walk(Paths.get(dirPath))) {
            return paths.filter(Files::isRegularFile)
                       .mapToLong(path -> {
                           try {
                               return Files.size(path);
                           } catch (IOException e) {
                               return 0L;
                           }
                       })
                       .sum();
        } catch (IOException e) {
            log.error("计算目录大小失败: {}", dirPath, e);
            return 0L;
        }
    }

    // ==================== 压缩解压 ====================

    /**
     * 压缩文件或目录为ZIP
     *
     * @param sourcePath 源路径
     * @param zipPath    ZIP文件路径
     * @return 是否压缩成功
     */
    public static boolean zipFile(String sourcePath, String zipPath) {
        if (!StringUtils.hasText(sourcePath) || !StringUtils.hasText(zipPath)) {
            return false;
        }
        
        try (FileOutputStream fos = new FileOutputStream(zipPath);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            
            Path source = Paths.get(sourcePath);
            if (Files.isDirectory(source)) {
                zipDirectory(source, source, zos);
            } else {
                zipSingleFile(source, zos);
            }
            
            return true;
        } catch (IOException e) {
            log.error("压缩文件失败: {} -> {}", sourcePath, zipPath, e);
            return false;
        }
    }

    /**
     * （私有方法）递归压缩目录
     * @param sourceDir 源目录
     * @param baseDir 基础目录
     * @param zos ZIP输出流
     * @throws IOException 压缩失败
     */
    private static void zipDirectory(Path sourceDir, Path baseDir, ZipOutputStream zos) throws IOException {
        try (Stream<Path> paths = Files.walk(sourceDir)) {
            paths.filter(Files::isRegularFile)
                 .forEach(file -> {
                     try {
                         String entryName = baseDir.relativize(file).toString().replace("\\", "/");
                         ZipEntry entry = new ZipEntry(entryName);
                         zos.putNextEntry(entry);
                         Files.copy(file, zos);
                         zos.closeEntry();
                     } catch (IOException e) {
                         log.error("压缩文件失败: {}", file, e);
                     }
                 });
        }
    }

    /**
     * （私有方法）压缩单个文件
     * @param file 文件
     * @param zos ZIP输出流
     * @throws IOException 压缩失败
     */
    private static void zipSingleFile(Path file, ZipOutputStream zos) throws IOException {
        ZipEntry entry = new ZipEntry(file.getFileName().toString());
        zos.putNextEntry(entry);
        Files.copy(file, zos);
        zos.closeEntry();
    }

    /**
     * 解压ZIP文件
     *
     * @param zipPath   ZIP文件路径
     * @param targetDir 目标目录
     * @return 是否解压成功
     */
    public static boolean unzipFile(String zipPath, String targetDir) {
        if (!isFile(zipPath) || !StringUtils.hasText(targetDir)) {
            return false;
        }
        
        try (FileInputStream fis = new FileInputStream(zipPath);
             ZipInputStream zis = new ZipInputStream(fis)) {
            
            createDirectories(targetDir);
            
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    continue;
                }
                
                String entryName = entry.getName();
                if (!isSafeFileName(entryName)) {
                    log.warn("跳过不安全的文件名: {}", entryName);
                    continue;
                }
                
                Path targetFile = Paths.get(targetDir, entryName);
                createDirectories(targetFile.getParent().toString());
                
                try (FileOutputStream fos = new FileOutputStream(targetFile.toFile())) {
                    byte[] buffer = new byte[8192];
                    int length;
                    while ((length = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, length);
                    }
                }
                
                zis.closeEntry();
            }
            
            return true;
        } catch (IOException e) {
            log.error("解压文件失败: {} -> {}", zipPath, targetDir, e);
            return false;
        }
    }
}