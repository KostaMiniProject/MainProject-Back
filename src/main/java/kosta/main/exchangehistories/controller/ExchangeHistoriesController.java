package kosta.main.exchangehistories.controller;

import kosta.main.users.entity.LoginUser;
import kosta.main.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/histories")
@RequiredArgsConstructor
public class ExchangeHistoriesController {

//  @GetMapping("/{exchangeHistoryId}")
//  public ResponseEntity<?> getAllExchangePostsHistories(@PathVariable("exchangeHistoryId") Integer exexchangeHistoryId,
//                                                        @LoginUser User user){
//    return new ResponseEntity(new )
//  }
}
