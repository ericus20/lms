package com.developersboard.lms.controller.user;


import com.developersboard.lms.constant.index.IndexConstant;
import com.developersboard.lms.enums.PlansEnum;
import com.developersboard.lms.enums.RolesEnum;
import com.developersboard.lms.model.payload.ProAccountPayload;
import com.developersboard.lms.proxy.mail.EmailServiceProxy;
import com.developersboard.lms.proxy.payment.StripeServiceProxy;
import com.developersboard.lms.security.Plan;
import com.developersboard.lms.security.Role;
import com.developersboard.lms.security.User;
import com.developersboard.lms.service.i18n.I18NService;
import com.developersboard.lms.service.security.PlanService;
import com.developersboard.lms.service.security.UserService;
import com.developersboard.lms.utils.LMSValidationUtils;
import com.developersboard.lms.utils.WebModuleUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

import static com.developersboard.lms.constant.signup.SignUpConstants.*;


/**
 * Created by Eric on 3/29/2018.
 *
 * @author Eric Opoku
 */
@Slf4j
@Controller
public class SignUpController {

    private final UserService userService;
    private final PlanService planService;
    private final I18NService i18NService;
    private final EmailServiceProxy emailService;
    private final StripeServiceProxy stripeService;

    @Value("${webmaster.email}")
    private String webMasterEmail;

    @Autowired
    public SignUpController(
            UserService userService,
            PlanService planService,
            I18NService i18NService,
            EmailServiceProxy emailService, StripeServiceProxy stripeService) {
        this.userService = userService;
        this.planService = planService;
        this.i18NService = i18NService;
        this.emailService = emailService;
        this.stripeService = stripeService;
    }

    /**
     * Redirects user to the sign-up form to complete registration
     *
     * @param planId the plan chosen by user
     * @param model  the model to transfer objects to view
     * @return the view of the subscription/registration page
     */
    @GetMapping(value = SIGNUP_URL_MAPPING)
    public String signup(@RequestParam("plan") int planId, Model model) {

        LMSValidationUtils.validateInputs(planId);

        if (planId != PlansEnum.BASIC.getId() && planId != PlansEnum.PRO.getId()) {

            LOG.error("Plan id is not valid");
            model.addAttribute(ERROR_MESSAGE_KEY, "plan.id.not.found");

        } else {
            model.addAttribute(PAYLOAD_MODEL_KEY_NAME, new ProAccountPayload());
        }

        return SUBSCRIPTION_VIEW_NAME;
    }

