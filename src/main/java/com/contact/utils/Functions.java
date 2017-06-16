package com.contact.utils;

import com.lyncode.jtwig.functions.annotations.JtwigFunction;
import com.lyncode.jtwig.functions.annotations.Parameter;
import com.contact.dao.Pager;
import com.contact.enums.AccountType;
import com.contact.models.Account;

import java.util.Arrays;
import java.util.Date;
import java.util.Set;

public class Functions {


    @JtwigFunction(name = "fullUrl")
    public String pager(@Parameter String uri, @Parameter int page, @Parameter String qString) {
        return UriUtil.getFullPagedUrl(uri, page, qString);
    }

    @JtwigFunction(name = "pagerUri")
    public String pager(@Parameter String uri, @Parameter int page) {
        return UriUtil.getPagedUrl(uri, page);
    }

    @JtwigFunction(name = "newPager")
    public Pager newPager(@Parameter Pager pager, @Parameter Long total, @Parameter int page, int size) {
        return new Pager(total,  page, size);
    }


    @JtwigFunction(name = "concatStr2")
    public String concatStr2(@Parameter String str, @Parameter String addStr) {
        return str + addStr;
    }

    @JtwigFunction(name = "concatStr")
    public String concatStr(@Parameter String str, @Parameter String... addStr) {
        StringBuilder sb = new StringBuilder(str);
        Arrays.asList(addStr).forEach(p -> {
            sb.append(p);
        });
        return sb.toString();
    }



}
