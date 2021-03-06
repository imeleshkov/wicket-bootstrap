package de.agilecoders.wicket.core.markup.html.bootstrap.components.progress;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.ResourceReference;

import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.util.Attributes;

/**
 * A specialization of wicket-extensions' {@link org.apache.wicket.extensions.ajax.markup.html.form.upload.UploadProgressBar}
 * that uses Bootstrap markup and styles.
 */
public class UploadProgressBar extends org.apache.wicket.extensions.ajax.markup.html.form.upload.UploadProgressBar {

    /**
     * The color type of the stack
     */
    private ProgressBar.Type type = ProgressBar.Type.DEFAULT;

    /**
     * A flag indicating whether the progress bar is animated/active.
     */
    private boolean active = false;

    /**
     * A flag indicating whether the progress bar is striped.
     */
    private boolean striped = false;

    /**
     * Constructor that will display the upload progress bar for every submit of the given form.
     * Starts with initial progress value of {@value de.agilecoders.wicket.core.markup.html.bootstrap.components.progress.ProgressBar#MIN}
     *
     * @param id The component id
     * @param form The form with the file upload fields
     */
    public UploadProgressBar(String id, Form<?> form) {
        this(id, form, Model.of(ProgressBar.MIN));
    }

    /**
     * Constructor that will display the upload progress bar for every submit of the given form.
     *
     * @param id The component id
     * @param form The form with the file upload fields
     * @param model The initial value of the progress
     */
    public UploadProgressBar(String id, Form<?> form, IModel<Integer> model) {
        this(id, form, null, model);
    }

    /**
     * Constructor that will display the upload progress bar for the given file upload field.
     *
     * @param id The component id
     * @param form The form with the file upload fields
     * @param fileUploadField The file upload field which progress will be followed.
     * @param model The initial value of the progress
     */
    public UploadProgressBar(String id, Form<?> form, FileUploadField fileUploadField, IModel<Integer> model) {
        super(id, form, fileUploadField);

        setRenderBodyOnly(false);

        setDefaultModel(model);
    }

    @Override
    protected ResourceReference getCss() {
        return null;
    }

    public boolean striped() {
        return striped;
    }

    public UploadProgressBar striped(boolean value) {
        striped = value;
        return this;
    }

    public boolean active() {
        return active;
    }

    public UploadProgressBar active(boolean value) {
        active = value;
        if (value) {
            striped(true);
        }
        return this;
    }

    public ProgressBar.Type type() {
        return type;
    }

    public UploadProgressBar type(ProgressBar.Type type) {
        this.type = type;
        return this;
    }

    @Override
    protected MarkupContainer newBarComponent(String id) {
        return new WebMarkupContainer(id) {
            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);

                if (!ProgressBar.Type.DEFAULT.equals(type)) {
                    Attributes.addClass(tag, type().cssClassName());
                }

                tag.put("style", createStyleValue().getObject());
            }
        };
    }

    @Override
    protected MarkupContainer newStatusComponent(String id) {
        MarkupContainer status = super.newStatusComponent(id);
        status.setVisible(false);
        return status;
    }

    private IModel<String> createStyleValue() {
        return Model.of(String.format("width: %s%%", value()));
    }

    /**
     * Returns whether the progress bar is complete or not.
     *
     * @return {@code true} if the progress bar is complete.
     */
    public final boolean complete() {
        return value() == ProgressBar.MAX;
    }

    /**
     * Sets a new value for the progress.
     *
     * @return this instance, for method chaining.
     */
    public UploadProgressBar value(IModel<Integer> value) {
        setDefaultModel(value);
        return this;
    }

    /**
     * Sets a new value for the progress.
     *
     * @return this instance, for method chaining.
     */
    public UploadProgressBar value(Integer value) {
        setDefaultModelObject(value);
        return this;
    }

    /**
     * Returns the current value of the progress.
     *
     * @return the current value of the progress.
     */
    public Integer value() {
        return Math.max(Math.min((Integer) getDefaultModelObject(), ProgressBar.MAX), ProgressBar.MIN);
    }

    @Override
    protected void onComponentTag(ComponentTag tag) {
        super.onComponentTag(tag);

        ProgressBar.internalOnComponentTag(tag, active(), striped());
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        // monkey patches the JavaScript provided by super.renderHead(response)
        response.render(JavaScriptHeaderItem.forReference(new UploadProgressBarJavaScriptReference()));
    }
}
