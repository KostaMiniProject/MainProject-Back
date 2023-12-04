package kosta.main.dibs.entity;
import jakarta.persistence.*;
import kosta.main.global.audit.Auditable;
import kosta.main.exchangeposts.entity.ExchangePost;
import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "dibs")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Dib extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer dibId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "exchange_post_id")
    private ExchangePost exchangePost;

}
