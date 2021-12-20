package shop.kimkj.mytrip.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shop.kimkj.mytrip.domain.Comment;
import shop.kimkj.mytrip.domain.User;
import shop.kimkj.mytrip.domain.UserReview;
import shop.kimkj.mytrip.dto.CommentDto;
import shop.kimkj.mytrip.dto.UserDto;
import shop.kimkj.mytrip.dto.UserReviewDto;
import shop.kimkj.mytrip.repository.CommentRepository;
import shop.kimkj.mytrip.repository.UserReviewRepository;
import shop.kimkj.mytrip.security.UserDetailsImpl;

import java.util.Optional;

import static org.springframework.test.util.AssertionErrors.assertEquals;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
@Transactional
public class CommentServiceTest {
    @Autowired
    CommentService commentService;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    UserReviewRepository userReviewRepository;
    @Autowired
    UserService userService;

    User user;
    UserDetailsImpl nowUser;
    UserReview userReview;

    @BeforeEach
    void beforeEach() {
        UserDto userDto = new UserDto("signup", "test1234", "test1234");
        this.user = userService.registerUser(userDto);
        this.nowUser = new UserDetailsImpl(user);

        UserReviewDto userReviewDto = new UserReviewDto("title", "place", "review");
        UserReview saveUserReview = new UserReview(userReviewDto, user);
        this.userReview = userReviewRepository.save(saveUserReview);
    }

    @Test
    @DisplayName("댓글 생성 성공")
    void postComment() {
        // given
        CommentDto commentDto = new CommentDto("comment");
        Comment comment = commentService.postComment(userReview.getId(), commentDto, nowUser);

        // when
        Comment commentTest = commentRepository.findById(comment.getId()).orElseThrow(
                () -> new NullPointerException("Comment 가 정상적으로 생성되지 않았습니다.")
        );

        // then
        assertEquals("Comment 의 Id 값이 일치하는지 확인.", comment.getId(), commentTest.getId());
    }

    @Test
    @DisplayName("댓글 수정 성공")
    void updateComment() {
        // given
        CommentDto commentDto = new CommentDto("comment");
        Comment comment = commentService.postComment(userReview.getId(), commentDto, nowUser);

        CommentDto commentDtoEdit = new CommentDto("comment-edit");

        // when
        Comment commentTest = commentService.updateComment(userReview.getId(), comment.getId(), commentDtoEdit);

        // then
        assertEquals("Comment Id 값이 일치하는지 확인.", comment.getId(), commentTest.getId());
        assertEquals("Comment 내용이 업데이트 되었는지 확인", "comment-edit", comment.getComment());
    }

    @Test
    @DisplayName("댓글 삭제 성공")
    void deleteComment() {
        // given
        CommentDto commentDto = new CommentDto("comment");
        Comment comment = commentService.postComment(userReview.getId(), commentDto, nowUser);

        //when
        commentService.deleteComment(userReview.getId(), comment.getId(), nowUser);

        // then
        Optional<Comment> commentTest = commentRepository.findById(comment.getId());
        if (commentTest.isPresent())
            throw new IllegalArgumentException("Comment 가 정상적으로 삭제되지 않았습니다.");
        else
            assertEquals("Comment 가 정상적으로 삭제되었습니다.", Optional.empty(), commentTest);
    }
}
