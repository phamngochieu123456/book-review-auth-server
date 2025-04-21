package com.hieupn.auth.mapper;

import com.hieupn.auth.model.dto.RedirectUriDTO;
import com.hieupn.auth.model.entity.RedirectUri;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RedirectUriMapper {

    RedirectUriMapper INSTANCE = Mappers.getMapper(RedirectUriMapper.class);

    @Mapping(target = "registeredClientRedirectUris", ignore = true)
    RedirectUri toRedirectUri(RedirectUriDTO redirectUriDTO);

    RedirectUriDTO toRedirectUriDTO(RedirectUri redirectUri);

    List<RedirectUriDTO> toRedirectUriDTOs(List<RedirectUri> redirectUris);
}
