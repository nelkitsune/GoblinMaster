// src/main/java/ar/utn/tup/goblinmaster/magic/controller/SpellClassController.java
package ar.utn.tup.goblinmaster.magic.controller;

import ar.utn.tup.goblinmaster.magic.entity.SpellClass;
import ar.utn.tup.goblinmaster.magic.repository.SpellClassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/magic/spell-classes")
@RequiredArgsConstructor
public class SpellClassController {

    private final SpellClassRepository repo;

    @PostMapping
    public ResponseEntity<SpellClass> create(@RequestBody SpellClass req) {
        SpellClass saved = repo.save(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}