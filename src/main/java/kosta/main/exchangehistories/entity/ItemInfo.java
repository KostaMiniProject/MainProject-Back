package kosta.main.exchangehistories.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import kosta.main.bids.entity.Bid;
import kosta.main.categories.entity.Category;
import kosta.main.exchangeposts.entity.ExchangePost;
import kosta.main.items.entity.Item;
import kosta.main.users.entity.User;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "item_info")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class ItemInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer itemInfoId;

    private Integer itemId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "bid_id")
    private Bid bid;

    @JsonIgnore//DTO생기면 지울것
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToOne(cascade = CascadeType.PERSIST, mappedBy = "itemInfo")
    private ExchangePost exchangePost;

    @OneToOne(cascade = CascadeType.PERSIST, mappedBy = "itemInfo")
    private ExchangeHistory exchangeHistory;
    @ElementCollection
    @Builder.Default
    @CollectionTable(name = "item_info_images", joinColumns = @JoinColumn(name = "item_info_id"))
    @Column(name = "item_info_image")
    private List<String> images = new ArrayList<>(); // 아이템 정보의 이미지 리스트


    public static ItemInfo from(Item item){
        return ItemInfo.builder()
                .itemId(item.getItemId())
                .user(item.getUser())
                .bid(item.getBid())
                .category(item.getCategory())
                .title(item.getTitle())
                .description(item.getDescription())
                .images(item.getImages())
                .build();
    }
}
