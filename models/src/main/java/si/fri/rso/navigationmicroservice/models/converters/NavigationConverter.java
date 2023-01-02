package si.fri.rso.navigationmicroservice.models.converters;

import si.fri.rso.navigationmicroservice.lib.Navigation;
import si.fri.rso.navigationmicroservice.models.entities.NavigationEntity;

public class NavigationConverter {

    public static Navigation toDto(NavigationEntity entity) {

        Navigation dto = new Navigation();
        dto.setId(entity.getId());
        dto.setDeliveryId(entity.getDeliveryId());
        dto.setDistance(entity.getDistance());
        dto.setTime(entity.getTime());

        return dto;

    }

    public static NavigationEntity toEntity(Navigation dto) {

        NavigationEntity entity = new NavigationEntity();
        entity.setId(dto.getId());
        entity.setDeliveryId(dto.getDeliveryId());
        entity.setDistance(dto.getDistance());
        entity.setTime(dto.getTime());

        return entity;

    }

}
