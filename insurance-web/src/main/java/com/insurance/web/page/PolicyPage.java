package com.insurance.web.page;

import com.insurance.domain.entity.Policy;
import com.insurance.service.PolicyService;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.validation.IValidationError;
import org.apache.wicket.validation.IValidator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PolicyPage extends WebPage {

    // @EJB
    private PolicyService policyService;

    public PolicyPage() {
        initPage();
    }

    public void initPage() {
        add(new Label("policies", "Policies"));
        List<Policy> policies = new ArrayList<>();
        /*
        Policy newPolicy = new Policy();
        newPolicy.setPolicyNumber("POL-0001");
        newPolicy.setPremium(new BigDecimal("100.00"));
        policies.add(newPolicy);

        Policy policy2 = new Policy();
        policy2.setPolicyNumber("POL-0002");
        policy2.setPremium(new BigDecimal("200.00"));
        policies.add(policy2);
        */

        add(new ListView<Policy>("policyRows", policies) {
            @Override
            protected void populateItem(ListItem<Policy> item) {
                Policy policy = item.getModelObject();
                item.add(new Label("policyNumber", policy.getPolicyNumber()));
                item.add(new Label("policyPremium", policy.getPremium()));
            }
        });

        Policy newPolicy = new Policy();
        CompoundPropertyModel<Policy> model = new CompoundPropertyModel<>(newPolicy);
        Form<Policy> form = new Form<>("createPolicyForm", model) {
            @Override
            protected void onSubmit() {
                Policy submittedPolicy = getModelObject();
                policies.add(submittedPolicy);
                setModelObject(new Policy());
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

        Button createPolicyButton = new Button("createButton");
        form.add(createPolicyButton);

        FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
        add(feedbackPanel);
        add(form);
    }

}
