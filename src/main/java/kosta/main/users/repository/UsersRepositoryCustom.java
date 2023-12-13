package kosta.main.users.repository;

import kosta.main.users.dto.UsersResponseDTO;

import java.util.Optional;

public interface UsersRepositoryCustom {

    Optional<UsersResponseDTO> findUserByUserId(Integer userId);

    Optional<UsersResponseDTO> findUserByUserName(String name);
}
