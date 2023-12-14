package kosta.main.exchangehistories;

import jakarta.persistence.EntityNotFoundException;
import kosta.main.bids.entity.Bid;
import kosta.main.exchangehistories.dto.ExchangeHistoriesResponseDTO;
import kosta.main.exchangehistories.dto.ExchangeHistoryCreateResponseDTO;
import kosta.main.exchangehistories.dto.ItemHistoryDTO;
import kosta.main.exchangehistories.entity.ExchangeHistory;
import kosta.main.exchangeposts.ExchangePostStubData;
import kosta.main.exchangeposts.dto.ExchangePostListDTO;
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

    public ExchangeHistoriesResponseDTO getExchangeHistoriesResponseDTO(){
        ExchangePostStubData exchangePostStubData = new ExchangePostStubData();

        ExchangePost exchangePostBid = exchangePostStubData.getExchangePostBid();
        Bid bid = exchangePostBid.getBids().get(0);

        User user = exchangePostBid.getUser();
        Item item = exchangePostBid.getItem();

        List<ItemHistoryDTO> exchangedItems
                = createItemHistoryList(bid, item);

        ExchangeHistory exchangeHistory = ExchangeHistory.builder()
                .exchangeInitiator(user)
                .exchangePartner(user)
                .exchangePost(exchangePostBid)
                .item(item)
                .exchangedItems(exchangedItems) // 추가된 필드
                .build();
        return makeExchangeHistoriesResponseDTO(exchangeHistory);
    }

    public List<ExchangeHistoriesResponseDTO> getExchangeHistoriesResponseDTOs(){
        ExchangeHistoriesResponseDTO exchangeHistoriesResponseDTO = getExchangeHistoriesResponseDTO();
        ArrayList<ExchangeHistoriesResponseDTO> exchangeHistoriesResponseDTOS = new ArrayList<>();
        exchangeHistoriesResponseDTOS.add(exchangeHistoriesResponseDTO);
        return exchangeHistoriesResponseDTOS;
    }

    private ExchangeHistoriesResponseDTO makeExchangeHistoriesResponseDTO(ExchangeHistory history){
        ExchangePost exchangePost = history.getExchangePost();
        Bid selectedBid = history.getExchangePost().getBids().stream()
//                .filter(bid -> bid.getStatus() == Bid.BidStatus.SELECTED) 테스트라서 제외
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Selected bid not found"));
        List<ItemHistoryDTO> exchangeHistory = history.getExchangedItems();

        List<ExchangeHistoriesResponseDTO.ItemDetailsDTO> myItems = Collections.singletonList(
                new ExchangeHistoriesResponseDTO.ItemDetailsDTO(
                        exchangePost.getItem().getItemId(),
                        exchangePost.getItem().getTitle(),
                        exchangePost.getItem().getDescription(),
                        exchangePost.getItem().getImages().isEmpty() ? null : exchangePost.getItem().getImages().get(0)
                )
        );

        List<ExchangeHistoriesResponseDTO.ItemHistoryDTO> otherUserItems = exchangeHistory.stream()
                .map(item -> new ExchangeHistoriesResponseDTO.ItemHistoryDTO(
                        item.getTitle(),
                        item.getDescription(),
                        item.getImageUrl().isEmpty() ? null : Collections.singletonList(item.getImageUrl())
                ))
                .collect(Collectors.toList());

        return new ExchangeHistoriesResponseDTO(
                history.getCreatedAt(),
                exchangePost.getUser().getName(),
                exchangePost.getUser().getAddress(),
                exchangePost.getUser().getProfileImage(),
                myItems,
                selectedBid.getUser().getName(),
                selectedBid.getUser().getAddress(),
                selectedBid.getUser().getProfileImage(),
                otherUserItems
        );
    }

    public Page<ExchangeHistoriesResponseDTO> getExchangeHistoriesResponsePageDTOs() {
        List<ExchangeHistoriesResponseDTO> exchangeHistoriesResponseDTOs = getExchangeHistoriesResponseDTOs();
        // 요청으로 들어온 page와 한 page당 원하는 데이터의 갯수
        PageRequest pageRequest = PageRequest.of(0, 10);
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), exchangeHistoriesResponseDTOs.size());
        return new PageImpl<>(exchangeHistoriesResponseDTOs.subList(start, end), pageRequest, exchangeHistoriesResponseDTOs.size());
    }

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


