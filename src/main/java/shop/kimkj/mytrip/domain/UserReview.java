package shop.kimkj.mytrip.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import shop.kimkj.mytrip.dto.UserReviewRequestDto;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class UserReview extends Timestamped {

    @Id
    @GeneratedValue
    @Column(name = "REVIEW_ID")
    private Long id;

    @Column
    private String title;

    @Column
    private String place;

    @Column
    private String review;

    @Column
    private String reviewImgUrl;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

//    @OneToMany(mappedBy = "userReview")
//    private List<Comment> comments;

//    @OneToMany(mappedBy = "userReview")
//    private List<UserReviewLikes> userReviewLikes;

    public UserReview(UserReviewRequestDto requestDto, User user) {
        if (requestDto.getId() != null) {
            this.id = requestDto.getId();
        }
        this.title = requestDto.getTitle();
        this.place = requestDto.getPlace();
        this.review = requestDto.getReview();
        this.user = user;
    }
}
