package com.developersboard.lms.controller.user;

import com.developersboard.lms.proxy.amazon.AmazonS3ServiceProxy;
import com.developersboard.lms.security.User;
import com.developersboard.lms.service.security.UserService;
import com.developersboard.lms.utils.WebModuleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static com.developersboard.lms.constant.user.ProfileConstants.*;

/**
 * Created by Eric on 3/30/2018.
 *
 * @author Eric Opoku
 */
@Controller
public class ProfileController {

    /**
     * The application logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(ProfileController.class);

    private final UserService userService;
    private final AmazonS3ServiceProxy s3Service;

    @Autowired
    public ProfileController(UserService userService, AmazonS3ServiceProxy s3Service) {
        this.userService = userService;
        this.s3Service = s3Service;
    }

    /**
     * View user's page.
     *
     * @param model The model to convey objects to view layer
     * @return profile page.
     */
    @GetMapping(value = USER_PROFILE_URL_MAPPING)
    public String profile(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authorizedUser = (User) authentication.getPrincipal();

        if (authorizedUser == null) {
            LOG.warn("Illegal user access this method... Returning to error page.");
            return REDIRECT_TO_LOGIN;
        }

        Optional<User> userById = userService.getUserById(authorizedUser.getId());
        authorizedUser = userById.orElse(authorizedUser);

        model.addAttribute(AUTHORIZED_USER_MODEL_KEY, authorizedUser);

        if (authorizedUser.getProfileImageUrl() != null) {

            // Retrieve user profile from amazon S3
            String imageUrl = "https://s3.amazonaws.com/eopoku3-lms/" + authorizedUser
                    .getProfileImageUrl();

            model.addAttribute("profileImage", imageUrl);
            model.addAttribute(PROFILE_IMAGE_URL_VALID, true);
        } else {

            model.addAttribute("profileImage", "https://picsum.photos/500/300/?random");


        }

        return USER_PROFILE_VIEW_NAME;
    }

    /**
     * Updates the user
     *
     * @param user          the user to update
     * @param bindingResult holds any error that occurs during the binding of user from view
     * @param model         the model
     * @return the view to profile page.
     */
    @PutMapping(value = "/update")
    public String updateProfile(
            @ModelAttribute(AUTHORIZED_USER_MODEL_KEY) User user,
            BindingResult bindingResult,
            @RequestParam(name = "file", required = false) MultipartFile multipartFile,
            Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authorizedUser = (User) authentication.getPrincipal();

        if (bindingResult.hasErrors() || user == null || (authorizedUser == null) ||
                !(user.getId().equals(authorizedUser.getId()))) {

            model.addAttribute("error", "true");

            return REDIRECT_TO_LOGIN;
        }

        if (multipartFile != null && !multipartFile.isEmpty()) {

            String url = "http://localhost:8081/amazon-s3-service/storeProfileImage/" + user.getUsername();
            String updatedProfileImage = null;
            try {
                LOG.debug("Attempting to delete profile image from S3 with key {}", user.getProfileImageUrl());
                if (s3Service.deleteProfileImageFromS3(user.getProfileImageUrl())) {
                    LOG.debug("Successfully deleted profile image. Ready to transfer new image.");
                }
                updatedProfileImage = WebModuleUtils.upload(url, multipartFile);
                user.setProfileImageUrl(updatedProfileImage);
                LOG.debug("Profile image successfully updated.");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        userService.updateUser(user);

        return "redirect:/profile";
    }

}
