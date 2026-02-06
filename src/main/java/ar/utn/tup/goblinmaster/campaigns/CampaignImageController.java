package ar.utn.tup.goblinmaster.campaigns;

import ar.utn.tup.goblinmaster.common.cloudinary.CloudinaryService;
import ar.utn.tup.goblinmaster.common.cloudinary.ImageUploadResult;
import ar.utn.tup.goblinmaster.users.User;
import ar.utn.tup.goblinmaster.users.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/campaigns")
public class CampaignImageController {
    private final CampaignRepository campaignRepository;
    private final CampaignService campaignService;
    private final UserService userService;
    private final CloudinaryService cloudinaryService;

    public CampaignImageController(CampaignRepository campaignRepository, CampaignService campaignService,
                                   UserService userService, CloudinaryService cloudinaryService) {
        this.campaignRepository = campaignRepository;
        this.campaignService = campaignService;
        this.userService = userService;
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> uploadCampaignImage(@PathVariable Long id, @RequestPart("file") MultipartFile file) {
        try {
            Campaign campaign = campaignRepository.findById(id).orElseThrow();
            User current = userService.getCurrentUser();
            campaignService.assertCanEditImages(current, campaign); // TODO real roles

            String folder = "goblinmaster/campaigns/" + campaign.getId();
            ImageUploadResult res = cloudinaryService.uploadImage(file, folder);
            // subir OK, ahora borrar anterior si había
            if (campaign.getImagePublicId() != null && !campaign.getImagePublicId().isBlank()) {
                cloudinaryService.deleteImage(campaign.getImagePublicId());
            }
            campaign.setImageUrl(res.secureUrl());
            campaign.setImagePublicId(res.publicId());
            campaignRepository.save(campaign);
            return ResponseEntity.ok(Map.of("imageUrl", res.secureUrl()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
}

