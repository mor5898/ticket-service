package com.benevolo.repo;

import com.benevolo.entity.CustomerEntity;
import com.benevolo.entity.TicketEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CustomerRepo implements PanacheRepositoryBase<CustomerEntity, String> {

}
