package kosta.main.categories.entity;

import jakarta.persistence.*;
import kosta.main.items.entity.Items;

import java.util.List;

@Entity
@Table(name = "categories")
public class Categories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 20, nullable = false)
    private String categoryName;

    @OneToMany(mappedBy = "category")
    private List<Items> items;

    // 게터와 세터
    // 생략...
}
