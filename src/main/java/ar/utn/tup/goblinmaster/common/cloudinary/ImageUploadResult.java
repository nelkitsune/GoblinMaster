package ar.utn.tup.goblinmaster.common.cloudinary;

public record ImageUploadResult(
        String secureUrl,
        String publicId,
        Integer width,
        Integer height,
        String format,
        Long bytes
) {}

