package kosta.main.exchangeposts.service;

import kosta.main.bids.repository.BidRepository;
import kosta.main.exchangeposts.ExchangePostStubData;
import kosta.main.exchangeposts.dto.ExchangePostDTO;
import kosta.main.exchangeposts.dto.ExchangePostDetailDTO;
import kosta.main.exchangeposts.dto.ExchangePostListDTO;
import kosta.main.exchangeposts.dto.ResponseDto;
import kosta.main.exchangeposts.entity.ExchangePost;
import kosta.main.exchangeposts.repository.ExchangePostsRepository;
import kosta.main.items.ItemStubData;
import kosta.main.items.entity.Item;
import kosta.main.items.repository.ItemsRepository;
import kosta.main.users.UserStubData;
import kosta.main.users.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class ExchangePostsServiceTest {


    public static final int EXCHANGE_POST_COUNT = 1;
    public static final int EXCHANGE_POST_ID = 1;
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
    @Description("물물교환 게시글 생성")
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
        //컨벤션이 지정되어 있지 않은 관계로 이후에 다시 작성 예정
    }

    @Test
    void findAllExchangePosts() {
        //given
        Page<ExchangePost> exchangePostPages = exchangePostStubData.getExchangePostPages();
        Pageable pageable = exchangePostStubData.getPageable();
        given(bidRepository.countByExchangePostAndStatusNotDeleted(Mockito.any(ExchangePost.class))).willReturn(EXCHANGE_POST_COUNT);
        given(exchangePostRepository.findAll(Mockito.any(Pageable.class))).willReturn(exchangePostPages);
        //when
        Page<ExchangePostListDTO> result = exchangePostsService.findAllExchangePosts(pageable);
        //then
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getTotalPages()).isEqualTo(1);
    }

    @Test
    void findExchangePostById() {
        ExchangePost exchangePostBid = exchangePostStubData.getExchangePostBid();
        User user = userStubData.getUser();
        //given
        given(exchangePostRepository.findById(Mockito.anyInt())).willReturn(Optional.of(exchangePostBid));

        //when

        ExchangePostDetailDTO result = exchangePostsService.findExchangePostById(EXCHANGE_POST_ID, user);

        //then

        assertThat(result.getTitle()).isEqualTo(exchangePostBid.getTitle());
        assertThat(result.getAddress()).isEqualTo(exchangePostBid.getAddress());
        assertThat(result.getItem().getDescription()).isEqualTo(exchangePostBid.getItem().getDescription());
        assertThat(result.getProfile().getRating()).isEqualTo(exchangePostBid.getUser().getRating());

    }

    @Test
    void updateExchangePost() {

        //given


        //when

        //then

    }

    @Test
    void deleteExchangePost() {
    }
}