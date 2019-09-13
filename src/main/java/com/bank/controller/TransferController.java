package com.bank.controller;

import com.bank.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("transfer")
public class TransferController {
    private TransferService transferService;

    @Autowired
    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping(value = "/sameInquiry", produces = "application/json; charset=UTF-8")
    public String transferSameInquiry(@RequestBody String message) {
        System.out.println(message);
        return transferService.processInquiry(message);
    }

    @PostMapping(value = "/same", produces = "application/json; charset=UTF-8")
    public String transferSame(@RequestBody String message) {
        return transferService.process(message);
    }

}
