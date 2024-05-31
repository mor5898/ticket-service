package com.benevolo.repo;

import com.benevolo.entity.Cancellation;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CancellationRepo implements PanacheRepositoryBase<Cancellation, String> {

}
