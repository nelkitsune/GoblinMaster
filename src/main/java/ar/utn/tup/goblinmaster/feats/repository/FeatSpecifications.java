package ar.utn.tup.goblinmaster.feats.repository;

import ar.utn.tup.goblinmaster.feats.entity.Feats;
import ar.utn.tup.goblinmaster.feats.entity.Feats.Tipo;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class FeatSpecifications {
    public static Specification<Feats> ownerIsNull() {
        return (root, query, cb) -> cb.isNull(root.get("owner"));
    }

    public static Specification<Feats> hasTipoIn(List<Tipo> tipos) {
        if (tipos == null || tipos.isEmpty()) {
            return (root, query, cb) -> cb.conjunction();
        }
        return (root, query, cb) -> root.get("tipo").in(tipos);
    }
}

