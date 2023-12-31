package kosta.main.exchangehistories.controller;

import kosta.main.exchangehistories.dto.ExchangeHistoriesResponseDTO;
import kosta.main.exchangehistories.service.ExchangeHistoriesService;
import kosta.main.global.dto.PageInfo;
import kosta.main.global.dto.PageResponseDto;
import kosta.main.users.entity.LoginUser;
import kosta.main.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/histories")
@RequiredArgsConstructor
public class ExchangeHistoriesController {

  private final ExchangeHistoriesService exchangeHistoriesService;

  // 교환 내역 생성 = 거래완료후에 실행될 로직 (굳이 컨트롤러에 없어도 될거 같긴합니다.)
//  public ResponseEntity<?> createExchangeHistory(@RequestBody ExchangeHistoryCreateDTO exchangeHistoryCreateDTO, @LoginUser User user) {
//    return new ResponseEntity<>(exchangeHistoriesService.createExchangeHistory(exchangeHistoryCreateDTO, user), HttpStatus.CREATED);
//  }

  // 교환 내역 조회
  @GetMapping
  public ResponseEntity<?> getExchangeHistories(
      @LoginUser User user,
      @PageableDefault(sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
    Page<ExchangeHistoriesResponseDTO> exchangeHistories = exchangeHistoriesService.getExchangeHistories(user, pageable);
    List<ExchangeHistoriesResponseDTO> list = exchangeHistories.stream().toList();
    return new ResponseEntity<>(new PageResponseDto<>(list, PageInfo.of(exchangeHistories)), HttpStatus.OK);
  }


}
