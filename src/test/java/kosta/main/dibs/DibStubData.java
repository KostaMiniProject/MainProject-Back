package kosta.main.dibs;

import kosta.main.dibs.dto.DibbedExchangePostDTO;
import kosta.main.dibs.entity.Dib;
import kosta.main.exchangeposts.ExchangePostStubData;
import kosta.main.exchangeposts.entity.ExchangePost;
import kosta.main.users.UserStubData;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DibStubData {

    private ExchangePostStubData exchangePostStubData;

    public Dib getDib(){
        exchangePostStubData = new ExchangePostStubData();
        ExchangePost exchangePostBid = exchangePostStubData.getExchangePostBid();
        Dib newDib = Dib.builder()
                .user(exchangePostBid.getUser())
                .exchangePost(exchangePostBid)
                .build();
        return newDib;
    }
    public Dib getAnotherDib(){
        exchangePostStubData = new ExchangePostStubData();
        ExchangePost exchangePostBid = exchangePostStubData.getAnotherExchangePostNoBid();
        Dib newDib = Dib.builder()
                .user(exchangePostBid.getUser())
                .exchangePost(exchangePostBid)
                .build();
        return newDib;
    }

    public DibbedExchangePostDTO getDibbedExchangePost(){
        Dib dib = getDib();
        ExchangePost exchangePost = dib.getExchangePost();
        return DibbedExchangePostDTO.builder()
                .exchangePostId(exchangePost.getExchangePostId())
                .title(exchangePost.getTitle())
                .representativeImageUrl(exchangePost.getItem().getImages().get(0))
                .createdAt(LocalDateTime.now())
                .build();
    }
    public DibbedExchangePostDTO getAnotherDibbedExchangePost(){
        Dib dib = getAnotherDib();
        ExchangePost exchangePost = dib.getExchangePost();
        return DibbedExchangePostDTO.builder()
                .exchangePostId(exchangePost.getExchangePostId())
                .title(exchangePost.getTitle())
                .representativeImageUrl(exchangePost.getItem().getImages().get(0))
                .createdAt(LocalDateTime.now())
                .build();
    }
    public List<DibbedExchangePostDTO> getDibbedExchangePostDTOs(){
        DibbedExchangePostDTO dibbedExchangePost = getDibbedExchangePost();
        DibbedExchangePostDTO anotherDibbedExchangePost = getAnotherDibbedExchangePost();
        List<DibbedExchangePostDTO> dibbedExchangePostDTOs = new ArrayList<>();
        dibbedExchangePostDTOs.add(dibbedExchangePost);
        dibbedExchangePostDTOs.add(anotherDibbedExchangePost);
        return dibbedExchangePostDTOs;
    }

}
