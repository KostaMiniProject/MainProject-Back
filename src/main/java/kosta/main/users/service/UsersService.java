package kosta.main.users.service;

import kosta.main.blockedusers.entity.BlockedUser;
import kosta.main.blockedusers.repository.BlockedUsersRepository;
import kosta.main.dibs.dto.DibResponseDto;
import kosta.main.exchangehistories.dto.ExchangeHistoryResponseDto;
import kosta.main.global.error.exception.BusinessException;
import kosta.main.global.s3upload.service.ImageService;
import kosta.main.reports.dto.CreateReportDTO;
import kosta.main.reports.entity.Report;
import kosta.main.reports.repository.ReportsRepository;
import kosta.main.users.dto.*;
import kosta.main.users.entity.User;
import kosta.main.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

import static kosta.main.global.error.exception.ErrorCode.INVALID_PASSWORD;
import static kosta.main.global.error.exception.ErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;
    private final ReportsRepository reportsRepository;
    private final BlockedUsersRepository blockedUsersRepository;
    private final ImageService imageService;
    private final PasswordEncoder passwordEncoder;
    @Value("${profile}")
    private String basicProfileImage;

    @Transactional(readOnly = true)
    public UsersResponseDTO findMyProfile(User user) {
        return UsersResponseDTO.of(user);
    }

    @Transactional
    public UserCreateResponseDTO createUser(UserCreateDTO userCreateDTO) {
        String encryptedPassword  = passwordEncoder.encode(userCreateDTO.getPassword());
        userCreateDTO.updatePassword(encryptedPassword);
        User user = User.createUser(userCreateDTO,basicProfileImage);


        return UserCreateResponseDTO.of(usersRepository.save(user));
    }

    @Transactional
    public UsersResponseDTO updateUser(User user, UserUpdateDTO userUpdateDTO, MultipartFile file) {
        String imagePath = imageService.resizeToProfileSizeAndUpload(file);
        userUpdateDTO.updateProfileImage(imagePath);
        if(!Objects.equals(userUpdateDTO.getPassword(), userUpdateDTO.getCheckPassword()))
            throw new BusinessException(INVALID_PASSWORD);
        User updatedUser = user.updateUser(userUpdateDTO);
        return UsersResponseDTO.of(usersRepository.save(updatedUser));
    }
    @Transactional
    public void withdrawalUser(User user) {
        usersRepository.delete(user);
    }

    public User findUserByUserId(Integer userId) {
        return usersRepository.findById(userId).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));
    }

    public void reportUser(Integer reportedUserId, User reporterUser, CreateReportDTO createReportDTO) {
        User reportedUser = findUserByUserId(reportedUserId);
        reportsRepository
                .save(Report.builder()
                .reporter(reporterUser)
                .reportedUser(reportedUser)
                .reason(createReportDTO.getReason())
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
