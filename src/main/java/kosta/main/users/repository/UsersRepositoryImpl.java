package kosta.main.users.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kosta.main.users.dto.UsersResponseDto;
import kosta.main.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static kosta.main.users.entity.QUser.user;


@Repository
@RequiredArgsConstructor
public class UsersRepositoryImpl implements UsersRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public Optional<UsersResponseDto> findUserByUserId(Integer userId){
        UsersResponseDto result = queryFactory.select(Projections.constructor(UsersResponseDto.class,
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
}
