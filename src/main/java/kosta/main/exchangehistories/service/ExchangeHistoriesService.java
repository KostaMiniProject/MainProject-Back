package kosta.main.exchangehistories.service;

import kosta.main.exchangehistories.dto.ExchangeHistoriesResponseDto;
import kosta.main.exchangehistories.entity.ExchangeHistory;
import kosta.main.exchangehistories.repository.ExchangeHistoriesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ExchangeHistoriesService {
  private final ExchangeHistoriesRepository exchangeHistoriesRepository;

//  @Transactional
//  public void createExchangeHistory(ExchangeHistoryCreateDto createDto) {
//    // ExchangeHistory 엔터티 생성 및 저장 로직
//    // 필요한 정보를 createDto에서 추출하여 ExchangeHistory 엔터티 생성
//    ExchangeHistory exchangeHistory = new ExchangeHistory();
//    // ... 엔터티 필드 설정
//    exchangeHistoryRepository.save(exchangeHistory);
//  }
//
//  @Transactional(readOnly = true)
//  public ExchangeHistoriesResponseDto getExchangeHistory(Integer id) {
//    ExchangeHistory exchangeHistory = exchangeHistoriesRepository.findById(id)
//        .orElseThrow(() -> new RuntimeException("Exchange history not found"));
//    return createExchangeHistoriesResponseDto(exchangeHistory);
//  }
//
//  @Transactional
//  public void deleteExchangeHistory(Integer id) {
//    exchangeHistoriesRepository.deleteById(id);
//  }
//
//  private ExchangeHistoriesResponseDto createExchangeHistoriesResponseDto(ExchangeHistory exchangeHistory) {
//    // ExchangeHistory 엔터티를 사용하여 ExchangeHistoriesResponseDto 생성
//    // ... DTO 생성 로직
//    return new ExchangeHistoriesResponseDto();
//  }
}
