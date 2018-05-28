package com.developersboard.lms.proxy.payment;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Created by Eric on 5/19/2018.
 *
 * @author Eric Opoku
 */
@FeignClient(name = "payment-service")
@RibbonClient(name = "payment-service")
public interface StripeServiceProxy {

    @PostMapping(path = "/stripe/createCustomer")
    String createCustomer(@RequestParam Map<String, Object> tokenParams,
                          @RequestParam Map<String, Object> customerParams);

}
