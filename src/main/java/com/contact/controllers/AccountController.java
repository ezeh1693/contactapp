package com.contact.controllers;

import com.contact.dao.Accessor;
import com.contact.dao.Filter;
import com.contact.dao.Pager;
import com.contact.dao.Param;
import com.contact.enums.AccountType;
import com.contact.models.Account;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.contact.utils.Utility;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * An Mvc controller class that manage all the interactions between a user's request and the Account Object.
 * It interacts with the database via jpa queries when necessary.
 * @author emmanuel
 */
@Controller
@Transactional
@RequestMapping("/accounts")
//@Security()
public class AccountController {

    /**
     * Return a paginated list of all the users in the system
     * @param request
     * @param model
     * @return
     */
    @RequestMapping
    public String index(HttpServletRequest request,  Model model) {
        Param p = Utility.getParam(request);
        List<Account> list = Accessor.findList(Account.class, Filter.get(), p);

        Long total = Accessor.count(Account.class, Filter.get());
        model.addAttribute("pager", new Pager(total, p.getPage(), p.getSize()));

        model.addAttribute("accounts", list);
        return "accounts/allAccounts";
    }

    /**
     * View an account by supplying it's id
     * @param model
     * @param id
     * @return
     */
    @RequestMapping("/{id}")
    public String page(Model model, @PathVariable Long id) {
        model.addAttribute("account", Accessor.findOne(Account.class, id));
        return "/accounts/viewAccount" ;
    }

    /**
     * Present a new Account entry form to the user
     * @param model
     * @return
     */
    @RequestMapping(params = {"new"})
    public String showForm(Model model) {
        model.addAttribute("accountTypes", Arrays.asList(AccountType.values()));
        return "accounts/createAccount";
    }

    /**
     * Submit a new Account entity
     * @param account
     * @param bindingResult
     * @param model
     * @return
     */
    @RequestMapping(params = {"new"}, method = RequestMethod.POST)
    public String submitForm(@Valid Account account, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", Utility.errors(bindingResult));
            model.addAttribute("account", account);
            model.addAttribute("accountTypes", Arrays.asList(AccountType.values()));
            return "accounts/createAccount";
        }

        Accessor.saveOne(account);
        return "redirect:/accounts";
    }

    @RequestMapping(value = "/{id}", params = {"edit"})
    public String showEditForm(Model model, @PathVariable("id") Long id) {
        Account account = Accessor.findOne(Account.class, id);
        if (null == account) {
            model.addAttribute("accountTypes", Arrays.asList(AccountType.values()));
            return "redirect:/accounts?new";
        }

        model.addAttribute("account", account);
        model.addAttribute("edit", "edit");
        model.addAttribute("accountTypes", Arrays.asList(AccountType.values()));
        return "accounts/createAccount";
    }

    /**
     * Submit an Edited Account
     * @param acc
     * @param bindingResult
     * @param model
     * @return
     */
    @RequestMapping(params = {"edit"}, method = RequestMethod.POST)
    public String submitEditForm(@Valid Account acc, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", Utility.errors(bindingResult));
            model.addAttribute("account", acc);
            model.addAttribute("accountTypes", Arrays.asList(AccountType.values()));
            return "accounts/createAccount";
        }

        Account oldAccount = Accessor.findOne(Account.class, acc.getId());

        Account account = updateAccount(acc, oldAccount);


        Accessor.saveOne(account);
        return "redirect:/accounts";
    }

    /**
     * Single method to manage all forms of update to an Account Object
     * @param accountForm
     * @param oldAccount
     * @return
     */
    private Account updateAccount(Account accountForm, Account oldAccount){
        if(StringUtils.isNotEmpty(accountForm.getName())){
            oldAccount.setName(accountForm.getName());
        }

        if(StringUtils.isNotEmpty(accountForm.getEmail())){
            oldAccount.setEmail(accountForm.getEmail());
        }

        if(StringUtils.isNotEmpty(accountForm.getPassword())){
            oldAccount.setPassword(accountForm.getPassword());
        }

        /*if(null != accountForm.getActive()){
            oldAccount.setActive(accountForm.getActive());
        }*/

        oldAccount.setActive((accountForm.getActive() == null) ? false : accountForm.getActive());

        if(StringUtils.isNotEmpty(accountForm.getPhone())){
            oldAccount.setPhone(accountForm.getPhone());
        }



        if(accountForm.getAccountType() != null){
            oldAccount.setAccountType(accountForm.getAccountType());
        }

        return oldAccount;
    }

}
