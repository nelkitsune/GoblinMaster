package ar.utn.tup.goblinmaster.feats.controller;


import ar.utn.tup.goblinmaster.feats.dto.FeatsRequest;
import ar.utn.tup.goblinmaster.feats.dto.FeatsResponse;
import ar.utn.tup.goblinmaster.feats.sevice.FeatsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feats")
public class FeatsController {
    private final FeatsService service;

    public FeatsController(FeatsService service) {
        this.service = service;
    }

    @PostMapping
    public void createFeat(@RequestBody FeatsRequest request) {
        service.createFeat(request);
    }

    @GetMapping
    public List<FeatsResponse> getAllFeats() {
        return service.getAllFeats();
    }
}
