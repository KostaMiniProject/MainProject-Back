package kosta.main.users.controller;

import kosta.main.users.dto.UserCreateDto;
import kosta.main.users.dto.UserUpdateDto;
import kosta.main.users.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
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

    @PutMapping("/users/{userId}")
    public ResponseEntity updateMyInfo(@PathVariable("userId") Integer userId,
                                       @RequestBody UserUpdateDto userUpdateDto){
        return ResponseEntity.ok(usersService.updateUser(userId, userUpdateDto));
    }

    @PutMapping("/users/withdrawal/{userId}")
    public ResponseEntity withdrawal(@PathVariable("userId") Integer userId){
        usersService.withdrawalUser(userId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }



}
