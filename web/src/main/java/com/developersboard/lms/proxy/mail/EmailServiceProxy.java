package com.developersboard.lms.proxy.mail;

import com.developersboard.lms.model.Feedback;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Eric on 5/19/2018.
 *
 * @author Eric Opoku
 */
@FeignClient(name = "email-service")
@RibbonClient(name = "email-service")
public interface EmailServiceProxy {

    /**
     * Sends a an email with a simple mail message
     * @param mailMessage the simple mail message to send
     * @return the status true for success or false for error
     */
    @PostMapping(path = "/email-service/sendGenericMail")
    boolean sendGenericMail(@RequestParam(name = "mailMessage") SimpleMailMessage mailMessage);


    /**
     * Sends an email given a feedback object
     * @param feedback the feedback
     * @return the status true for success or false for error
     */
    @PostMapping(path = "/email-service/sendFeedback")
    boolean sendMailWithFeedback(@RequestBody Feedback feedback);
}
