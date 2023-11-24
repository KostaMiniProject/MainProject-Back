package kosta.main.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuerydslConfig {

    /*
     * EntityManager를 빈으로 주입할 때 사용하는 어노테이션
     * 빈으로 주입받을 때 EntityManager의 경우 @Autowired가 아니라 해당 어노테이션으로 주입
     */
    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory(){
        return new JPAQueryFactory(entityManager);
    }

}
