package ar.utn.tup.goblinmaster.common.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class CloudinaryService {
    private static final long MAX_BYTES = 5L * 1024 * 1024; // 5MB
    private static final List<String> ALLOWED = List.of(
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_PNG_VALUE,
            "image/webp"
    );

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public ImageUploadResult uploadImage(MultipartFile file, String folder) {
        validate(file);
        try {
            Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", folder,
                    "overwrite", true
            ));
            String secureUrl = (String) result.get("secure_url");
            String publicId = (String) result.get("public_id");
            Integer width = (Integer) result.get("width");
            Integer height = (Integer) result.get("height");
            String format = (String) result.get("format");
            Long bytes = result.get("bytes") instanceof Integer i ? i.longValue() : (Long) result.get("bytes");
            return new ImageUploadResult(secureUrl, publicId, width, height, format, bytes);
        } catch (IOException e) {
            throw new RuntimeException("Error subiendo imagen a Cloudinary", e);
        }
    }

    public void deleteImage(String publicId) {
        if (publicId == null || publicId.isBlank()) return;
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException("Error borrando imagen previa en Cloudinary", e);
        }
    }

    private void validate(MultipartFile file) {
        if (file == null || file.isEmpty())
            throw new IllegalArgumentException("Archivo vacío");
        if (file.getSize() > MAX_BYTES)
            throw new IllegalArgumentException("Máximo 5MB");
        String ct = file.getContentType();
        if (ct == null || !ALLOWED.contains(ct))
            throw new IllegalArgumentException("Tipo no permitido. Solo JPEG, PNG, WEBP");
    }
}

