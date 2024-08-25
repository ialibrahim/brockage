package org.ialibrahim.brokerage.dao;

import org.ialibrahim.brokerage.entity.AssetEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssetRepository extends JpaRepository<AssetEntity, Long> {
    List<AssetEntity> findByCustomerId(Long customerId);

    Optional<AssetEntity> findByCustomerIdAndAssetName(Long customerId, String assetName);
}