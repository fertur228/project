package org.example.nodes.service;

import org.example.nodes.storage.StorageProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class StorageService {

    private final Path uploadRoot;   // абсолютный Path к каталогу uploads/posts

    public StorageService(StorageProperties properties) {
        this.uploadRoot = Paths.get(properties.getUploadDir()).toAbsolutePath().normalize();
        try { Files.createDirectories(uploadRoot); } catch (IOException e) {
            throw new RuntimeException("Не могу создать папку для загрузок", e);
        }
    }

    /**
     * Сохранить файл и вернуть относительный путь вида /uploads/posts/uuid.ext
     */
    public String store(MultipartFile file) {
        if (file == null || file.isEmpty()) return null;

        // проверка размера (20 МБ настроено в properties)
        if (file.getSize() > 20 * 1024 * 1024)
            throw new RuntimeException("Файл слишком большой (макс 20 МБ)");

        // контент‑тайпы картинок/видео
        String contentType = file.getContentType();
        if (contentType == null || !(contentType.startsWith("image/") || contentType.startsWith("video/")))
            throw new RuntimeException("Разрешены только изображения и видео");

        // безопасное имя
        String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String filename = UUID.randomUUID() + (ext != null ? "." + ext : "");
        Path target = uploadRoot.resolve(filename).normalize();

        try { file.transferTo(target); }
        catch (IOException e) { throw new RuntimeException("Не удалось сохранить файл", e); }

        return "/uploads/" + filename;  // относительный URL
    }

    /** Удалить ранее сохранённый файл (если он есть) */
    public void delete(String relativePath) {
        if (relativePath == null) return;
        try {
            Path file = uploadRoot.resolve(Paths.get(relativePath).getFileName().toString());
            Files.deleteIfExists(file);
        } catch (IOException ignored) { }
    }
}
