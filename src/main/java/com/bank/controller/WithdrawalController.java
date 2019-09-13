package com.bank.controller;

import com.bank.service.WithdrawalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("withdrawal")
public class WithdrawalController {
    private WithdrawalService withdrawalService;

    @Autowired
    public WithdrawalController(WithdrawalService withdrawalService){
        this.withdrawalService = withdrawalService;
    }

    @PostMapping(value = "/withdraw", produces = "application/json; charset=UTF-8")
    public String withdraw(@RequestBody String message){
        System.out.println("WithdrawalController: "+message);
        return withdrawalService.process(message);
    }
}
