package kosta.main.users.controller;

import kosta.main.users.dto.UserCreateDto;
import kosta.main.users.dto.UserUpdateDto;
import kosta.main.reports.dto.CreateReportDto;
import kosta.main.users.entity.LoginUser;
import kosta.main.users.entity.User;
import kosta.main.users.service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class UsersController {

    private final UsersService usersService;

    //내 정보 조회와 유저 프로필 조회를 나눠 둘 필요가있을지 잘 모르겠음
    @GetMapping("/users/{userId}")
    public ResponseEntity findMyProfile(@PathVariable("userId") Integer userId){
        return ResponseEntity.ok(usersService.findMyProfile(userId));
    }

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody UserCreateDto userCreateDto){
        return new ResponseEntity(usersService.createUser(userCreateDto), HttpStatus.CREATED);
    }

    @PutMapping("/users")
    public ResponseEntity updateMyInfo(@LoginUser User user,
                                       @RequestPart("userUpdateDto") UserUpdateDto userUpdateDto,
                                       @RequestPart(value = "file") MultipartFile file) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("==============================={}=============================", user);
        return ResponseEntity.ok(usersService.updateUser(1, userUpdateDto,file));
    }

    @PutMapping("/users/withdrawal/{userId}")
    public ResponseEntity withdrawal(@PathVariable("userId") Integer userId){
        usersService.withdrawalUser(userId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    @PostMapping("/users/report/{reportedUserId}/{reporterUserId}")
    public ResponseEntity reportUser(@PathVariable("reportedUserId") Integer reportedUserId,
                                     @PathVariable("reporterUserId") Integer reporterUserId,
                                     @RequestBody CreateReportDto createReportDto){
        usersService.reportUser(reportedUserId, reporterUserId, createReportDto);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PutMapping("/users/block/{blockUserId}/{userId}")
    public ResponseEntity blockUser(@PathVariable("blockUserId") Integer blockUserId,
                                    @PathVariable("userId") Integer userId){
        usersService.blockUser(blockUserId,userId);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/users/exchange-history/{userId}")
    public ResponseEntity getExchangeHistories(@PathVariable("userId") Integer userId){
        return new ResponseEntity(usersService.findMyExchangeHistory(userId), HttpStatus.OK);
    }

    @GetMapping("/users/dibs/{userId}")
    public ResponseEntity getDibs(@PathVariable("userId") Integer userId){
        return new ResponseEntity(usersService.findMyDibs(userId),HttpStatus.OK);
    }
//
//    @PostMapping("/users/reivews/{userId}")
//    public ResponseEntity createReviews(@PathVariable("userId") Integer userId,
//                                     @RequestBody CreateReportDto createReportDto){
//        usersService.reportUser(reportedUserId, reporterUserId, createReportDto);
//        return new ResponseEntity(HttpStatus.CREATED);
//    }



}
