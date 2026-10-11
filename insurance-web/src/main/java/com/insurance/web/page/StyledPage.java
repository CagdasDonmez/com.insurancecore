package com.insurance.web.page;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.resource.PackageResourceReference;

/** Shared presentation resources for the insurance workspace. */
public abstract class StyledPage extends WebPage {
    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(CssHeaderItem.forReference(
                new PackageResourceReference(StyledPage.class, "workspace.css")));
    }
}
