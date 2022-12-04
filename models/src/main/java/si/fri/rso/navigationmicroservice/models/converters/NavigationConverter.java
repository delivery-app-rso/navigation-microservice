package si.fri.rso.navigationmicroservice.models.converters;

import si.fri.rso.navigationmicroservice.lib.Navigation;
import si.fri.rso.navigationmicroservice.models.entities.NavigationEntity;

public class NavigationConverter {//should be ok

    public static Navigation toDto(NavigationEntity entity) {

        Navigation dto = new Navigation();
        dto.setId(entity.getId());
        dto.setSender(entity.getSender());
        dto.setReceiver(entity.getReceiver());
        dto.setSentOn(entity.getSentOn());
        dto.setDeliveredOn(entity.getDeliveredOn());

        return dto;

    }

    public static NavigationEntity toEntity(Navigation dto) {

        NavigationEntity entity = new NavigationEntity();
        entity.setId(dto.getId());
        entity.setSender(dto.getSender());
        entity.setReceiver(dto.getReceiver());
        entity.setSentOn(dto.getSentOn());
        entity.setDeliveredOn(dto.getDeliveredOn());

        return entity;

    }

}
