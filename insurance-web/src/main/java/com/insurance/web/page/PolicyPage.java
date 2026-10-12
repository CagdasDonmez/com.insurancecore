package com.insurance.web.page;

import com.insurance.domain.entity.Policy;
import com.insurance.service.PolicyService;
import com.insurance.web.forms.PolicyFormPanel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import com.insurance.web.InsuranceApplication;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.LoadableDetachableModel;

import java.util.List;

public class PolicyPage extends StyledPage {

    private PolicyService service() {
        return ((InsuranceApplication) getApplication()).getPolicyService();
    }

    public PolicyPage() {
        initPage();
    }

    public void initPage() {
        add(new Label("policies", "Policies"));
        WebMarkupContainer webMarkupContainer = new WebMarkupContainer("webMarkupContainer");
        webMarkupContainer.setOutputMarkupId(true);

        ListView<Policy> policyListView = getPolicyListView();
        webMarkupContainer.add(policyListView);

        PolicyFormPanel policyFormPanel =
                new PolicyFormPanel("policyFormPanel",
                    target -> {
                        target.add(webMarkupContainer);
                    });

        FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
        add(feedbackPanel);

        Link<Void> claimsLink = new Link<>("claimsLink"){
            @Override
            public void onClick() {
                setResponsePage(ClaimPage.class);
            }
        };

        add(policyFormPanel);
        add(claimsLink);
        add(webMarkupContainer);
    }

    private ListView<Policy> getPolicyListView() {
        LoadableDetachableModel<List<Policy>> ldm_policies = new LoadableDetachableModel<>() {
            @Override
            protected List<Policy> load() {
                return service().getPolicies();
            }
        };

        return new ListView<>("policyRows", ldm_policies) {
            @Override
            protected void populateItem(ListItem<Policy> item) {
                Policy policy = item.getModelObject();
                item.add(new Label("policyNumber", policy.getPolicyNumber()));
                item.add(new Label("policyPremium", policy.getPremium()));
                item.add(new Label("validFrom", policy.getValidFrom()));
                item.add(new Label("validTo", policy.getValidTo()));
            }
        };
    }

}
