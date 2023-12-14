package kosta.main.exchangehistories.dto;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ItemHistoryDTO {
  private String title;
  private String description;

  @Column(length = 1000)  // 길이를 1000으로 설정
  private String imageUrl;
}
