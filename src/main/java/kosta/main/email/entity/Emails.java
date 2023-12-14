package kosta.main.email.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import kosta.main.global.audit.Auditable;
import lombok.*;


@Entity
@Table(name = "emails")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Emails extends Auditable {
  @Id
  @NotEmpty(message = "이메일을 입력해 주세요")
  private String email;

  @Column(length = 6)
  private Integer authNum;
}
