package kosta.main.users.service;

import kosta.main.bids.repository.BidRepository;
import kosta.main.blockedusers.entity.BlockedUser;
import kosta.main.blockedusers.repository.BlockedUsersRepository;

import kosta.main.communityposts.dto.CommunityPostListDTO;
import kosta.main.communityposts.entity.CommunityPost;
import kosta.main.communityposts.repository.CommunityPostsRepository;
import kosta.main.dibs.dto.DibResponseDto;
import kosta.main.email.service.EmailSendService;
import kosta.main.exchangeposts.entity.ExchangePost;
import kosta.main.exchangeposts.repository.ExchangePostsRepository;
import kosta.main.global.error.exception.BusinessException;
import kosta.main.global.s3upload.service.ImageService;
import kosta.main.reports.dto.CreateReportDTO;
import kosta.main.reports.entity.Report;
import kosta.main.reports.repository.ReportsRepository;
import kosta.main.users.dto.request.UserCreateDTO;
import kosta.main.users.dto.request.UserFindIdDTO;
import kosta.main.users.dto.request.UserFindPasswordDTO;
import kosta.main.users.dto.request.UserUpdateDTO;
import kosta.main.users.dto.response.*;
import kosta.main.users.entity.User;
import kosta.main.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static kosta.main.global.error.exception.CommonErrorCode.*;

@Service
@RequiredArgsConstructor
public class UsersService {

  private final UsersRepository usersRepository;
  private final ReportsRepository reportsRepository;
  private final ExchangePostsRepository exchangePostRepository;
  private final BlockedUsersRepository blockedUsersRepository;
  private final ImageService imageService;
  private final PasswordEncoder passwordEncoder;
  private final EmailSendService emailSendService;
  private final CommunityPostsRepository communityPostsRepository;
  private final BidRepository bidRepository;


  @Value("${profile}")
  private String basicProfileImage;


  @Transactional(readOnly = true)
  public UsersResponseDTO findMyProfile(User user) {
    return UsersResponseDTO.of(user);
  }

  @Transactional(readOnly = true)
  public UsersResponseDTO findProfileByName(String name) {
    UsersResponseDTO usersResponseDTO = usersRepository.findUserByUserName(name)
        .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));
    return usersResponseDTO;
  }

  @Transactional
  public UserCreateResponseDTO createUser(UserCreateDTO userCreateDTO) {
    if (!Objects.equals(userCreateDTO.getPassword(), userCreateDTO.getCheckPassword()))
      throw new BusinessException(INVALID_PASSWORD);
    String encryptedPassword = passwordEncoder.encode(userCreateDTO.getPassword());
    userCreateDTO.updatePassword(encryptedPassword);
    User user = User.createUser(userCreateDTO, basicProfileImage);

    return UserCreateResponseDTO.of(usersRepository.save(user));
  }

  @Transactional
  public UsersResponseDTO updateUser(User user, UserUpdateDTO userUpdateDTO, MultipartFile file) {
    String imagePath = imageService.resizeToProfileSizeAndUpload(file);
    userUpdateDTO.updateProfileImage(imagePath);

    //만약 비밀번호가 하나라도 널일경우 비밀번호는 통과
    //만약 두 비밀번호가 일치할 경우 userUpdateDto의 비밀번호를 encode한 비밀번호로 변경
    //일치하지 않을 경우 비밀번호가 다르다는 에러를 던짐
    if (userUpdateDTO.getPassword() != null && userUpdateDTO.getCheckPassword() != null) {
      if (Objects.equals(userUpdateDTO.getPassword(), userUpdateDTO.getCheckPassword())) {
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

  @Transactional
  public boolean blockUser(Integer blockUserId, User user) {
    User blockUser = findUserByUserId(blockUserId);
    user = findUserByUserId(user.getUserId());
    System.out.println("User before blockUser: " + user.toString());
    Optional<BlockedUser> first =
              user.getBlockedUsers()
                      .stream()
                      .filter(bUser -> Objects.equals(bUser.getBlockingUser().getUserId(), blockUser.getUserId()))
                      .findFirst();
      if(first.isPresent()){
        BlockedUser blockedUser = first.get();
        user.removeBlockedUser(blockedUser);
        blockedUsersRepository.delete(blockedUser);
        return false;
      }



    BlockedUser blockedUser = BlockedUser.builder()
        .user(user)
        .blockingUser(blockUser)
        .build();
    BlockedUser save = blockedUsersRepository.save(blockedUser);
    user.addBlockedUser(save);
    return true;
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
    if (userName && userPhone) {
      return userInfo.getEmail();
    }
    return null;
  }

  public String findIdByNamePhoneEmail(UserFindPasswordDTO userFindPasswordDTO) {
    UsersResponseDTO userInfo = usersRepository.findUserByUserName(userFindPasswordDTO.getName())
        .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

    boolean userEmail = userFindPasswordDTO.getEmail().equals(userInfo.getEmail());
    boolean userName = userFindPasswordDTO.getName().equals(userInfo.getName());
    boolean userPhone = userFindPasswordDTO.getPhone().equals(userInfo.getPhone());

    if (userEmail && userName && userPhone) {
      // 임시비번생성 및 커밋
      // 변경된 비번 해당 메일로 전송
      emailSendService.sendEmailNewPassword(userInfo.getEmail());
      return userInfo.getEmail();
    }
    return null;
  }

  public UserAllProfileResponseDTO findMyAllProfile(User user) {
    // failed to lazily initialize a collection of role
    Optional<User> findUser = usersRepository.findById(user.getUserId());
    User user1 = findUser.get();
    return UserAllProfileResponseDTO.from(user1);
  }

  public Page<UserExchangePostResponseDTO> findMyExchangePostList(Pageable pageable, User user){
    Page<ExchangePost> all = exchangePostRepository.findByUser_UserId(pageable, user.getUserId());
    return all
        .map(post -> {
          // 아이템 대표 이미지 URL을 가져오는 로직 (첫 번째 이미지를 대표 이미지로 사용)
          String imgUrl = !post.getItem().getImages().isEmpty() ? post.getItem().getImages().get(0) : null;

          // 해당 교환 게시글에 입찰된 Bid의 갯수를 세는 로직 + BidStatus가 DELETED인 것은 세지 않도록 하는 로직
          Integer bidCount = bidRepository.countByExchangePostAndStatusNotDeleted(post);

          return UserExchangePostResponseDTO.builder()
              .exchangePostId(post.getExchangePostId())
              .title(post.getTitle())
              .preferItem(post.getPreferItems())
              .address(post.getAddress())
              .exchangePostStatus(post.getExchangePostStatus().toString())
              .createdAt(post.getCreatedAt().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)))
              .imgUrl(imgUrl)
              .bidCount(bidCount)
              .build();
        });
  }

  public Page<CommunityPostListDTO> findMyCommunityPostList(Pageable pageable, User user){
    Page<CommunityPost> posts = communityPostsRepository.findByUser_UserId(pageable, user.getUserId());

    List<CommunityPostListDTO> list = posts.stream().map(post -> CommunityPostListDTO.from(post, user)).toList();
    return new PageImpl<>(list, posts.getPageable(), posts.getTotalElements());
  }
}
