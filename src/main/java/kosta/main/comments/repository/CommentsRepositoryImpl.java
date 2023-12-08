package kosta.main.comments.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kosta.main.comments.dto.CommentDTO;
import kosta.main.comments.entity.Comment;
import kosta.main.users.dto.UsersResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kosta.main.comments.entity.QComment.comment;

@Repository
@RequiredArgsConstructor
public class CommentsRepositoryImpl implements CommentsRepositoryCustom{

    private final JPAQueryFactory queryFactory;



    @Override
    public List<Comment> findComments(Integer postId){
        return queryFactory.selectFrom(comment)
                .leftJoin(comment.parent)
                .fetchJoin()
                .where(comment.communityPost.communityPostId.eq(postId))
                .orderBy(
                        comment.parent.commentId.asc().nullsFirst(),
                        comment.createdAt.asc()
                ).fetch();
    }
//
//    @Override
//    public List<CommentDTO> findComments(Integer postId){
//        return queryFactory.select(Projections.constructor(CommentDTO.class,
//                        comment.communityPost.communityPostId,
//                        comment.commentId,
//                        comment.content))
//                .from(comment)
//                .leftJoin(comment.parent)
//                .fetchJoin()
//                .where(comment.communityPost.communityPostId.eq(postId))
//                .orderBy(
//                        comment.parent.commentId.asc().nullsFirst(),
//                        comment.createdAt.asc()
//                ).fetch();
//    }

}
