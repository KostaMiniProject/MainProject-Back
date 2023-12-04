package kosta.main.users.repository;

import kosta.main.users.dto.UsersResponseDto;
import kosta.main.users.entity.User;

import java.util.Optional;

public interface UsersRepositoryCustom {

    Optional<UsersResponseDto> findUserByUserId(Integer userId);

}
