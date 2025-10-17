
package ar.utn.tup.goblinmaster.feats.mapper;

import ar.utn.tup.goblinmaster.feats.dto.FeatsRequest;
import ar.utn.tup.goblinmaster.feats.dto.FeatsResponse;
import ar.utn.tup.goblinmaster.feats.entity.Feats;

import java.util.List;

public class FeatsMapper {

    public static FeatsResponse toResponse(Feats feats) {
        FeatsResponse response = new FeatsResponse();
        response.setId(feats.getId());
        response.setName(feats.getName());
        response.setOriginalName(feats.getOriginalName());
        response.setCode(feats.getCode());
        response.setSource(feats.getSource());
        response.setBenefit(feats.getBenefit());
        response.setSpecial(feats.getSpecial());
        response.setTipo(List.of(feats.getTipo()));
        return response;
    }

    public static Feats toEntity(FeatsRequest request) {
        Feats feats = new Feats();
        feats.setName(request.getName());
        feats.setOriginalName(request.getOriginalName());
        feats.setCode(request.getCode());
        feats.setSource(request.getSource());
        feats.setBenefit(request.getBenefit());
        feats.setSpecial(request.getSpecial());
        feats.setTipo(request.getTipo().isEmpty() ? null : request.getTipo().get(0));
        return feats;
    }
}