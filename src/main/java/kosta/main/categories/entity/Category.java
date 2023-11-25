package kosta.main.categories.entity;

import jakarta.persistence.*;
import kosta.main.global.audit.Auditable;
import kosta.main.items.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "categories")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Category extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Integer categoryId;

    @Column(length = 20, nullable = false)
    private String categoryName;

    @OneToMany(mappedBy = "category", cascade = {CascadeType.PERSIST, CascadeType.REMOVE},fetch = FetchType.LAZY)
    private List<Item> items;

    // 게터와 세터
    // 생략...
}
