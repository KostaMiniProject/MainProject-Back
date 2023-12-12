package kosta.main.exchangehistories.controller;

import kosta.main.exchangehistories.dto.ExchangeHistoriesResponseDto;
import kosta.main.exchangehistories.service.ExchangeHistoriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/histories")
@RequiredArgsConstructor
public class ExchangeHistoriesController {

  private final ExchangeHistoriesService exchangeHistoriesService;
  // 교환 내역 생성
//  @PostMapping
//  public ResponseEntity<?> createExchangeHistory(@RequestBody ExchangeHistoryCreateDto createDto) {
//    exchangeHistoriesService.createExchangeHistory(createDto);
//    return ResponseEntity.ok().build();
//  }
//
//  // 교환 내역 조회
//  @GetMapping("/{id}")
//  public ResponseEntity<ExchangeHistoriesResponseDto> getExchangeHistory(@PathVariable Integer id) {
//    ExchangeHistoriesResponseDto responseDto = exchangeHistoriesService.getExchangeHistory(id);
//    return ResponseEntity.ok(responseDto);
//  }
//
//  // 교환 내역 삭제
//  @DeleteMapping("/{id}")
//  public ResponseEntity<?> deleteExchangeHistory(@PathVariable Integer id) {
//    exchangeHistoriesService.deleteExchangeHistory(id);
//    return ResponseEntity.ok().build();
//  }
}
