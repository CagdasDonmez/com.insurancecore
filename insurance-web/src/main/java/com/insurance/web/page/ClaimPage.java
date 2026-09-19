package com.insurance.web.page;

import com.insurance.domain.entity.Claim;
import com.insurance.service.ClaimService;
import com.insurance.service.InvalidClaimException;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ClaimPage extends WebPage {

    private ClaimService service() {
        return ((InsuranceApplication) getApplication()).getClaimService();
    }

    public ClaimPage() {
        initPage();
    }

    public void initPage() {
        add(new Label("claims", "Claims"));

        LoadableDetachableModel<List<Claim>> ldm_claims = new  LoadableDetachableModel<>() {
            @Override
            protected List<Claim> load() {
                return service().getClaims();
            }
        };

        add(new ListView<Claim>("claimRows", ldm_claims) {
            @Override
            protected void populateItem(ListItem<Claim> item) {
                Claim claim = item.getModelObject();
                item.add(new Label("claimNumber", claim.getClaimNumber()));
                item.add(new Label("amount", claim.getAmount()));
                item.add(new Label("creationDate", claim.getCreationDate()));
                item.add(new Label("description", claim.getDescription()));
            }
        });

        Model<Long> policyId = Model.of();
        Claim claim = new Claim();
        CompoundPropertyModel<Claim> model = new CompoundPropertyModel<>(claim);
        Form<Claim> form = new Form<Claim>("createClaimForm", model) {
            @Override
            protected void onSubmit() {
                Claim submittedClaim = getModelObject();
                try {
                    service().createClaim(submittedClaim, policyId.getObject());
                    setModelObject(new Claim());
                } catch (InvalidClaimException e) {
                    this.error(e.getMessage());
                }
            }
        };

        TextField<String> claimNumber = new TextField<>("claimNumber");
        claimNumber.setRequired(true);
        form.add(claimNumber);

        TextField<BigDecimal> amount = new TextField<>("amount");
        amount.setRequired(true);
        amount.add(validatable -> {
            BigDecimal value = validatable.getValue();
            if (value.compareTo(BigDecimal.ZERO) <= 0) {
                validatable.error((IValidationError) messageSource -> "Amount must be greater than zero.");
            }
        });
        form.add(amount);

        LocalDateTextField creationDate = new LocalDateTextField("creationDate", "yyyy-MM-dd");
        creationDate.setRequired(true);
        creationDate.add(validatable -> {
            LocalDate value = validatable.getValue();
            if (value.isBefore(LocalDate.now())) {
                validatable.error((IValidationError) messageSource -> "Creation Date can not be in the past.");
            }
        });
        form.add(creationDate);

        TextField<String> description = new TextField<>("description");
        form.add(description);

        form.add(new TextField<Long>("policyId", policyId, Long.class).setRequired(true));

        Button createButton = new Button("createClaimButton");
        form.add(createButton);

        FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
        add(feedbackPanel);
        add(form);

        Link<Void> policiesLink = new Link<>("policiesLink") {
            @Override
            public void onClick() {
                setResponsePage(PolicyPage.class);
            }
        };

        add(policiesLink);
    }

}
