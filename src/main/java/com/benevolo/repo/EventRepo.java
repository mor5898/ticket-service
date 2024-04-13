package com.benevolo.repo;

import com.benevolo.entity.EventEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EventRepo implements PanacheRepositoryBase<EventEntity, String> {
}
