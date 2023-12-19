package kosta.main.users.dto.response;

import kosta.main.dibs.entity.Dib;
import kosta.main.exchangehistories.entity.ExchangeHistory;
import kosta.main.exchangeposts.entity.ExchangePost;
import kosta.main.items.entity.Item;
import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserAllProfileResponseDTO {

    private Integer userId;
    private String email;
    private String name;

    private String address;

    private Double rating;

    private String profileImage;

    private List<MyItemInfo> myItemInfo;
    private List<MyExchangeHistoryInfo> myExchangeHistoryInfo;
    private List<MyDibbedExchangeHistoryInfo> myDibbedExhcangePostInfo;


    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MyItemInfo{
        private Integer itemId;
        private String itemTitle;
        public static MyItemInfo from(Item item){
            return new MyItemInfo(item.getItemId(), item.getTitle());
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class MyExchangeHistoryInfo{
        private Integer exchangeHistoryId;
        private String exchangeHistoryTitle;

        public static MyExchangeHistoryInfo from(ExchangeHistory exchangeHistory){
            return new MyExchangeHistoryInfo(exchangeHistory.getExchangeHistoryId(), exchangeHistory.getExchangePost().getTitle());
        }
    }
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class MyDibbedExchangeHistoryInfo{
        private Integer dibbedExchangePostId;
        private String dibbedExchangePostTitle;
        private Integer dibId;

        public static MyDibbedExchangeHistoryInfo from(Dib dib){
            ExchangePost exchangePost = dib.getExchangePost();
            return new MyDibbedExchangeHistoryInfo(exchangePost.getExchangePostId(), exchangePost.getTitle(),dib.getDibId());
        }
    }

    public static UserAllProfileResponseDTO from(
            User user
            ,List<MyItemInfo> myItemInfo
            ,List<MyExchangeHistoryInfo> myExchangeHistoryInfo
            ,List<MyDibbedExchangeHistoryInfo> myDibbedExhcangePostInfo){
        List<ExchangeHistory> initiatedExchanges = user.getInitiatedExchanges();
        List<ExchangeHistory> participatedExchanges = user.getParticipatedExchanges();
        initiatedExchanges.addAll(participatedExchanges);
        initiatedExchanges.sort(new Comparator<ExchangeHistory>() {
            @Override
            public int compare(ExchangeHistory o1, ExchangeHistory o2) {
                if (o1.getCreatedAt().isBefore(o2.getCreatedAt())) {
                    return 1;
                } else if (o1.getCreatedAt().isAfter(o2.getCreatedAt())) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        return new UserAllProfileResponseDTO(
                user.getUserId(),
                user.getEmail(),
                user.getName(),
                user.getAddress(),
                user.getRating(),
                user.getProfileImage(),
                myItemInfo,
                myExchangeHistoryInfo,
                myDibbedExhcangePostInfo);
    }
    public static UserAllProfileResponseDTO from(User user){
        List<ExchangeHistory> initiatedExchanges = user.getInitiatedExchanges();
        List<ExchangeHistory> participatedExchanges = user.getParticipatedExchanges();
        initiatedExchanges.addAll(participatedExchanges);
        initiatedExchanges.sort(new Comparator<ExchangeHistory>() {
            @Override
            public int compare(ExchangeHistory o1, ExchangeHistory o2) {
                if (o1.getCreatedAt().isBefore(o2.getCreatedAt())) {
                    return 1;
                } else if (o1.getCreatedAt().isAfter(o2.getCreatedAt())) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        return new UserAllProfileResponseDTO(
                user.getUserId(),
                user.getEmail(),
                user.getName(),
                user.getAddress(),
                user.getRating(),
                user.getProfileImage(),
                user.getItems().stream().map(MyItemInfo::from).toList(),
                initiatedExchanges.stream().map(MyExchangeHistoryInfo::from).collect(Collectors.toList()),
                user.getDibs().stream().map(MyDibbedExchangeHistoryInfo::from).collect(Collectors.toList()));
    }

}
