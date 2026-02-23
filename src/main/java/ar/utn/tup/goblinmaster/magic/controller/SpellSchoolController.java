package ar.utn.tup.goblinmaster.magic.controller;

import ar.utn.tup.goblinmaster.magic.dto.SpellSchoolDTO;
import ar.utn.tup.goblinmaster.magic.dto.SpellSubschoolDTO;
import ar.utn.tup.goblinmaster.magic.entity.SpellSchool;
import ar.utn.tup.goblinmaster.magic.entity.SpellSubschool;
import ar.utn.tup.goblinmaster.magic.repository.SpellSchoolRepository;
import ar.utn.tup.goblinmaster.magic.repository.SpellSubschoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/magic/spell-schools")
@RequiredArgsConstructor
public class SpellSchoolController {

    private final SpellSchoolRepository repo;
    private final SpellSubschoolRepository subsRepo;

    @GetMapping
    public ResponseEntity<List<SpellSchoolDTO>> getAll() {
        List<SpellSchool> schools = repo.findAll();
        List<SpellSchoolDTO> dto = schools.stream().map(s -> {
            List<SpellSubschoolDTO> subs = subsRepo.findBySchoolId(s.getId()).stream()
                    .map(ss -> new SpellSubschoolDTO(ss.getId(), ss.getName()))
                    .collect(Collectors.toList());
            return new SpellSchoolDTO(s.getId(), s.getCode(), s.getName(), subs);
        }).collect(Collectors.toList());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpellSchoolDTO> getById(@PathVariable Long id) {
        Optional<SpellSchool> sOpt = repo.findById(id);
        if (sOpt.isEmpty()) return ResponseEntity.notFound().build();
        SpellSchool s = sOpt.get();
        List<SpellSubschoolDTO> subs = subsRepo.findBySchoolId(s.getId()).stream()
                .map(ss -> new SpellSubschoolDTO(ss.getId(), ss.getName()))
                .collect(Collectors.toList());
        SpellSchoolDTO dto = new SpellSchoolDTO(s.getId(), s.getCode(), s.getName(), subs);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}/subschools")
    public ResponseEntity<List<SpellSubschoolDTO>> getSubschools(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        List<SpellSubschool> subs = subsRepo.findBySchoolId(id);
        List<SpellSubschoolDTO> dto = subs.stream()
                .map(ss -> new SpellSubschoolDTO(ss.getId(), ss.getName()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dto);
    }
}
