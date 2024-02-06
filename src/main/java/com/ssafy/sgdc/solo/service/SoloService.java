package com.ssafy.sgdc.solo.service;

import com.ssafy.sgdc.category.Category;
import com.ssafy.sgdc.category.repository.CategoryRepo;
import com.ssafy.sgdc.enums.SoloResult;
import com.ssafy.sgdc.enums.SoloStatus;
import com.ssafy.sgdc.solo.domain.Solo;
import com.ssafy.sgdc.solo.dto.SoloDto;
import com.ssafy.sgdc.solo.dto.request.CreateSoloDto;
import com.ssafy.sgdc.solo.repository.SoloRepo;
import com.ssafy.sgdc.user.User;
import com.ssafy.sgdc.user.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SoloService {

    private final UserRepo userRepo;
    private final CategoryRepo categoryRepo;
    private final SoloRepo soloRepo;

    // 솔로 모드 생성
    public void createChallenge(int userId, int categoryId) {

        User user = userRepo.findByUserId(userId);

        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }

        Category category = categoryRepo.findByCategoryId(categoryId);

        if (category == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }

        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();

        // 오늘 진행한 해당 카테고리 솔로모드 확인
        Optional<Solo> optionalSolo =
                soloRepo.findFirstByUserUserIdAndCategoryCategoryIdAndCreateAtBetween(
                        userId, categoryId, startOfDay, endOfDay
        );

        if (optionalSolo.isPresent()) {
            throw new RuntimeException("해당 카테고리는 오늘 솔로모드 도전 중이거나 완료했습니다.");
        }

        soloRepo.save(CreateSoloDto.from(
                new CreateSoloDto(
                        LocalDateTime.now(),
                        SoloStatus.PROGRESS,
                        SoloResult.INCOMPLETE,
                        category,
                        user
                )
        ));

    }

    // 솔로 모드 이미지 인증
    @Transactional
    public void soloImageAuth(int userId, int categoryId, String soloAuthImg) {

        Solo solo = soloRepo.findSoloByUserUserIdAndCategoryCategoryIdAndSoloStatus(userId, categoryId, SoloStatus.PROGRESS)
                .orElseThrow(() -> new RuntimeException("진행 중인 솔로모드가 존재하지 않습니다."));

        solo.updateImageAuth(soloAuthImg);
        solo.updateStatus(SoloStatus.DONE, SoloResult.COMPLETE);

    }

    // 진행한 리스트를 최신 순으로 30개 조회
    public List<SoloDto> getSoloList(int userId) {
        return soloRepo.findSoloList(userId, SoloResult.COMPLETE,
                PageRequest.of(0, 30)).stream().map(SoloDto::of).toList();
    }

    // 오늘 진행 중이거나 진행 완료한 리스트 조회
    public List<SoloDto> getSoloTodayList(int userId) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();

        return soloRepo.findSoloByUserUserIdAndCreateAtBetween(userId, startOfDay, endOfDay).stream()
                .map(SoloDto::of).toList();
    }
    
    //TODO: 12시가 되면 완료되지 않은 솔로 모드 상태 업데이트 해줘야함

}
