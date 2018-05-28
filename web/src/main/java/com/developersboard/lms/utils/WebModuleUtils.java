package com.developersboard.lms.utils;

import com.developersboard.lms.constant.signup.PasswordTokenConstants;
import com.developersboard.lms.constant.signup.SignUpConstants;
import com.developersboard.lms.model.payload.BasicAccountPayload;
import com.developersboard.lms.model.payload.ProAccountPayload;
import com.developersboard.lms.security.User;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.developersboard.lms.utils.LMSValidationUtils.validateInputs;

/**
 * Created by Eric on 3/30/2018.
 *
 * @author Eric Opoku
 */
@Slf4j
public abstract class WebModuleUtils {

    /**
     * Non instantiable
     */
    private WebModuleUtils() {
        throw new AssertionError("Non instantiable");
    }


    /**
     * Create user with basic attributes set.
     *
     * @param username The username.
     * @param email    The email.
     * @return A User Entity with basic plan.
     */
    public static User createBasicUser(String username, String email) {

        User user = new User();
        user.setUsername(username);
        user.setPassword("secret");
        user.setEmail(email);
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setPhone("123456789");
        user.setCountry("GB");
        user.setEnabled(true);
        user.setDescription("A basic User");
        user.setProfileImageUrl("https://blabla.images.com/blasicuser");

        return user;
    }

    public static String createPasswordResetUrl(
            HttpServletRequest httpServletRequest,
            Long userId,
            String token) {

        return getGenericUrl(httpServletRequest) +
                PasswordTokenConstants.CHANGE_PASSWORD_PATH +
                "?id=" +
                userId +
                "&token=" +
                token;
    }

    /**
     * AnyType that extends BasicAccountPayload will be accepted and converted to Bankend User
     *
     * @param frontendPayload the front end payload to be converted to backend User
     * @param <T> the object to convert
     * @return the user
     */
    public static <T extends BasicAccountPayload> User fromWebUserToDomainUser(T frontendPayload) {
        User user = new User();
        user.setUsername(frontendPayload.getUsername());
        user.setPassword(frontendPayload.getPassword());
        user.setFirstName(frontendPayload.getFirstName());
        user.setLastName(frontendPayload.getLastName());
        user.setEmail(frontendPayload.getEmail());
        user.setPhone(frontendPayload.getPhoneNumber());
        user.setCountry(frontendPayload.getCountry());
        user.setEnabled(true);
        user.setDescription(frontendPayload.getDescription());
        return user;
    }

    /**
     * Given the card info provided by the user in teh front-end, it retruns a parameters map to
     * obtain a stripe token
     *
     * @param payload The info provided by the user during registration
     * @return A parameters map that can be used to obtain a Stripe token
     */
    public static Map<String, Object> extractTokenParamsFromSignupPayload(ProAccountPayload payload) {

        Map<String, Object> tokenParams = new HashMap<>();
        Map<String, Object> cardParams = new HashMap<>();
        cardParams.put(SignUpConstants.STRIPE_CARD_NUMBER_KEY, payload.getCardNumber());
        cardParams.put(SignUpConstants.STRIPE_EXPIRY_MONTH_KEY, Integer.valueOf(payload.getCardMonth()));
        cardParams.put(SignUpConstants.STRIPE_EXPIRY_YEAR_KEY, Integer.valueOf(payload.getCardYear()));
        cardParams.put(SignUpConstants.STRIPE_CVC_KEY, payload.getCardCode());
        tokenParams.put(SignUpConstants.STRIPE_CARD_KEY, cardParams);

        return tokenParams;
    }

    /**
     * Generates a uri dynamically by constructing url using http servlet request
     * @param httpServletRequest the http servlet request
     * @return a dynamically formulated uri
     */
    public static String getGenericUrl(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getScheme() + // http or https
                "://" +
                httpServletRequest.getServerName() + // localhost or other
                ":" +
                httpServletRequest.getServerPort() + // Server port 8080
                httpServletRequest.getContextPath(); // Root context "/"
    }

    /**
     * Returns a ready-made simple mail message given parameters
     * @param webMasterEmail the email from
     * @param user the user to receive the email
     * @param messageBody the message body
     * @param messageSubject the subject.
     * @return a prepared simple mail message.
     */
    public static SimpleMailMessage getSimpleMailMessage(
            String webMasterEmail,
            User user,
            String messageBody,
            String messageSubject) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setReplyTo(webMasterEmail);
        mailMessage.setText(messageBody);
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject(messageSubject);
        try {
            mailMessage.setFrom(String.valueOf(new InternetAddress(webMasterEmail, "LMS")));
        } catch (UnsupportedEncodingException e) {
            LOG.error("There was an error setting internet name", e);
        }
        return mailMessage;

    }


    public static String upload(String url, MultipartFile multipartFile) throws IOException {
        validateInputs(multipartFile);
        File file = multipartToFile(multipartFile);
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(MediaType.parse("text/plain"), file))
//                .addFormDataPart("other_field", "other_field_value")
                .build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        Response response = client.newCall(request).execute();

        if (response.body() != null) {
            return response.body().string();
        }
        return null;
    }

    /**
     * Retrieve file content from multipart file
     * @param multipart the multipart file
     * @return the file
     * @throws IllegalStateException if something goes wrong with the internal conversion
     * @throws IOException if there is an error with inputs/outputs
     */
    private static File multipartToFile(MultipartFile multipart) throws IllegalStateException,
            IOException {
        validateInputs(multipart);

        File file = new File(Objects.requireNonNull(multipart.getOriginalFilename()));
        boolean newFile = file.createNewFile();
        if (newFile) LOG.debug("File created successfully!");
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(multipart.getBytes());
        fos.close();
        return file;
    }


    public static String executeCall(String url, Map<String,String> params) throws IOException {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        HttpUrl.Builder httpBuider = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder();
        if (params != null) {
            for(Map.Entry<String, String> param : params.entrySet()) {
                httpBuider.addQueryParameter(param.getKey(),param.getValue());
            }
        }
        Request request = new Request.Builder().url(httpBuider.build()).build();
        Response response = client.newCall(request).execute();
        if (response != null) {
            assert response.body() != null;
            return response.body().string();
        } else return null;
    }
}
