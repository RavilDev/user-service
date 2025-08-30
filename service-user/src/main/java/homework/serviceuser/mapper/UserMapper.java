package homework.serviceuser.mapper;

import homework.serviceuser.dto.request.UserRequestTo;
import homework.serviceuser.dto.response.UserResponseTo;
import homework.serviceuser.entity.User;
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
