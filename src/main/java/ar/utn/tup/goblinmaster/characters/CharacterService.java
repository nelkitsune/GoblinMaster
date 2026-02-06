package ar.utn.tup.goblinmaster.characters;

import ar.utn.tup.goblinmaster.campaigns.Campaign;
import ar.utn.tup.goblinmaster.campaigns.CampaignCharacter;
import ar.utn.tup.goblinmaster.campaigns.CampaignCharacterRepository;
import ar.utn.tup.goblinmaster.campaigns.CampaignRepository;
import ar.utn.tup.goblinmaster.characters.dto.CharacterCreateRequest;
import ar.utn.tup.goblinmaster.characters.dto.CharacterResponse;
import ar.utn.tup.goblinmaster.users.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class CharacterService {

    private final CharacterRepository characters;
    private final UserRepository users;
    private final CampaignRepository campaigns;
    private final CampaignCharacterRepository campaignCharacters;

    public CharacterService(CharacterRepository characters, UserRepository users,
                            CampaignRepository campaigns, CampaignCharacterRepository campaignCharacters) {
        this.characters = characters; this.users = users; this.campaigns = campaigns; this.campaignCharacters = campaignCharacters;
    }

    @Transactional
    public CharacterResponse create(CharacterCreateRequest req) {
        var user = req.userId() != null ? users.findById(req.userId()).orElse(null) : null;
        boolean npc = req.isNpc() != null ? req.isNpc() : (user == null);
        Character c = Character.builder()
                .user(user)
                .name(req.name())
                .maxHp(req.maxHp())
                .baseInitiative(req.baseInitiative())
                .isNpc(npc)
                .build();
        c = characters.save(c);
        return toDto(c);
    }

    @Transactional(readOnly = true)
    public List<CharacterResponse> list() {
        return characters.findAll().stream()
                .filter(c -> c.getDeletedAt() == null)
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public CharacterResponse get(Long id) {
        Character c = characters.findById(id).orElseThrow();
        if (c.getDeletedAt() != null) throw new IllegalStateException("Personaje eliminado");
        return toDto(c);
    }

    @Transactional
    public CharacterResponse update(Long id, CharacterCreateRequest req) {
        Character c = characters.findById(id).orElseThrow();
        var user = req.userId() != null ? users.findById(req.userId()).orElse(null) : null;
        c.setUser(user);
        if (req.name() != null) c.setName(req.name());
        if (req.maxHp() != null) c.setMaxHp(req.maxHp());
        if (req.baseInitiative() != null) c.setBaseInitiative(req.baseInitiative());
        if (req.isNpc() != null) c.setIsNpc(req.isNpc()); else c.setIsNpc(user == null);
        return toDto(characters.save(c));
    }

    @Transactional
    public void delete(Long id) {
        Character c = characters.findById(id).orElseThrow();
        c.setDeletedAt(Instant.now());
        characters.save(c);
    }

    @Transactional
    public void addToCampaign(Long characterId, Long campaignId) {
        Character ch = characters.findById(characterId).orElseThrow();
        Campaign cp = campaigns.findById(campaignId).orElseThrow();
        if (campaignCharacters.existsByCampaignIdAndCharacterId(campaignId, characterId)) return;
        CampaignCharacter cc = CampaignCharacter.builder().campaign(cp).character(ch).build();
        campaignCharacters.save(cc);
    }

    private CharacterResponse toDto(Character c) {
        return new CharacterResponse(
                c.getId(),
                c.getUser() != null ? c.getUser().getId() : null,
                c.getName(),
                c.getMaxHp(),
                c.getBaseInitiative(),
                c.getIsNpc()
        );
    }
}
