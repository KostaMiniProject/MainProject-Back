package kosta.main.exchangehistories.dto;

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
  private List<String> imageUrl;
}
