package kosta.main.users.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kosta.main.users.UserStubData;
import kosta.main.users.dto.UsersResponseDto;
import kosta.main.users.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UsersRepositoryImplTest {

    public static final int USER_ID = 1;
    @PersistenceContext
    EntityManager em;

    @Autowired
    private UsersRepositoryImpl usersRepository;

    @Autowired
    private UsersRepository jpaUsersRepository;

    private UserStubData userStubData;

    private User user;
    @BeforeEach
    void init() {
        userStubData = new UserStubData();
        //setUp
        user = userStubData.getUser();
        jpaUsersRepository.save(user);
    }

    @Test
    void 유저_아이디로_유저_응답_반환() {
        Integer userId = USER_ID;
        UsersResponseDto result = usersRepository.findUserByUserId(userId).get();
        assertThat(user.getAddress()).isEqualTo(result.getAddress());

    }
}