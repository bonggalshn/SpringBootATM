package com.bank.service;

import com.bank.entity.Customer;
import com.bank.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Customer findByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    public Customer update(Customer customer) {
        return accountRepository.save(customer);
    }

    public boolean checkAccount(String accountNumber, String pinNumber) {
        Customer customer = findByAccountNumber(accountNumber);
        if (customer == null)
            return false;
        if (Integer.parseInt(customer.getPinNumber()) == Integer.parseInt(pinNumber))
            return true;
        else
            return false;
    }
}
