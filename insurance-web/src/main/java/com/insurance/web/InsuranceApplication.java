package com.insurance.web;

import com.insurance.web.page.PolicyPage;
import com.insurance.service.PolicyService;
import com.insurance.service.ClaimService;
import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class InsuranceApplication extends WebApplication {
    @Override
    public Class<? extends Page> getHomePage() { return PolicyPage.class; }

    // Resolve container-managed EJBs on demand; never serialize an EJB reference in a page.
    public PolicyService getPolicyService() { return lookup("PolicyService", PolicyService.class); }
    public ClaimService getClaimService() { return lookup("ClaimService", ClaimService.class); }

    private <T> T lookup(String name, Class<T> type) {
        try {
            return type.cast(InitialContext.doLookup("java:module/" + name));
        } catch (NamingException e) {
            throw new IllegalStateException("Cannot resolve EJB " + name, e);
        }
    }
}