package com.swd392.preOrderBlindBox.service.impl;

import com.swd392.preOrderBlindBox.entity.BlindboxAsset;
import com.swd392.preOrderBlindBox.enums.ErrorCode;
import com.swd392.preOrderBlindBox.exception.ResourceNotFoundException;
import com.swd392.preOrderBlindBox.repository.BlindboxAssetRepository;
import com.swd392.preOrderBlindBox.service.BlindboxAssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class BlindboxAssetServiceImpl implements BlindboxAssetService {
    private final BlindboxAssetRepository blindboxAssetRepository;

    @Override
    public List<BlindboxAsset> getAllBlindboxAssets() {
        return blindboxAssetRepository.findAll();
    }

    @Override
    public BlindboxAsset getBlindboxAssetById(Long id) {
        return blindboxAssetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.RESOURCES_NOT_FOUND));
    }

    @Override
    public List<BlindboxAsset> getBlindboxAssetsByBlindboxSeriesId(Long blindboxSeriesId) {
        return blindboxAssetRepository.findByBlindboxSeriesId(blindboxSeriesId);
    }

    @Override
    public BlindboxAsset createBlindboxAsset(BlindboxAsset blindboxAsset) {
        return blindboxAssetRepository.save(blindboxAsset);
    }

    @Override
    public BlindboxAsset updateBlindboxAsset(BlindboxAsset blindboxAsset, Long id) {
        BlindboxAsset oldBlindboxAsset = blindboxAssetRepository.findById(id).orElse(null);
        if (oldBlindboxAsset == null) {
            return null;
        }
        oldBlindboxAsset.setMediaKey(blindboxAsset.getMediaKey());
        return blindboxAssetRepository.save(oldBlindboxAsset);
    }

    @Override
    public void deleteBlindboxAsset(Long id) {
        blindboxAssetRepository.deleteById(id);
    }
}
