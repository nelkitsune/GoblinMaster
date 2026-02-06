package ar.utn.tup.goblinmaster.users;

import ar.utn.tup.goblinmaster.common.cloudinary.CloudinaryService;
import ar.utn.tup.goblinmaster.common.cloudinary.ImageUploadResult;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserAvatarController {
    private final UserService userService;
    private final CloudinaryService cloudinaryService;
    private final UserRepository userRepository;

    public UserAvatarController(UserService userService, CloudinaryService cloudinaryService, UserRepository userRepository) {
        this.userService = userService;
        this.cloudinaryService = cloudinaryService;
        this.userRepository = userRepository;
    }

    @PostMapping(value = "/me/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> uploadAvatar(@RequestPart("file") MultipartFile file) {
        try {
            User current = userService.getCurrentUser();
            String folder = "goblinmaster/avatars/users/" + current.getId();
            ImageUploadResult res = cloudinaryService.uploadImage(file, folder);
            // subir OK, ahora borrar anterior si había
            if (current.getAvatarPublicId() != null && !current.getAvatarPublicId().isBlank()) {
                cloudinaryService.deleteImage(current.getAvatarPublicId());
            }
            current.setAvatarUrl(res.secureUrl());
            current.setAvatarPublicId(res.publicId());
            userRepository.save(current);
            return ResponseEntity.ok(Map.of("avatarUrl", res.secureUrl()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
}

