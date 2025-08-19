package homework.mapper;

import homework.dto.request.UserRequestTo;
import homework.dto.response.UserResponseTo;
import homework.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "age", defaultValue = "0")
    User toUser(UserRequestTo userRequestTo);

    UserResponseTo toUserResponseTo(User user);
}
