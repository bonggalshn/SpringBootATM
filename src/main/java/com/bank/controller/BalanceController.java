package com.bank.controller;

import com.bank.service.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("balance")
public class BalanceController {
    private BalanceService balanceService;

    @Autowired
    public BalanceController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @PostMapping(value = "/info", produces = "application/json; charset=UTF-8")
    public String BalanceInfo(@RequestBody String message) {
        return balanceService.process(message);
    }

}
