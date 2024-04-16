package com.benevolo.repo;

import com.benevolo.entity.TicketTypeEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TicketTypeRepo implements PanacheRepositoryBase<TicketTypeEntity, String> {

}
