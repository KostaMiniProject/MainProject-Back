package kosta.main.users.controller;

import jakarta.validation.Valid;
import kosta.main.communityposts.dto.CommunityPostDetailDTO;
import kosta.main.communityposts.dto.CommunityPostListDTO;
import kosta.main.exchangeposts.dto.ExchangePostListDTO;
import kosta.main.global.dto.PageInfo;
import kosta.main.global.dto.PageResponseDto;
import kosta.main.reports.dto.CreateReportDTO;
import kosta.main.users.dto.request.*;
import kosta.main.users.dto.response.UserExchangePostResponseDTO;
import kosta.main.users.entity.LoginUser;
import kosta.main.users.entity.User;
import kosta.main.users.service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class UsersController {

  private final UsersService usersService;

  //내 정보 조회와 유저 프로필 조회를 나눠 둘 필요가있을지 잘 모르겠음
  @GetMapping("/users")
  public ResponseEntity<?> findMyProfile(@LoginUser User user) {
    return ResponseEntity.ok(usersService.findMyProfile(user));
  }

  @GetMapping("/users/profile")
  public ResponseEntity<?> findProfileByName(@RequestParam(value = "name") String name) {
    return ResponseEntity.ok(usersService.findProfileByName(name));
  }

  @GetMapping("/users/my-profile")
  public ResponseEntity<?> findMyAllProfile(@LoginUser User user){
    return new ResponseEntity<>(usersService.findMyAllProfile(user), HttpStatus.OK);
  }

  @PostMapping("/signup")
  public ResponseEntity<?> signup(@Valid @RequestBody UserCreateDTO userCreateDto) {
    return new ResponseEntity(usersService.createUser(userCreateDto), HttpStatus.CREATED);
  }

  @PutMapping("/users")
  public ResponseEntity<?> updateMyInfo(@LoginUser User user,
                                        @Valid @RequestPart("userUpdateDto") UserUpdateDTO userUpdateDTO,
                                        @Valid @RequestPart(value = "file") MultipartFile file) {

    return ResponseEntity.ok(usersService.updateUser(user, userUpdateDTO, file));
  }

  @DeleteMapping("/users/withdrawal")
  public ResponseEntity<?> withdrawal(@LoginUser User user) {
    usersService.withdrawalUser(user);
    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  @PostMapping("/users/report/{reportedUserId}")
  public ResponseEntity<?> reportUser(@PathVariable("reportedUserId") Integer reportedUserId,
                                      @LoginUser User user,
                                      @RequestBody CreateReportDTO createReportDTO) {
    usersService.reportUser(reportedUserId, user, createReportDTO);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @PutMapping("/users/block/{blockUserId}")
  public ResponseEntity<?> blockUser(@PathVariable("blockUserId") Integer blockUserId,
                                     @LoginUser User user)  {
    boolean isCreate = usersService.blockUser(blockUserId, user);
    if(isCreate) return new ResponseEntity<>(HttpStatus.CREATED);
    else return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @GetMapping("/users/blocked")
  public ResponseEntity<List<User>> getBlockedUsers(@LoginUser User user) {
    List<User> blockedUsers = usersService.getBlockedUsers(user);
    return ResponseEntity.ok(blockedUsers); // 차단된 사용자의 목록을 반환
  }


//  @GetMapping("/users/exchange-history")
//  public ResponseEntity<?> getExchangeHistories(@LoginUser User user) {
//    return new ResponseEntity(usersService.findMyExchangeHistory(user), HttpStatus.OK);
//  }

  @GetMapping("/users/dibs")
  public ResponseEntity<?> getDibs(@LoginUser User user) {
    return new ResponseEntity(usersService.findMyDibs(user), HttpStatus.OK);
  }
//
//    @PostMapping("/users/reivews/{userId}")
//    public ResponseEntity createReviews(@PathVariable("userId") Integer userId,
//                                     @RequestBody CreateReportDto createReportDto){
//        usersService.reportUser(reportedUserId, reporterUserId, createReportDto);
//        return new ResponseEntity(HttpStatus.CREATED);
//    }


  @PostMapping("/find-id")
  public ResponseEntity<?> findId(@RequestBody UserFindIdDTO userFindIdDTO) {
    String result = usersService.findIdByNamePhone(userFindIdDTO);
    if (result != null) {
      UserEmailDTO userEmailDTO = UserEmailDTO.builder().email(result).build();
      return new ResponseEntity<>(userEmailDTO, HttpStatus.OK);
    }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @PutMapping("/find-password")
  public ResponseEntity<?> findPassword(@RequestBody UserFindPasswordDTO userFindPasswordDTO) {
    String result = usersService.findIdByNamePhoneEmail(userFindPasswordDTO);
    if (result != null) {
      UserEmailDTO userEmailDTO = UserEmailDTO.builder().email(result).build();
      return new ResponseEntity<>(userEmailDTO, HttpStatus.OK);
    }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @GetMapping("/users/exchange-post-list")
  public ResponseEntity<?> findMyExchangePostList(@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                                  @LoginUser User user){
    Page<UserExchangePostResponseDTO> myExchangePostList = usersService.findMyExchangePostList(pageable, user);
    List<UserExchangePostResponseDTO> list = myExchangePostList.stream().toList();
    return new ResponseEntity<>(new PageResponseDto<>(list, PageInfo.of(myExchangePostList)), HttpStatus.OK);
  }

  @GetMapping("/users/community-post-list") // 12월
  public ResponseEntity<?> findMyCommunityPostList(@PageableDefault(size = 10, sort = "communityPostId", direction = Sort.Direction.DESC) Pageable pageable,
                                                  @LoginUser User user){
    Page<CommunityPostListDTO> myCommunityPostList = usersService.findMyCommunityPostList(pageable, user);
    List<CommunityPostListDTO> list = myCommunityPostList.stream().toList();
    return new ResponseEntity<>(new PageResponseDto<>(list, PageInfo.of(myCommunityPostList)), HttpStatus.OK);
  }

}
