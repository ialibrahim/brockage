package org.ialibrahim.brokerage.service;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.ialibrahim.brokerage.dao.AssetRepository;
import org.ialibrahim.brokerage.entity.AssetEntity;
import org.ialibrahim.brokerage.model.Asset;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AssetsService {
    public static final String TRY_ASSETS = "TRY";
    private final AssetRepository assetRepository;
    private final ModelMapper modelMapper;

    public List<Asset> listAssets(Long customerId) {
        return assetRepository.findByCustomerId(customerId).stream()
                .map(a->modelMapper.map(a, Asset.class)).toList();
    }

    public Asset deposite(Long customerId, Double amount) {

        // Check if customer already has TRY (Turkish Lira) asset
        AssetEntity asset = assetRepository.findByCustomerIdAndAssetName(customerId, TRY_ASSETS)
                .orElseGet(() -> new AssetEntity(null, customerId, TRY_ASSETS, 0d, 0d));

        // Update the asset size (total amount of TRY)
        asset.setSize(asset.getSize() + amount);
        asset.setUsableSize(asset.getUsableSize() + amount);

        assetRepository.save(asset);

        return modelMapper.map(asset, Asset.class);
    }

    public Asset withdraw(Long customerId, Double amount) throws BadRequestException {

        // Check if customer already has TRY (Turkish Lira) asset
        Optional<AssetEntity> assetOptional = assetRepository.findByCustomerIdAndAssetName(customerId, TRY_ASSETS);
        if (assetOptional.isEmpty()) {
            throw new BadRequestException("Customer has no TRY assets");
        }

        AssetEntity asset = assetOptional.get();
        if (asset.getUsableSize() < amount) {
            throw new BadRequestException("Customer's usable TRY assets is less than the required amount");
        }

        asset.setSize(asset.getSize() - amount);
        asset.setUsableSize(asset.getUsableSize() - amount);
        assetRepository.save(asset);
        return modelMapper.map(asset, Asset.class);
    }

    public Double getUsableSize(Long customerId, String assetName) {
        return assetRepository.findByCustomerIdAndAssetName(customerId, assetName)
                .map(AssetEntity::getUsableSize).orElse(0d);
    }

    public Double getUsableFund(Long customerId) {
        return assetRepository.findByCustomerIdAndAssetName(customerId, TRY_ASSETS)
                .map(AssetEntity::getUsableSize).orElse(0d);
    }
}
