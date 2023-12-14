package kosta.main.users.service;

import kosta.main.blockedusers.entity.BlockedUser;
import kosta.main.blockedusers.repository.BlockedUsersRepository;
import kosta.main.dibs.dto.DibResponseDto;
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

import static kosta.main.global.error.exception.CommonErrorCode.INVALID_PASSWORD;
import static kosta.main.global.error.exception.CommonErrorCode.USER_NOT_FOUND;

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

    @Transactional(readOnly = true)
    public UsersResponseDTO findProfileByName(String name){
        UsersResponseDTO usersResponseDTO = usersRepository.findUserByUserName(name)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));
        return usersResponseDTO;
    }

    @Transactional
    public UserCreateResponseDTO createUser(UserCreateDTO userCreateDTO) {
        if(!Objects.equals(userCreateDTO.getPassword(), userCreateDTO.getCheckPassword()))
            throw new BusinessException(INVALID_PASSWORD);
        String encryptedPassword  = passwordEncoder.encode(userCreateDTO.getPassword());
        userCreateDTO.updatePassword(encryptedPassword);
        User user = User.createUser(userCreateDTO,basicProfileImage);

        return UserCreateResponseDTO.of(usersRepository.save(user));
    }

    @Transactional
    public UsersResponseDTO updateUser(User user, UserUpdateDTO userUpdateDTO, MultipartFile file) {
        String imagePath = imageService.resizeToProfileSizeAndUpload(file);
        userUpdateDTO.updateProfileImage(imagePath);

        //만약 비밀번호가 하나라도 널일경우 비밀번호는 통과
        //만약 두 비밀번호가 일치할 경우 userUpdateDto의 비밀번호를 encode한 비밀번호로 변경
        //일치하지 않을 경우 비밀번호가 다르다는 에러를 던짐
        if(userUpdateDTO.getPassword() != null && userUpdateDTO.getCheckPassword() != null){
            if(Objects.equals(userUpdateDTO.getPassword(), userUpdateDTO.getCheckPassword())){
                String encodePassword = passwordEncoder.encode(userUpdateDTO.getPassword());
                userUpdateDTO.updatePassword(encodePassword);
            } else throw new BusinessException(INVALID_PASSWORD);

        }
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

//    public List<ExchangeHistoryResponseDTO> findMyExchangeHistory(User user) {
//        return user.getExchangeHistories()
//                .stream().map(ExchangeHistoryResponseDTO::of).toList();
//    }

    public List<DibResponseDto> findMyDibs(User user) {
        return user.getDibs().stream().map(DibResponseDto::of).toList();
    }

    public String findIdByNamePhone(UserFindIdDTO userFindIdDTO) {
        UsersResponseDTO userInfo = usersRepository.findUserByUserName(userFindIdDTO.getName())
            .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        boolean userName = userFindIdDTO.getName().equals(userInfo.getName());
        boolean userPhone = userFindIdDTO.getPhone().equals(userInfo.getPhone());
        if(userName && userPhone) {
            return userInfo.getEmail();
        }
        return "입력값과 등록된 정보가 일치하지않습니다.";
    }
}
