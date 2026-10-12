package com.insurance.web.callback;

import org.apache.wicket.ajax.AjaxRequestTarget;
import java.io.Serializable;

public interface PolicyCreatedCallback extends Serializable {
    void onPolicyCreated(AjaxRequestTarget target);
}
