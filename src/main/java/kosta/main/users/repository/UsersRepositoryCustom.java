package kosta.main.users.repository;

import kosta.main.users.dto.UsersResponseDto;

import java.util.Optional;

public interface UsersRepositoryCustom {

    Optional<UsersResponseDto> findUserByUserId(Integer userId);
}
