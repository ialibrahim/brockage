package org.ialibrahim.brokerage.dao;

import org.ialibrahim.brokerage.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByCustomerId(Long customerId);

    @Query("SELECT o from OrderEntity o where o.customerId=:customerId " +
            "AND (o.createDate>=:startDate or :startDate is null) " +
            "AND (o.createDate<=:endDate or :endDate is null) ")
    List<OrderEntity> findByCustomerIdAndCreateDateBetween(Long customerId, LocalDateTime startDate, LocalDateTime endDate);
}
