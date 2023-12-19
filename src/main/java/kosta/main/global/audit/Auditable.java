package kosta.main.global.audit;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable implements Comparable<Auditable> {
    @CreatedDate
    @Column(name = "created_at",updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Override
    public int compareTo(Auditable o) {
        if (this.createdAt.isBefore(o.getCreatedAt())) {
            return 1;
        } else if (this.createdAt.isAfter(o.getCreatedAt())) {
            return -1;
        } else {
            return 0;
        }
    }
}
