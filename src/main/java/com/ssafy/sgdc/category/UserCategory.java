package com.ssafy.sgdc.category;

import com.ssafy.sgdc.category.Category;
import com.ssafy.sgdc.enums.CategoryStatus;
import com.ssafy.sgdc.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_category")
public class UserCategory {

    @Id
    @Column(name = "user_category_id")
    private int userCategoryId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "category_win_cnt")
    private int categoryWinCnt;

    @Column(name = "category_fail_cnt")
    private int categoryFailCnt;

    @Column(name = "category_status")
    @Enumerated(EnumType.STRING)
    private CategoryStatus categoryStatus;

}
