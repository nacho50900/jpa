package uo.ri.cws.infrastructure.persistence.jpa.util;

import jakarta.persistence.PreUpdate;
import uo.ri.cws.domain.base.BaseEntity;

public class EntityListener {

	@PreUpdate
    public void preUpdate(BaseEntity entity) {
    	entity.updatedNow();
    }
	
}
