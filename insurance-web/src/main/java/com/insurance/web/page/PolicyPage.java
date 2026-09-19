package com.insurance.web.page;

import com.insurance.domain.entity.Policy;
import com.insurance.service.InvalidPolicyException;
import com.insurance.service.PolicyService;
import org.apache.wicket.markup.html.WebPage;
import com.insurance.web.InsuranceApplication;
import org.apache.wicket.model.Model;
import org.apache.wicket.extensions.markup.html.form.datetime.LocalDateTextField;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.validation.IValidationError;
import org.apache.wicket.validation.IValidator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PolicyPage extends WebPage {

    private PolicyService service() {
        return ((InsuranceApplication) getApplication()).getPolicyService();
    }

    public PolicyPage() {
        initPage();
    }

    public void initPage() {
        add(new Label("policies", "Policies"));

        LoadableDetachableModel<List<Policy>> ldm_policies = new  LoadableDetachableModel<List<Policy>>() {
            @Override
            protected List<Policy> load() {
                return service().getPolicies();
            }
        };

        add(new ListView<Policy>("policyRows", ldm_policies) {
            @Override
            protected void populateItem(ListItem<Policy> item) {
                Policy policy = item.getModelObject();
                item.add(new Label("policyNumber", policy.getPolicyNumber()));
                item.add(new Label("policyPremium", policy.getPremium()));
            }
        });

        Model<Long> customerId = Model.of();
        Policy newPolicy = new Policy();
        CompoundPropertyModel<Policy> model = new CompoundPropertyModel<>(newPolicy);
        Form<Policy> form = new Form<>("createPolicyForm", model) {
            @Override
            protected void onSubmit() {
                Policy submittedPolicy = getModelObject();
                try {
                    service().createPolicy(submittedPolicy, customerId.getObject());
                    setModelObject(new Policy());
                } catch (InvalidPolicyException e) {
                    this.error("Policy validation failed. " + e.getMessage());
                }
            }
        };

        TextField<String> policyNumber = new TextField<>("policyNumber");
        policyNumber.setRequired(true);
        form.add(policyNumber);

        TextField<BigDecimal> premium = new TextField<>("premium");
        premium.setRequired(true);
        premium.add((IValidator<BigDecimal>) validatable -> {
            BigDecimal value = validatable.getValue();
            if (value.compareTo(BigDecimal.ZERO) <= 0) {
                validatable.error((IValidationError) messageSource -> "Premium must be greater than zero.");
            }
        });
        form.add(premium);

        form.add(new TextField<Long>("customerId", customerId, Long.class).setRequired(true));
        form.add(new LocalDateTextField("validFrom", "yyyy-MM-dd").setRequired(true));
        form.add(new LocalDateTextField("validTo", "yyyy-MM-dd").setRequired(true));

        Button createPolicyButton = new Button("createButton");
        form.add(createPolicyButton);

        FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
        add(feedbackPanel);
        add(form);

        Link<Void> claimsLink = new Link<>("claimsLink"){
            @Override
            public void onClick() {
                setResponsePage(ClaimPage.class);
            }
        };

        add(claimsLink);
    }

}
