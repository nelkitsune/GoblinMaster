package ar.utn.tup.goblinmaster.magic.controller;

import ar.utn.tup.goblinmaster.magic.entity.SpellSchool;
import ar.utn.tup.goblinmaster.magic.repository.SpellSchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/magic/spell-schools")
@RequiredArgsConstructor
public class SpellSchoolController {

    private final SpellSchoolRepository repo;

    @GetMapping
    public ResponseEntity<List<SpellSchool>> getAll() {
        return ResponseEntity.ok(repo.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpellSchool> getById(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}

