package kosta.main.users.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kosta.main.users.dto.response.UsersResponseDTO;
import kosta.main.users.entity.QUser;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static kosta.main.users.entity.QUser.user;


@Repository
@RequiredArgsConstructor
public class UsersRepositoryImpl implements UsersRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public Optional<UsersResponseDTO> findUserByUserId(Integer userId){
        UsersResponseDTO result =
                queryFactory.select(Projections.constructor(UsersResponseDTO.class,
                user.userId,
                user.rating,
                user.email,
                        user.name,
                        user.address,
                        user.phone,
                        user.profileImage,
                        user.userStatus
                ))
                .from(user)
                .where(user.userId.eq(userId))
                .fetchOne();
        return Optional.of(result);
    }
    @Override
    public Optional<UsersResponseDTO> findUserByUserName(String name){
        UsersResponseDTO result = queryFactory.select(Projections.constructor(UsersResponseDTO.class,
                user.userId,
                user.rating,
                user.email,
                        user.name,
                        user.address,
                        user.phone,
                        user.profileImage,
                        user.userStatus
                ))
                .from(user)
                .where(user.name.eq(name))
                .fetchOne();
        return Optional.of(result);
    }

//    @Transactional
//    @Scheduled(cron = "0 0 0 * * *") // 매일 0시 0분 0초에 실행
//    public void userInfoDelete(){
//        QUser user = QUser.user;
//        queryFactory.delete(user)
//                .where(user.updatedAt,)
//    }


}
