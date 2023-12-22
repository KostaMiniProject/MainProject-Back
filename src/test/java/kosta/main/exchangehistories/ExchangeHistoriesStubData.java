package kosta.main.exchangehistories;

import jakarta.persistence.EntityNotFoundException;
import kosta.main.bids.entity.Bid;
import kosta.main.exchangehistories.dto.ExchangeHistoriesResponseDTO;
import kosta.main.exchangehistories.dto.ItemHistoryDTO;
import kosta.main.exchangeposts.ExchangePostStubData;
import kosta.main.exchangeposts.entity.ExchangePost;
import kosta.main.items.entity.Item;
import kosta.main.users.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ExchangeHistoriesStubData {



    // 아이템 정보를 ItemHistoryDTO 리스트로 변환하는 로직
    private List<ItemHistoryDTO> createItemHistoryList(Bid selectedBid, Item postItem) {
        List<ItemHistoryDTO> exchangedItems = new ArrayList<>();
        // 게시글 아이템 정보 추가
        exchangedItems.add(new ItemHistoryDTO(
                postItem.getTitle(),
                postItem.getDescription(),
                postItem.getImages().get(0)
        ));
        // 입찰 아이템 정보 추가
        for (Item item : selectedBid.getItems()) {
            exchangedItems.add(new ItemHistoryDTO(
                    item.getTitle(),
                    item.getDescription(),
                    item.getImages().get(0)
            ));
        }
        return exchangedItems;
    }
}


