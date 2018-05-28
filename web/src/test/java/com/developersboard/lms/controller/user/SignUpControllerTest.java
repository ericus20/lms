package com.developersboard.lms.controller.user;

import com.developersboard.lms.model.payload.ProAccountPayload;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.time.Clock;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static com.developersboard.lms.constant.signup.SignUpConstants.PAYLOAD_MODEL_KEY_NAME;
import static com.developersboard.lms.constant.signup.SignUpConstants.SIGNUP_URL_MAPPING;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@RunWith(SpringRunner.class)
public class SignUpControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private Model model;

    @Mock
    private MockMultipartFile mockMultipartFile;

    @Rule
    public TestName testName = new TestName();

    @Before
    public void setUp() throws Exception {
        assertNotNull(mockMvc);
        URL url = Thread.currentThread()
                .getContextClassLoader().getResource("static/images/test.png");
        assert url != null;
        File file = new File(url.getPath());
        assertTrue(file.exists());
        InputStream inputStream = new FileInputStream(file);
        mockMultipartFile =
                new MockMultipartFile("file", "test.png",
                        "image", inputStream);

        assertNotNull(mockMultipartFile);
    }

    @Test
    public void signup() throws Exception {
    }

    @Test
    public void signupPost() throws Exception {
        ProAccountPayload payload = new ProAccountPayload();
        payload.setUsername(testName.getMethodName().concat("_username"));
        payload.setPassword(testName.getMethodName().concat("_password"));
        payload.setCountry(testName.getMethodName().concat("_country"));
        payload.setPhoneNumber(testName.getMethodName().concat("_123456789"));
        payload.setEmail(testName.getMethodName().concat("_@email.com"));
        payload.setFirstName(testName.getMethodName().concat("_firstName"));
        payload.setLastName(testName.getMethodName().concat("_lastName"));
        payload.setCardCode("314");
        payload.setCardMonth("1");
        payload.setCardNumber("4242424242424242");
        payload.setCardYear(LocalDate.now(Clock.systemUTC()).getYear() + 1 + "");

        Map<String, String> contentTypeParameters = new HashMap<String, String>();
        contentTypeParameters.put("boundary", "lms");
        MediaType contentType = new MediaType("multipart", "form-data", contentTypeParameters);


        mockMvc.perform(multipart(SIGNUP_URL_MAPPING).file(mockMultipartFile).contentType(contentType)
                .param("planId", "2")
                .flashAttr(PAYLOAD_MODEL_KEY_NAME, payload))
                .andDo(print())
                .andExpect(status().isOk());
//                .andExpect(model().attribute("msg", equalTo("SUCCESS")));

    }
}