package com.hieupn.auth.mapper;

import com.hieupn.auth.model.dto.ClientAuthenticationMethodDTO;
import com.hieupn.auth.model.entity.ClientAuthenticationMethod;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClientAuthenticationMethodMapper {

    ClientAuthenticationMethodMapper INSTANCE = Mappers.getMapper(ClientAuthenticationMethodMapper.class);

    @Mapping(target = "registeredClientAuthenticationMethods", ignore = true)
    ClientAuthenticationMethod toClientAuthenticationMethod(ClientAuthenticationMethodDTO clientAuthenticationMethodDTO);

    ClientAuthenticationMethodDTO toClientAuthenticationMethodDTO(ClientAuthenticationMethod clientAuthenticationMethod);

    List<ClientAuthenticationMethodDTO> toClientAuthenticationMethodDTOs(List<ClientAuthenticationMethod> clientAuthenticationMethods);
}
