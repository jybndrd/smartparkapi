package com.demo.smartpark.mapper;

import com.demo.smartpark.dto.UserDto;
import com.demo.smartpark.entity.User;
import org.mapstruct.Mapper;

/**
 * @author jandrada
 */
@Mapper(componentModel = "spring", config = GlobalMapperConfig.class)
public interface UserMapper {

    UserDto userToUserDto (User user);

    User userDtoToUser (UserDto userDto);
}
