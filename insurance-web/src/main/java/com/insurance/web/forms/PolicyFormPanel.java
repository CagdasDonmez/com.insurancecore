package com.insurance.web.forms;

import com.insurance.domain.entity.Policy;
import com.insurance.service.InvalidPolicyException;
import com.insurance.service.PolicyService;
import com.insurance.web.InsuranceApplication;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.form.datetime.LocalDateTextField;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.IValidationError;
import org.apache.wicket.validation.IValidator;

import java.math.BigDecimal;

public class PolicyFormPanel extends Panel {

    private static final long serialVersionUID = 1L;
    private Form<Policy> form;

    private PolicyService service() {
        return ((InsuranceApplication) getApplication()).getPolicyService();
    }

    public PolicyFormPanel(String id) {
        super(id);
        initPolicyForm();
    }

    private void initPolicyForm() {
        Policy newPolicy = new Policy();
        CompoundPropertyModel<Policy> model = new CompoundPropertyModel<>(newPolicy);
        Model<Long> customerId = Model.of();
        this.form = new Form<>("createPolicyForm", model);

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

        FeedbackPanel feedbackPanel = new FeedbackPanel("error_feedback");
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);

        AjaxButton createPolicyButton = new AjaxButton("createButton") {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                Policy policy = form.getModelObject();
                try {
                    service().createPolicy(policy, customerId.getObject());
                    form.setModelObject(new Policy());
                } catch (InvalidPolicyException e)  {
                    this.error("Policy validation failed. " + e.getMessage());
                }

                target.add(feedbackPanel);
            }

            @Override
            protected void onError(AjaxRequestTarget target) {
                super.onError(target);
                target.add(feedbackPanel);
            }
        };

        form.add(createPolicyButton);
        add(form);
    }

}
