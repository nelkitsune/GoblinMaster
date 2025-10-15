package ar.utn.tup.goblinmaster.magic.service;

import ar.utn.tup.goblinmaster.magic.dto.SpellListItem;
import ar.utn.tup.goblinmaster.magic.dto.SpellRequest;
import ar.utn.tup.goblinmaster.magic.dto.SpellResponse;
import ar.utn.tup.goblinmaster.magic.entity.*;
import ar.utn.tup.goblinmaster.magic.mapper.SpellMapper;
import ar.utn.tup.goblinmaster.magic.repository.SpellClassLevelRepository;
import ar.utn.tup.goblinmaster.magic.repository.SpellClassRepository;
import ar.utn.tup.goblinmaster.magic.repository.SpellRepository;
import ar.utn.tup.goblinmaster.magic.repository.SpellSchoolRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class SpellServiceImpl implements SpellService {

    private final SpellRepository spellRepo;
    private final SpellSchoolRepository schoolRepo;
    private final SpellClassRepository classRepo;
    private final SpellClassLevelRepository sclRepo;
    private final SpellMapper mapper;

    @Override
    public SpellResponse create(SpellRequest req) {
        SpellSchool school = schoolRepo.findByCode(req.getSchoolCode())
                .orElseThrow(() -> new IllegalArgumentException("schoolCode inválido"));

        Spell s = new Spell();
        s.setName(req.getName());
        s.setOriginalName(req.getOriginalName());
        s.setSchool(school);
        s.setCastingTime(req.getCastingTime());
        s.setRangeText(req.getRangeText());
        s.setAreaText(req.getAreaText());
        s.setDurationText(req.getDurationText());
        s.setSavingThrow(req.getSavingThrow());
        s.setSpellResistance(Boolean.TRUE.equals(req.getSpellResistance()));
        s.setComponentsV(Boolean.TRUE.equals(req.getComponentsV()));
        s.setComponentsS(Boolean.TRUE.equals(req.getComponentsS()));
        s.setComponentsM(Boolean.TRUE.equals(req.getComponentsM()));
        s.setMaterialDesc(req.getMaterialDesc());
        s.setSource(req.getSource());
        s.setDescription(req.getDescription());

        s = spellRepo.save(s);

        // niveles por clase
        for (Map.Entry<String, Integer> e : req.getClassLevels().entrySet()) {
            SpellClass sc = classRepo.findByCode(e.getKey())
                    .orElseThrow(() -> new IllegalArgumentException("classCode inválido: " + e.getKey()));
            SpellClassLevel rel = new SpellClassLevel();
            rel.setId(new SpellClassLevelId(s.getId(), sc.getId()));
            rel.setSpell(s);
            rel.setSpellClass(sc);
            rel.setLevel(e.getValue());
            sclRepo.save(rel);
        }

        return mapper.toResponse(s, sclRepo.findBySpellId(s.getId()));
    }

    @Override
    @Transactional(readOnly = true)
    public SpellResponse get(Long id) {
        Spell s = spellRepo.findById(id).orElseThrow();
        return mapper.toResponse(s, sclRepo.findBySpellId(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SpellListItem> search(String q, Pageable pageable) {
        Page<Spell> page = spellRepo.search(q == null ? "" : q, pageable);
        return page.map(mapper::toListItem);
    }

    @Override
    public SpellResponse update(Long id, SpellRequest req) {
        Spell s = spellRepo.findById(id).orElseThrow();

        SpellSchool school = schoolRepo.findByCode(req.getSchoolCode())
                .orElseThrow(() -> new IllegalArgumentException("schoolCode inválido"));

        s.setName(req.getName());
        s.setOriginalName(req.getOriginalName());
        s.setSchool(school);
        s.setCastingTime(req.getCastingTime());
        s.setRangeText(req.getRangeText());
        s.setAreaText(req.getAreaText());
        s.setDurationText(req.getDurationText());
        s.setSavingThrow(req.getSavingThrow());
        s.setSpellResistance(Boolean.TRUE.equals(req.getSpellResistance()));
        s.setComponentsV(Boolean.TRUE.equals(req.getComponentsV()));
        s.setComponentsS(Boolean.TRUE.equals(req.getComponentsS()));
        s.setComponentsM(Boolean.TRUE.equals(req.getComponentsM()));
        s.setMaterialDesc(req.getMaterialDesc());
        s.setSource(req.getSource());
        s.setDescription(req.getDescription());

        // reemplazar niveles
        sclRepo.deleteAll(sclRepo.findBySpellId(id));
        for (Map.Entry<String, Integer> e : req.getClassLevels().entrySet()) {
            SpellClass sc = classRepo.findByCode(e.getKey())
                    .orElseThrow(() -> new IllegalArgumentException("classCode inválido: " + e.getKey()));
            SpellClassLevel rel = new SpellClassLevel();
            rel.setId(new SpellClassLevelId(s.getId(), sc.getId()));
            rel.setSpell(s);
            rel.setSpellClass(sc);
            rel.setLevel(e.getValue());
            sclRepo.save(rel);
        }

        return mapper.toResponse(s, sclRepo.findBySpellId(id));
    }


    public void delete(Long id) {
        spellRepo.deleteById(id);
    }
}
