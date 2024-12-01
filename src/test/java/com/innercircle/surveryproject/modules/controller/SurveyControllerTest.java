package com.innercircle.surveryproject.modules.controller;

import com.innercircle.surveryproject.global.utils.FileUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SurveyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("설문조사 등록 시 설문조사 항목이 없는 경우 에러 발생")
    void test_case_1() throws Exception {
        // given
        String request = FileUtils.readFileAsString("testcase/fail_survey.txt");
        // when
        // then
        mockMvc.perform(post("/api/survey").contentType(MediaType.APPLICATION_JSON).content(request)).andExpect(
                status().isBadRequest())
            .andExpect(jsonPath("$.message").value("설문조사 항목은 1~10개까지 등록가능합니다."));
    }

    @Test
    @DisplayName("설문조사 등록 성공")
    void test_case_2() throws Exception {
        // given
        String request = FileUtils.readFileAsString("testcase/success_survey.txt");
        // then

        mockMvc.perform(post("/api/survey").contentType(MediaType.APPLICATION_JSON).content(request)).andExpect(
                status().isCreated())
            .andExpect(jsonPath("$.message").value("설문조사 등록에 성공하였습니다."));

    }

    @Test
    @DisplayName("설문조사 수정 성공")
    void test_case_3() throws Exception {
        // given
        String request = FileUtils.readFileAsString("testcase/survey_update_success.txt");
        // when // then
        ResultActions result = mockMvc.perform(put("/api/survey")
                                                   .contentType(MediaType.APPLICATION_JSON)
                                                   .content(request));

        // 응답 검증
        result.andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(200))
            .andExpect(jsonPath("$.message").value("설문조사 수정에 성공하였습니다."))
            .andExpect(jsonPath("$.data.name").value("서비스 만족도 설문조사"))
            .andExpect(jsonPath("$.data.description").value("서비스 만족도를 평가하는 설문조사입니다."));
    }

}