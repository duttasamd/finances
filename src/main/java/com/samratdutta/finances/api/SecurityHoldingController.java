package com.samratdutta.finances.api;

import com.samratdutta.finances.service.AccountService;
import com.samratdutta.finances.service.SecurityHoldingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class SecurityHoldingController {
    @Autowired
    private SecurityHoldingService securityHoldingService;
}
