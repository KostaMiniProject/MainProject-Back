package kosta.main.exchangeposts.service;

import kosta.main.bids.repository.BidRepository;
import kosta.main.exchangeposts.ExchangePostStubData;
import kosta.main.exchangeposts.dto.ExchangePostDTO;
import kosta.main.exchangeposts.dto.ResponseDto;
import kosta.main.exchangeposts.entity.ExchangePost;
import kosta.main.exchangeposts.repository.ExchangePostsRepository;
import kosta.main.items.ItemStubData;
import kosta.main.items.entity.Item;
import kosta.main.items.repository.ItemsRepository;
import kosta.main.users.UserStubData;
import kosta.main.users.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class ExchangePostsServiceTest {


    @InjectMocks
    ExchangePostsService exchangePostsService;

    @Mock
    private ExchangePostsRepository exchangePostRepository;
    @Mock
    private ItemsRepository itemsRepository;
    @Mock
    private BidRepository bidRepository;

    private ExchangePostStubData exchangePostStubData;
    private ItemStubData itemStubData;
    private UserStubData userStubData;

    @BeforeEach
    void init(){
        exchangePostStubData = new ExchangePostStubData();
        itemStubData = new ItemStubData();
        userStubData = new UserStubData();
    }
//    @Test
//    @Description("물물교환 게시글 생성")
    void createExchangePost() {
        //given
        User user = userStubData.getUser();
        ExchangePostDTO exchangePostDTO = exchangePostStubData.getExchangePostDTO();
        ExchangePost exchangePostBid = exchangePostStubData.getExchangePostBid();
        Item bidItem = itemStubData.getBidItem();
        given(itemsRepository.findById(Mockito.anyInt())).willReturn(Optional.of(bidItem));
        doNothing().when(itemsRepository.save(Mockito.any(Item.class)));
        given(exchangePostRepository.save(Mockito.any(ExchangePost.class))).willReturn(exchangePostBid);
        //when

        ResponseDto result = exchangePostsService.createExchangePost(user,exchangePostDTO);
        //then
        //검증 미구현

    }

    @Test
    void findAllExchangePosts() {
    }

    @Test
    void findExchangePostById() {
    }

    @Test
    void updateExchangePost() {
    }

    @Test
    void deleteExchangePost() {
    }
}