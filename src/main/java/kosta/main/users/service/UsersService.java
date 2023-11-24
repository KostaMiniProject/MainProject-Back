package kosta.main.users.service;

import kosta.main.users.dto.UserCreateDto;
import kosta.main.users.dto.UserCreateResponseDto;
import kosta.main.users.dto.UserUpdateDto;
import kosta.main.users.dto.UsersResponseDto;
import kosta.main.users.entity.User;
import kosta.main.users.repository.UsersRepository;
import kosta.main.users.repository.UsersRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;
    private final UsersRepositoryCustom usersRepositoryCustom;

    @Transactional(readOnly = true)
    public UsersResponseDto findMyProfile(Integer userId) {
        return usersRepositoryCustom.findUserByUserId(userId).orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
    }

    @Transactional
    public UserCreateResponseDto createUser(UserCreateDto userCreateDto) {
        return UserCreateResponseDto.of(usersRepository.save(User.createUser(userCreateDto)));
    }

    @Transactional
    public UsersResponseDto updateUser(Integer userId, UserUpdateDto userUpdateDto) {

        return UsersResponseDto.of(usersRepository.save(findUserByUserId(userId).updateUser(userUpdateDto)));
    }
    @Transactional
    public void withdrawalUser(Integer userId) {
        User user = findUserByUserId(userId);
        user.deleteUser();
        usersRepository.save(user);
    }

    private User findUserByUserId(Integer userId) {
        return usersRepository.findById(userId).orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
    }
}
