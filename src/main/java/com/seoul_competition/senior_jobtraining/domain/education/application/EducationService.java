package com.seoul_competition.senior_jobtraining.domain.education.application;

import com.seoul_competition.senior_jobtraining.domain.education.dao.EducationRepository;
import com.seoul_competition.senior_jobtraining.domain.education.dto.response.EducationListResponse;
import com.seoul_competition.senior_jobtraining.domain.education.dto.response.EducationResponse;
import com.seoul_competition.senior_jobtraining.domain.education.entity.Education;
import com.seoul_competition.senior_jobtraining.global.external.openApi.education.FiftyApi;
import com.seoul_competition.senior_jobtraining.global.external.openApi.education.SeniorApi;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EducationService {

  private final EducationRepository educationRepository;
  private final FiftyApi fiftyApi;
  private final SeniorApi seniorApi;
  private final static String BEFORE_WAITING_CONTENT = "접수중";
  private final static String AFTER_WAITING_CONTENT = "수강신청중";

  public EducationListResponse findAll() {
    return new EducationListResponse(entityToResponse());
  }

  private List<EducationResponse> entityToResponse() {
    return educationRepository.findAll().stream()
        .map(EducationResponse::new)
        .collect(Collectors.toList());
  }

  public EducationResponse findById(Long id) {
    return new EducationResponse(educationRepository.findById(id).get());
  }

  @Transactional
  public void saveAll() {
    saveFifty(fiftyApi.getInfoArr());
    saveSenior(seniorApi.getInfoArr());
  }

  private void saveFifty(JSONArray infoArr) {
    for (int i = 0; i < infoArr.size(); i++) {
      JSONObject jsonObject = (JSONObject) infoArr.get(i);
      Education education = Education.builder()
          .name((String) jsonObject.get("LCT_NM"))
          .state((String) jsonObject.get("LCT_STAT"))
          .url((String) jsonObject.get("CR_URL"))
          .price(Integer.parseInt((String) jsonObject.get("LCT_COST")))
          .capacity(Integer.parseInt((String) jsonObject.get("CR_PPL_STAT")))
          .registerStart((String) jsonObject.get("REG_STDE"))
          .registerEnd((String) jsonObject.get("REG_EDDE"))
          .educationStart((String) jsonObject.get("CR_STDE"))
          .educationEnd((String) jsonObject.get("CR_EDDE"))
          .build();
      educationRepository.save(education);
    }
  }

  private void saveSenior(JSONArray infoArr) {
    for (int i = 0; i < infoArr.size(); i++) {
      JSONObject jsonObject = (JSONObject) infoArr.get(i);

      String applyState = getCommonApplyState(jsonObject);

      Education education = Education.builder()
          .name((String) jsonObject.get("SUBJECT"))
          .state(applyState)
          .url((String) jsonObject.get("VIEWDETAIL"))
          .price(Integer.parseInt((String) jsonObject.get("REGISTCOST")))
          .capacity(Integer.parseInt((String) jsonObject.get("REGISTPEOPLE")))
          .registerStart(((String) jsonObject.get("APPLICATIONSTARTDATE")).replaceAll("-", "."))
          .registerEnd(((String) jsonObject.get("APPLICATIONENDDATE")).replaceAll("-", "."))
          .educationStart(((String) jsonObject.get("STARTDATE")).replaceAll("-", "."))
          .educationEnd(((String) jsonObject.get("ENDDATE")).replaceAll("-", "."))
          .build();
      educationRepository.save(education);
    }
  }

  private static String getCommonApplyState(JSONObject jsonObject) {
    String applyState = (String) jsonObject.get("APPLY_STATE");
    if (applyState.equals(BEFORE_WAITING_CONTENT)) {
      applyState = AFTER_WAITING_CONTENT;
    }
    return applyState;
  }
}