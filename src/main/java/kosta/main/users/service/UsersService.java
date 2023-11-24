package kosta.main.users.service;

import kosta.main.users.dto.UserCreateDto;
import kosta.main.users.dto.UserCreateResponseDto;
import kosta.main.users.dto.UserUpdateDto;
import kosta.main.users.dto.UsersResponseDto;
import kosta.main.users.entity.User;
import kosta.main.users.repository.UsersRepository;
import kosta.main.utils.CustomBeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;
    private final CustomBeanUtils customBeanUtils;
    public UsersResponseDto findMyProfile(Integer userId) {

        return UsersResponseDto.of(findUserByUserId(userId));
    }

    private User findUserByUserId(Integer userId) {
        return usersRepository.findById(userId).orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
    }

    public UserCreateResponseDto createUser(UserCreateDto userCreateDto) {
        return UserCreateResponseDto.of(usersRepository.save(User.createUser(userCreateDto)));
    }

    public UsersResponseDto updateUser(Integer userId, UserUpdateDto userUpdateDto) {
        User user = (User) customBeanUtils.copyNonNullProperties(findUserByUserId(userId), User.createUser(userUpdateDto));
        return UsersResponseDto.of(user);
    }

    public void withdrawalUser(Integer userId) {
        findUserByUserId(userId).deleteUser();
    }
}
