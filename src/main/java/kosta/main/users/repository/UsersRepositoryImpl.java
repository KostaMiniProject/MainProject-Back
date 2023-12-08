package kosta.main.users.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kosta.main.users.dto.UsersResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static kosta.main.users.entity.QUser.user;


@Repository
@RequiredArgsConstructor
public class UsersRepositoryImpl implements UsersRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public Optional<UsersResponseDTO> findUserByUserId(Integer userId){
        UsersResponseDTO result = queryFactory.select(Projections.constructor(UsersResponseDTO.class,
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
