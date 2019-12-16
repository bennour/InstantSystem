package com.salah.instantsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.salah.instantsystem.restclient.model.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.Charset;

import static com.salah.instantsystem.constant.Constant.PARKING_NOT_FOUND_MSG;
import static com.salah.instantsystem.constant.Constant.SERVICE_NOT_AVAILABLE_MSG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ParkingControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RestTemplate restTemplate;

    private Response response;

    @Before
    public void setUp() throws IOException {
        ClassPathResource classPathResourceMockParkingList= new ClassPathResource("mock_API.json", this.getClass().getClassLoader());
        response = new ObjectMapper().readValue(classPathResourceMockParkingList.getInputStream(), Response.class);
    }

    // -------------------------------------
    // Tests cases : GET /parkings
    // -------------------------------------

    @Test
    public void shouldGetParkingList() throws Exception {
        Mockito.when(restTemplate.getForObject(anyString(), any())).thenReturn(response);

        String expectedResponse = "/expected_parkingList_response.json";
        ClassPathResource expectedResultPath = new ClassPathResource(expectedResponse, this.getClass().getClassLoader());
        String expectedResult = StreamUtils.copyToString( expectedResultPath.getInputStream(), Charset.defaultCharset());

        String uri = "/parkings";

        mockMvc.perform(get(uri))
                /*.andDo(print())*/
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResult));

    }

    @Test
    public void shouldThrowUnavailableServiceException_ParkingListCase() throws Exception {
        Mockito.when(restTemplate.getForObject(anyString(),any())).thenReturn(null);

        String uri = "/parkings";

        MvcResult mvcResult = mockMvc.perform(get(uri))
                /*.andDo(print())*/
                .andExpect(status().isServiceUnavailable())
                .andReturn();


        String errorMessage = mvcResult.getResolvedException().getMessage();
        assertThat(errorMessage).isEqualTo(SERVICE_NOT_AVAILABLE_MSG);
    }

    // -------------------------------------
    // Tests cases : GET /parkings/{id}
    // -------------------------------------

    @Test
    public void shouldGetParkingById() throws Exception {
        response.setNhits(1);
        response.getRecords().subList(1, response.getRecords().size()).clear();
        Mockito.when(restTemplate.getForObject(anyString(), any())).thenReturn(response);

        String uri = "/parkings/310";

        mockMvc.perform(get(uri))
                /*.andDo(print())**/
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("310"))
                .andExpect(jsonPath("$.key").value("Colombier"))
                .andExpect(jsonPath("$.max").value(1089))
                .andExpect(jsonPath("$.free").value(397))
                .andExpect(jsonPath("$.latitude").value( 48.10476252))
                .andExpect(jsonPath("$.longitude").value(-1.678624547))
                .andExpect(jsonPath("$.open").value(true))
                .andExpect(jsonPath("$.orgahoraires").value("24h/24 et 7j/7. Bureau ouvert, 7h30 à 20h30 sauf dimanche et jours fériés."))
                .andExpect(jsonPath("$.tarif15").value(0.4))
                .andExpect(jsonPath("$.tarif30").value(0.8))
                .andExpect(jsonPath("$.tarif1h").value(1.6))
                .andExpect(jsonPath("$.tarif1h30").value(2.2))
                .andExpect(jsonPath("$.tarif2h").value(2.8))
                .andExpect(jsonPath("$.tarif3h").value(4.0))
                .andExpect(jsonPath("$.tarif4h").value(5.2));
    }

    @Test
    public void shouldThrowNotFoundParkingException() throws Exception {
        response.setNhits(0);
        response.getRecords().clear();
        Mockito.when(restTemplate.getForObject(anyString(), any())).thenReturn(response);

        String uri = "/parkings/310";

        MvcResult mvcResult = mockMvc.perform(get(uri))
                /*.andDo(print())**/
                .andExpect(status().isNotFound())
                .andReturn();

        String errorMessage = mvcResult.getResolvedException().getMessage();
        assertThat(errorMessage).isEqualTo(String.format(PARKING_NOT_FOUND_MSG, "310"));
    }

    @Test
    public void shouldThrowUnavailableServiceException_ParkingByIdCase() throws Exception {
        Mockito.when(restTemplate.getForObject(anyString(), any())).thenReturn(null);

        String uri = "/parkings/310";

        MvcResult mvcResult = mockMvc.perform(get(uri))
                /*.andDo(print())**/
                .andExpect(status().isServiceUnavailable())
                .andReturn();

        String errorMessage = mvcResult.getResolvedException().getMessage();
        assertThat(errorMessage).isEqualTo(SERVICE_NOT_AVAILABLE_MSG);
    }
}
