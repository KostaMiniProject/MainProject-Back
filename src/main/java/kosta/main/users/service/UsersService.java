package kosta.main.users.service;

import kosta.main.bids.repository.BidRepository;
import kosta.main.blockedusers.dto.BlockedUserResponseDTO;
import kosta.main.blockedusers.entity.BlockedUser;
import kosta.main.blockedusers.repository.BlockedUsersRepository;

import kosta.main.comments.repository.CommentsRepository;
import kosta.main.communityposts.dto.CommunityPostListDTO;
import kosta.main.communityposts.entity.CommunityPost;
import kosta.main.communityposts.repository.CommunityPostsRepository;
import kosta.main.dibs.dto.DibResponseDto;
import kosta.main.email.service.EmailSendService;
import kosta.main.exchangeposts.entity.ExchangePost;
import kosta.main.exchangeposts.repository.ExchangePostsRepository;
import kosta.main.global.error.exception.BusinessException;
import kosta.main.global.s3upload.service.ImageService;
import kosta.main.items.repository.ItemsRepository;
import kosta.main.reports.dto.CreateReportDTO;
import kosta.main.reports.entity.Report;
import kosta.main.reports.repository.ReportsRepository;
import kosta.main.users.auth.oauth2.dto.OauthSignUpDTO;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static kosta.main.global.error.exception.CommonErrorCode.*;

@Service
@RequiredArgsConstructor
public class UsersService {

  //repository
  private final UsersRepository usersRepository;
  private final ReportsRepository reportsRepository;
  private final ExchangePostsRepository exchangePostRepository;
  private final BlockedUsersRepository blockedUsersRepository;
  private final CommunityPostsRepository communityPostsRepository;
  private final BidRepository bidRepository;
  private final ItemsRepository itemsRepository;
  private final CommentsRepository commentsRepository;
  private final ImageService imageService;
  private final PasswordEncoder passwordEncoder;
  private final EmailSendService emailSendService;


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

