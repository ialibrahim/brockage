package org.ialibrahim.brokerage.model;

import org.ialibrahim.brokerage.entity.AssetEntity;
import org.ialibrahim.brokerage.entity.OrderEntity;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class MapperFactory {

    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.createTypeMap(Order.class, OrderEntity.class).addMappings(mp -> {
            mp.skip(OrderEntity::setId);
            mp.skip(OrderEntity::setStatus);
            mp.skip(OrderEntity::setCreateDate);
        });

        modelMapper.createTypeMap(Asset.class, AssetEntity.class).addMappings(mp -> {
            mp.skip(AssetEntity::setId);
        });

        return modelMapper;
    }
}
