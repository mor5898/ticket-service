package com.benevolo.repo;

import com.benevolo.entity.TicketEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class TicketRepo implements PanacheRepositoryBase<TicketEntity, String> {

}