    /**
     * Creates a new User and transports user image to Amazon S3
     *
     * @param planId id of the selected plan
     * @param model  model to transport objects to view
     * @param image  the image of the user new user
     * @return if an error occurred with the specified plan
     */
    @PostMapping(value = SIGNUP_URL_MAPPING)
    public String signupPost(
            @RequestParam(name = "planId") int planId,
            @RequestParam(name = "file", required = false) MultipartFile image,
            @ModelAttribute(PAYLOAD_MODEL_KEY_NAME) @Valid ProAccountPayload payload,
            Model model) {

        // Ensure that valid inputs are passed to method
        LMSValidationUtils.validateInputs(planId, image, payload);

        if (planId != PlansEnum.BASIC.getId() && planId != PlansEnum.PRO.getId()) {
            model.addAttribute(SIGNED_UP_MESSAGE_KEY, false);
            model.addAttribute(ERROR_MESSAGE_KEY, "Plan id does not exist");
        }

        // If there exist a username or email in our database, return error to the user.
        boolean duplicates = checkForDuplicates(payload, model);

        if (duplicates) {
            return SUBSCRIPTION_VIEW_NAME;
        }

        // There are certain info that the user doesn't set, such as profile image URL, Stripe
        // customer id, plans and roles. Those needs to be set at the backend.
        LOG.debug("Transforming user payload into User domain object");
        User user = WebModuleUtils.fromWebUserToDomainUser(payload);

        // Stores the profile image on Amazon s3 and stores the URL in the user's record
        if (image != null && !image.isEmpty()) {

            String url = "http://localhost:8081/amazon-s3-service/storeProfileImage/" + user
                    .getUsername();
            String profileImageUrl = null;
            try {
                profileImageUrl = WebModuleUtils.upload(url, image);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (profileImageUrl != null) {
                user.setProfileImageUrl(profileImageUrl);
            } else {
                LOG.warn("There was a problem uploading the profile image to S3. The use's " +
                        "profile will be created without the image");
            }

        }

        // Attempt to assign plan with the planId to user. Return if an error occurs.
        // Sets the plan and the roles (depending on teh chosen plan)
        if (assignPlanToUser(planId, model, user)) return SUBSCRIPTION_VIEW_NAME;

        // Assign roles to the user and successfully save user to database
        if (assignRolesAndCreateUser(planId, payload, model, user)) return SUBSCRIPTION_VIEW_NAME;
        LOG.info("User created Successfully", user);

        String emailText = i18NService.getMessage("signup.confirmation.success.email.text");
        String messageSubject = i18NService.getMessage("signup.confirmation.success.email.subject");
        SimpleMailMessage mailMessage = WebModuleUtils.getSimpleMailMessage(webMasterEmail, user, emailText, messageSubject);
        emailService.sendGenericMail(mailMessage);

        LOG.debug("Mail successfully sent!");

        model.addAttribute(SIGNED_UP_MESSAGE_KEY, true);

        // Sign out user before displaying view.
        SecurityContextHolder.clearContext();

        return IndexConstant.LOGIN_VIEW_NAME;
    }


    //===================================================> Private methods
    //
    //              Private Methods
    //
    //===================================================> Private methods

    /*
     * Checks if the username/email are duplicates and sets error flags in the model.
     * Side effect: the method set attributes on Model
     *  @param payload The payload from the web filled with user's information
     * @param model   The model to transfer results from method.
     */
    private boolean checkForDuplicates(ProAccountPayload payload, Model model) {

        /* Check for duplicates in username */
        if (userService.checkUsernameExists(payload.getUsername())) {
            model.addAttribute(DUPLICATED_USERNAME_KEY, true);
        }

        /* Check for duplicates in email */
        if (userService.checkEmailExists(payload.getEmail())) {
            model.addAttribute(DUPLICATED_EMAIL_KEY, true);
        }

        boolean duplicates = false;

        List<String> errorMessages = new ArrayList<>();

        if (model.containsAttribute(DUPLICATED_USERNAME_KEY)) {
            LOG.warn("The username already exits. Displaying error to the user");
            model.addAttribute(SIGNED_UP_MESSAGE_KEY, false);
            errorMessages.add("Username already exist");
            duplicates = true;
        }

        if (model.containsAttribute(DUPLICATED_EMAIL_KEY)) {
            LOG.warn("The email already exists. Displaying error to the user");
            model.addAttribute(SIGNED_UP_MESSAGE_KEY, false);
            errorMessages.add("Email already exist");
            duplicates = true;
        }

        if (duplicates) {
            model.addAttribute(ERROR_MESSAGE_KEY, errorMessages);
            return true;
        }

        return false;
    }

    /*
     *  Assigns plan to the user based on the selected plan
     *
     * @param planId id of the selected plan
     * @param model model to transport objects to view
     * @param user the new user
     * @return if an error occurred with the specified plan
     */
    private boolean assignPlanToUser(
            @RequestParam(name = "planId") int planId, Model model, User user) {
        LOG.debug("Retrieving plan from the database");
        Optional<Plan> planById = planService.getPlanById(planId);

        if (!planById.isPresent()) {
            LOG.error("The plan id {} could not be found. Throwing exception.", planId);
            model.addAttribute(SIGNED_UP_MESSAGE_KEY, false);
            model.addAttribute(ERROR_MESSAGE_KEY, "Plan id not found");
            return true;
        }

        Plan selectedPlan = planById.get();
        user.setPlan(selectedPlan);
        return false;
    }


    /*
     *  Assigns a role to the user then persist to database.
     *
     * @param planId id of the selected plan
     * @param model model to transport objects to view
     * @param user the new user
     * @return if an error occurred with the specified plan
     */
    private boolean assignRolesAndCreateUser(
            @RequestParam(name = "planId") int planId,
            @Valid @ModelAttribute(PAYLOAD_MODEL_KEY_NAME) ProAccountPayload payload,
            Model model, User user) {

        User registeredUser;

        // By default users get the BASIC ROLE
        Set<Role> roles = new HashSet<>();
        if (planId == PlansEnum.BASIC.getId()) {

            roles.add(new Role(RolesEnum.USER));
            Optional<User> savedUser = userService.createUser(user, roles);
            assert savedUser.isPresent();
            registeredUser = savedUser.get();

        } else {

            // Extra precaution in case the POST is invoked programmatically
            if (StringUtils.isEmpty(payload.getCardCode()) ||
                    StringUtils.isEmpty(payload.getCardMonth()) ||
                    StringUtils.isEmpty(payload.getCardYear()) ||
                    StringUtils.isEmpty(payload.getCardNumber())) {
                LOG.error("One or more credit card fields is null or empty. Returning error to " +
                        "the user");
                model.addAttribute(SIGNED_UP_MESSAGE_KEY, false);
                model.addAttribute(ERROR_MESSAGE_KEY, "One or more credit card details is null " +
                        "or empty");
                return true;
            }

            // Create stripe customer and return the id assigned to the user.
            String stripeCustomerId = createStripeAccount(payload, user);
            user.setStripeCustomerId(stripeCustomerId);

            roles.add(new Role(RolesEnum.USER));
            Optional<User> savedUser = userService.createUser(user, roles);
            assert savedUser.isPresent();
            registeredUser = savedUser.get();
        }

        // Auto login the registered user
        Authentication authentication = new UsernamePasswordAuthenticationToken(registeredUser,
                null, registeredUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return false;
    }

    /*
     * Creates a stripe account given the payload and user.
     * @param payload the payload with payment details.
     * @param user the user who own's the payload
     * @return the stripeCustomerId.
     */
    private String createStripeAccount(
            @Valid @ModelAttribute(PAYLOAD_MODEL_KEY_NAME) ProAccountPayload payload,
            User user) {

        Map<String, Object> tokenParams = WebModuleUtils.extractTokenParamsFromSignupPayload(payload);
        Map<String, Object> customerParams = new HashMap<>();

        customerParams.put("description", "LMS customer. Username: " + payload.getUsername());
        customerParams.put("email", payload.getEmail());
        customerParams.put("plan", user.getPlan().getId());

        LOG.debug("Subscribing the customer to plan{}", user.getPlan().getName());

        String stripeCustomerId = stripeService.createCustomer(tokenParams, customerParams);
        LOG.debug("Username: {} has been subscribed to Stripe", payload.getUsername());
        return stripeCustomerId;
    }


}
