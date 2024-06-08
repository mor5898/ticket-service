package com.benevolo.repo;

import com.benevolo.entity.RefundLink;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RefundLinkRepo implements PanacheRepositoryBase<RefundLink, String> {

}
