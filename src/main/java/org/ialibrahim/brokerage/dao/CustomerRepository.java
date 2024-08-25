package org.ialibrahim.brokerage.dao;

import org.ialibrahim.brokerage.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
    CustomerEntity getByUsername(String username);

}
