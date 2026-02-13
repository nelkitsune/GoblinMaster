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
import ar.utn.tup.goblinmaster.magic.repository.CampaignSpellRepository;
import ar.utn.tup.goblinmaster.campaigns.CampaignRepository;
import ar.utn.tup.goblinmaster.campaigns.CampaignMemberRepository;
import ar.utn.tup.goblinmaster.campaigns.CampaignMember.CampaignRole;
import ar.utn.tup.goblinmaster.users.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SpellServiceImpl implements SpellService {

    private final SpellRepository spellRepo;
    private final SpellSchoolRepository schoolRepo;
    private final SpellClassRepository classRepo;
    private final SpellClassLevelRepository sclRepo;
    private final SpellMapper mapper;
    private final CampaignSpellRepository campaignSpellRepo;
    private final CampaignRepository campaignRepo;
    private final CampaignMemberRepository campaignMemberRepo;
    private final UserRepository userRepository;

    @Override
    public SpellResponse create(SpellRequest req, Authentication auth) {
        if (auth == null || auth.getName() == null) throw new SecurityException("No autenticado");
        var owner = userRepository.findByEmail(auth.getName()).orElseThrow(() -> new SecurityException("Usuario no encontrado"));
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
        s.setOwner(owner);

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
    public List<SpellListItem> search(String q) {
        List<Spell> spells = spellRepo.search(q == null ? "" : q);
        return spells.stream()
                .map(spell -> mapper.toListItem(spell, sclRepo.findBySpellId(spell.getId())))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SpellListItem> getBySpellClass(Long spellClassId) {
        List<Spell> spells = spellRepo.findBySpellClassId(spellClassId);
        return spells.stream()
                .map(spell -> mapper.toListItem(spell, sclRepo.findBySpellId(spell.getId())))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SpellListItem> getBySpellClassAndLevel(Long spellClassId, Integer level) {
        List<Spell> spells = spellRepo.findBySpellClassIdAndLevel(spellClassId, level);
        return spells.stream()
                .map(spell -> mapper.toListItem(spell, sclRepo.findBySpellId(spell.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public SpellResponse update(Long id, SpellRequest req, Authentication auth) {
        if (auth == null || auth.getName() == null) throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN, "No autenticado");
        // 404 si no existe
        Spell s = spellRepo.findById(id).orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Spell no encontrado"));
        // obtener usuario autenticado para chequear rol
        var user = userRepository.findByEmail(auth.getName()).orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN, "Usuario no encontrado"));
        // permisos:
        if (s.getOwner() != null) {
            // homebrew: solo dueño
            if (!auth.getName().equals(s.getOwner().getEmail())) {
                throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN, "No sos el dueño del homebrew");
            }
        } else {
            // no homebrew: solo ADMIN
            if (user.getRole() != ar.utn.tup.goblinmaster.users.User.Role.ADMIN) {
                throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN, "Solo admin puede modificar spells oficiales");
            }
        }
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

    @Override
    public void delete(Long id, Authentication auth) {
        if (auth == null || auth.getName() == null) throw new SecurityException("No autenticado");
        Spell s = spellRepo.findById(id).orElseThrow();
        if (s.getOwner() == null || !auth.getName().equals(s.getOwner().getEmail())) {
            throw new SecurityException("No sos el dueño del homebrew");
        }
        spellRepo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SpellListItem> mine(Authentication auth) {
        var email = auth.getName();
        // owner es User; filtramos por owner email usando repo método auxiliar
        List<Spell> spells = spellRepo.findByOwnerEmail(email);
        return spells.stream()
                .map(spell -> mapper.toListItem(spell, sclRepo.findBySpellId(spell.getId())))
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public void enableInCampaign(Long spellId, Long campaignId, Authentication auth) {
        // validar campaña existe
        var campaign = campaignRepo.findById(campaignId).orElseThrow();
        // validar permisos OWNER
        boolean isOwner = campaignMemberRepo.existsByCampaignIdAndUserEmailAndRole(campaignId, auth.getName(), CampaignRole.OWNER);
        if (!isOwner) throw new SecurityException("No autorizado para modificar la campaña");
        // validar spell existe y es homebrew del usuario
        Spell s = spellRepo.findById(spellId).orElseThrow();
        if (s.getOwner() == null) throw new IllegalArgumentException("Solo se puede habilitar homebrew");
        if (!s.getOwner().getEmail().equals(auth.getName())) throw new SecurityException("Homebrew ajeno");
        // idempotente
        if (!campaignSpellRepo.existsByCampaignIdAndSpellId(campaignId, spellId)) {
            ar.utn.tup.goblinmaster.magic.entity.CampaignSpell cs = ar.utn.tup.goblinmaster.magic.entity.CampaignSpell.builder()
                    .campaign(campaign)
                    .spell(s)
                    .build();
            campaignSpellRepo.save(cs);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<SpellListItem> listCampaignHomebrew(Long campaignId, Authentication auth) {
        // validar miembro
        boolean isMember = campaignMemberRepo.findByCampaignIdAndUserEmail(campaignId, auth.getName()).isPresent();
        if (!isMember) throw new SecurityException("No perteneces a la campaña");
        return campaignSpellRepo.findAllByCampaignId(campaignId).stream()
                .map(cs -> mapper.toListItem(cs.getSpell(), sclRepo.findBySpellId(cs.getSpell().getId())))
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public void disableInCampaign(Long campaignId, Long spellId, Authentication auth) {
        boolean isOwner = campaignMemberRepo.existsByCampaignIdAndUserEmailAndRole(campaignId, auth.getName(), CampaignRole.OWNER);
        if (!isOwner) throw new SecurityException("No autorizado para modificar la campaña");
        campaignSpellRepo.deleteByCampaignIdAndSpellId(campaignId, spellId);
    }
}