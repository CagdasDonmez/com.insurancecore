package com.insurance.web;

import com.insurance.web.page.PolicyPage;
import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;

public class InsuranceApplication extends WebApplication
{
    @Override
    public Class<? extends Page> getHomePage() {
        return PolicyPage.class;
    }
}
