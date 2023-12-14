package kosta.main.users.controller;

import jakarta.validation.Valid;
import kosta.main.users.dto.UserCreateDTO;
import kosta.main.users.dto.UserEmailDTO;
import kosta.main.users.dto.UserFindIdDTO;
import kosta.main.users.dto.UserUpdateDTO;
import kosta.main.reports.dto.CreateReportDTO;
import kosta.main.users.entity.LoginUser;
import kosta.main.users.entity.User;
import kosta.main.users.service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


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
    return new ResponseEntity(HttpStatus.CREATED);
  }

  @PutMapping("/users/block/{blockUserId}/{userId}")
  public ResponseEntity<?> blockUser(@PathVariable("blockUserId") Integer blockUserId,
                                     @PathVariable("userId") Integer userId) {
    usersService.blockUser(blockUserId, userId);
    return new ResponseEntity(HttpStatus.CREATED);
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

}
