package kosta.main.users.service;

import kosta.main.blockedusers.entity.BlockedUser;
import kosta.main.blockedusers.repository.BlockedUsersRepository;
import kosta.main.dibs.dto.DibResponseDto;
import kosta.main.exchangehistories.dto.ExchangeHistoryResponseDto;
import kosta.main.global.s3upload.service.ImageService;
import kosta.main.reports.dto.CreateReportDto;
import kosta.main.reports.entity.Report;
import kosta.main.reports.repository.ReportsRepository;
import kosta.main.users.dto.*;
import kosta.main.users.entity.User;
import kosta.main.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;
    private final ReportsRepository reportsRepository;
    private final BlockedUsersRepository blockedUsersRepository;
    private final ImageService imageService;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UsersResponseDto findMyProfile(User user) {
        return UsersResponseDto.of(user);
    }

    @Transactional
    public UserCreateResponseDto createUser(UserCreateDto userCreateDto) {
        String encryptedPassword  = passwordEncoder.encode(userCreateDto.getPassword());
        userCreateDto.updatePassword(encryptedPassword);
        User user = User.createUser(userCreateDto);


        return UserCreateResponseDto.of(usersRepository.save(user));
    }

    @Transactional
    public UsersResponseDto updateUser(User user, UserUpdateDto userUpdateDto, MultipartFile file) {
        String imagePath = imageService.resizeToProfileSizeAndUpload(file);
        userUpdateDto.updateProfileImage(imagePath);

        User updatedUser = user.updateUser(userUpdateDto);
        return UsersResponseDto.of(usersRepository.save(updatedUser));
    }
    @Transactional
    public void withdrawalUser(User user) {
        user.deleteUser();
        usersRepository.save(user);
    }

    private User findUserByUserId(Integer userId) {
        return usersRepository.findById(userId).orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
    }

    public void reportUser(Integer reportedUserId, User reporterUser, CreateReportDto createReportDto) {
        User reportedUser = findUserByUserId(reportedUserId);
        reportsRepository
                .save(Report.builder()
                .reporter(reporterUser)
                .reportedUser(reportedUser)
                .reason(createReportDto.getReason())
                .build());
    }

    public void blockUser(Integer blockUserId, Integer userId) {
        User blockUser = findUserByUserId(blockUserId);
        User user = findUserByUserId(userId);

        BlockedUser blockedUser = BlockedUser.builder()
                .user(user)
                .blockingUser(blockUser)
                .build();
        user.addBlockedUser(blockedUser);
        blockedUsersRepository.save(blockedUser);
    }

    public List<ExchangeHistoryResponseDto> findMyExchangeHistory(User user) {
        return user.getExchangeHistories()
                .stream().map(ExchangeHistoryResponseDto::of).toList();
    }

    public List<DibResponseDto> findMyDibs(User user) {
        return user.getDibs().stream().map(DibResponseDto::of).toList();
    }
}