  @Transactional(readOnly = true)
  public UsersResponseDTO findProfileByUserId(Integer userId) {
    UsersResponseDTO usersResponseDTO = usersRepository.findUserByUserId(userId)
        .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));
    return usersResponseDTO;
  }

  @Transactional
  public UserCreateResponseDTO createUser(UserCreateDTO userCreateDTO) {
    if (!Objects.equals(userCreateDTO.getPassword(), userCreateDTO.getPasswordConfirm()))
      throw new BusinessException(INVALID_PASSWORD);
    String encryptedPassword = passwordEncoder.encode(userCreateDTO.getPassword());
    userCreateDTO.updatePassword(encryptedPassword);
    Optional<User> userByEmail = usersRepository.findByEmailAnyOne(userCreateDTO.getEmail());
    User user = null;
    if(userByEmail.isEmpty()) {
      user = User.createUser(userCreateDTO, basicProfileImage);
    } else{
      user = userByEmail.get();
      user.updateUser(userCreateDTO);
      user.updateStatus();
    }
    return UserCreateResponseDTO.of(usersRepository.save(user));
  }

  @Transactional
  public UsersResponseDTO oauthUpdateUser(OauthSignUpDTO oauthSignUpDTO) {
    String email = oauthSignUpDTO.getEmail();
    Optional<User> userByEmail = usersRepository.findUserByEmail(email);
    User user = userByEmail.orElseThrow(() -> new BusinessException(USER_NOT_FOUND));
    User updatedUser = user.oauthSignUp(oauthSignUpDTO);
    return UsersResponseDTO.of(usersRepository.save(updatedUser));
  }

  @Transactional
  public UsersResponseDTO updateUser(User user, UserUpdateDTO userUpdateDTO, MultipartFile file) {
    // 프로필 이미지 업로드 및 경로 설정
    if (file != null && !file.isEmpty()) {
      String imagePath = imageService.resizeToProfileSizeAndUpload(file);
      userUpdateDTO.updateProfileImage(imagePath);
    } else {
      userUpdateDTO.updateProfileImage(user.getProfileImage());
    }

    // 비밀번호 처리
    if (userUpdateDTO.getPassword() != null && userUpdateDTO.getCheckPassword() != null) {
      if (Objects.equals(userUpdateDTO.getPassword(), userUpdateDTO.getCheckPassword())) {
        String encodePassword = passwordEncoder.encode(userUpdateDTO.getPassword());
        userUpdateDTO.updatePassword(encodePassword);
      } else {
        throw new BusinessException(INVALID_PASSWORD);
      }
    } else {
      userUpdateDTO.updatePassword(user.getPassword());
    }


    // 나머지 필드 업데이트
    if (userUpdateDTO.getNickName()== null) {
      userUpdateDTO.updateNickName(user.getName());
    }
    if (userUpdateDTO.getPhone() == null) {
      userUpdateDTO.updatePhone(user.getPhone());
    }
    User updatedUser = user.updateUser(userUpdateDTO);
    return UsersResponseDTO.of(usersRepository.save(updatedUser));
  }


  @Transactional
  public void withdrawalUser(User user) {
    //exchange delete
    Optional<User> userByEmail = usersRepository.findUserByEmail(user.getEmail());
    if (userByEmail.isEmpty()) throw new BusinessException(USER_NOT_FOUND);
    else user = userByEmail.get();
    Integer userId = user.getUserId();
    //comment delete
    commentsRepository.deleteAll(commentsRepository.findCommentByUser_UserId(userId));
    //community delete
    communityPostsRepository.deleteAll(communityPostsRepository.findByUser_UserId(userId));
    //item delete
    itemsRepository.deleteAll(user.getItems());
    //bid Delete 처리
    bidRepository.deleteAll(bidRepository.findByUser_UserId(userId));

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
    if (first.isPresent()) {
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

  @Transactional(readOnly = true)
  public List<BlockedUserResponseDTO> getBlockedUsers(User user) {
    user = findUserByUserId(user.getUserId()); // 차단 목록을 가져올 사용자
    List<BlockedUser> blockedUsers = user.getBlockedUsers(); // 차단 목록

    List<BlockedUserResponseDTO> blockedUsersList = new ArrayList<>();
    for (BlockedUser blockedUser : blockedUsers) {
      BlockedUserResponseDTO dto = BlockedUserResponseDTO.from(
          blockedUser.getUser(),
          blockedUser.getBlockingUser(),
          blockedUser.getCreatedAt(),
          blockedUser.getUpdatedAt()
      );
      blockedUsersList.add(dto);
    }

    return blockedUsersList; // 차단된 사용자 목록을 반환
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

//    boolean userName = userFindIdDTO.getName().equals(userInfo.getName());
    //userName의 경우 아이디를 찾는데 사용했기 때문에 불필요
    boolean userPhone = userFindIdDTO.getPhone().equals(userInfo.getPhone());
    if (userPhone) {
      return userInfo.getEmail();
    }
    return null;
  }

  public String findIdByNamePhoneEmail(UserFindPasswordDTO userFindPasswordDTO) {
    UsersResponseDTO userInfo = usersRepository.findUserByUserName(userFindPasswordDTO.getName())
            .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

    boolean userEmail = userFindPasswordDTO.getEmail().equals(userInfo.getEmail());
//    boolean userName = userFindPasswordDTO.getName().equals(userInfo.getName()); 위에서 이름을 통해 찾아오기 때문에 굳이 체크할 필요없음
    boolean userPhone = userFindPasswordDTO.getPhone().equals(userInfo.getPhone());

    if (userEmail && userPhone) {
      // 임시비번생성 및 커밋
      // 변경된 비번 해당 메일로 전송
      emailSendService.sendEmailNewPassword(userInfo.getEmail());
      return userInfo.getEmail();
    }
    return null;
  }

//  public UserAllProfileResponseDTO findMyAllProfile(User user) {
//    // failed to lazily initialize a collection of role
//    Optional<User> findUser = usersRepository.findById(user.getUserId());
//    User user1 = findUser.get();
//    return UserAllProfileResponseDTO.from(user1);
//  }

  public Page<UserExchangePostResponseDTO> findMyExchangePostList(Pageable pageable, User user) {
    Page<ExchangePost> all = exchangePostRepository.findByUser_UserId(pageable,user.getUserId());
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

  public Page<CommunityPostListDTO> findMyCommunityPostList(Pageable pageable, User user) {
    Page<CommunityPost> posts = communityPostsRepository.findByUser_UserId(pageable, user.getUserId());
    List<CommunityPostListDTO> list = posts.stream().map(post -> CommunityPostListDTO.from(post, user)).toList();
    return new PageImpl<>(list, posts.getPageable(), posts.getTotalElements());
  }
}
