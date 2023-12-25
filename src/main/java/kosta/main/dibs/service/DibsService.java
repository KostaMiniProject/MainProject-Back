package kosta.main.dibs.service;

import kosta.main.dibs.dto.DibbedExchangePostDTO;
import kosta.main.dibs.entity.Dib;
import kosta.main.dibs.repository.DibsRepository;
import kosta.main.exchangeposts.entity.ExchangePost;
import kosta.main.exchangeposts.repository.ExchangePostsRepository;
import kosta.main.global.error.exception.BusinessException;
import kosta.main.global.error.exception.CommonErrorCode;
import kosta.main.users.entity.User;
import kosta.main.users.repository.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DibsService {

    private final DibsRepository dibsRepository;
    private final UsersRepository usersRepository;
    private final ExchangePostsRepository exchangePostsRepository;

    @Transactional
    public void toggleDib(Integer userId, Integer exchangePostId) {
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(CommonErrorCode.USER_NOT_FOUND));
        ExchangePost exchangePost = exchangePostsRepository.findById(exchangePostId)
                .orElseThrow(() -> new BusinessException(CommonErrorCode.EXCHANGE_POST_NOT_FOUND));

        // dib 테이블에서 해당 게시글에 dib을 누른 userid를 조회한다. 없다면 null을 저장한다.
        Dib existingDib = dibsRepository.findByUserUserIdAndExchangePostExchangePostId(userId, exchangePostId)
                .orElse(null);

        if (existingDib != null) { // null이 아니라면 이미 눌렀다는 의미임으로 기존의 dib를 삭제한다
            dibsRepository.delete(existingDib);
        } else { // 그렇지 않으면 dib를 생성한다.
            Dib newDib = Dib.builder()
                    .user(user)
                    .exchangePost(exchangePost)
                    .build();
            dibsRepository.save(newDib);
        }
    }

    //찜 목록 조회 기능
    @Transactional(readOnly = true)
    public List<DibbedExchangePostDTO> getUserDibs(User user) {

        Optional<User> userByEmail = usersRepository.findUserByEmail(user.getEmail());
         user = userByEmail.orElseThrow(() -> new BusinessException(CommonErrorCode.USER_NOT_FOUND));

        return user.getDibs().stream()
            .map(Dib::getExchangePost)
            .map(exchangePost -> DibbedExchangePostDTO.builder()
                //.exchangePostId(exchangePost.getExchangePostId())
                .title(exchangePost.getTitle())
                .representativeImageUrl(exchangePost.getItem().getImages().isEmpty() ? null : exchangePost.getItem().getImages().get(0))
                .createdAt(exchangePost.getCreatedAt())
                .build())
            .collect(Collectors.toList());
    }
}
