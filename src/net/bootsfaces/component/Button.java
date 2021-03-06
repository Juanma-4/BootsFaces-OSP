/**
 *  Copyright 2014 Riccardo Massera (TheCoder4.Eu)
 *  
 *  This file is part of BootsFaces.
 *  
 *  BootsFaces is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  BootsFaces is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with BootsFaces. If not, see <http://www.gnu.org/licenses/>.
 */

package net.bootsfaces.component;

import static net.bootsfaces.C.BSFCOMPONENT;
import static net.bootsfaces.C.BUTTON_COMPONENT_TYPE;
import static net.bootsfaces.C.SP;
import static net.bootsfaces.C.W_NONAVCASE_BUTTON;
import static net.bootsfaces.render.A.ALLBUTTON_ATTRS;
import static net.bootsfaces.render.A.CLICK;
import static net.bootsfaces.render.A.DATA_DISMISS;
import static net.bootsfaces.render.A.DISABLED;
import static net.bootsfaces.render.A.DISMISS;
import static net.bootsfaces.render.A.FRAGMENT;
import static net.bootsfaces.render.A.ICON;
import static net.bootsfaces.render.A.ICONAWESOME;
import static net.bootsfaces.render.A.ICON_ALIGN;
import static net.bootsfaces.render.A.LOOK;
import static net.bootsfaces.render.A.RIGHT;
import static net.bootsfaces.render.A.SIZE;
import static net.bootsfaces.render.A.VALUE;
import static net.bootsfaces.render.A.asString;
import static net.bootsfaces.render.A.toBool;
import static net.bootsfaces.render.H.BUTTON;
import static net.bootsfaces.render.H.CLASS;
import static net.bootsfaces.render.H.ID;
import static net.bootsfaces.render.H.NAME;
import static net.bootsfaces.render.H.STYLE;
import static net.bootsfaces.render.H.STYLECLASS;
import static net.bootsfaces.render.H.TYPE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.NavigationCase;
import javax.faces.application.ProjectStage;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.component.html.HtmlOutcomeTargetButton;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import net.bootsfaces.C;
import net.bootsfaces.listeners.AddResourcesListener;
import net.bootsfaces.render.R;
import net.bootsfaces.render.Tooltip;

/**
 * This components represents and renders a button without AJAX functionality.
 * 
 * @author thecoder4.eu
 */
@ResourceDependencies({ @ResourceDependency(library = "bsf", name = "css/core.css", target = "head"),
	@ResourceDependency(library = "bsf", name = "css/tooltip.css", target = "head")
		 })
@FacesComponent(BUTTON_COMPONENT_TYPE)
public class Button extends HtmlOutcomeTargetButton {

	/**
	 * <p>
	 * The standard component type for this component.
	 * </p>
	 */
	public static final String COMPONENT_TYPE = BUTTON_COMPONENT_TYPE;
	/**
	 * <p>
	 * The component family for this component.
	 * </p>
	 */
	public static final String COMPONENT_FAMILY = BSFCOMPONENT;

	public Button() {
		setRendererType(null); // this component renders itself
		AddResourcesListener.addResourceToHeadButAfterJQuery(C.BSF_LIBRARY, "jq/jquery.js");
		Tooltip.addResourceFile();

	}

	/** 
	 * Renders the button. <br>
	 * General layout of the generated HTML code:<br>
	 * &lt;button class="btn btn-large" href="#"%gt;&lt;i class="icon-star"&gt;&lt;/i&gt; Star&lt;/button&gt;
	 * 
	 * @param context the current FacesContext
	 * @throws IOException thrown if something's wrong with the ResponseWriter
	 */
	@Override
	public void encodeEnd(FacesContext context) throws IOException {
        if (!isRendered()) {
            return;
        }

		encodeHTML(context, getAttributes());
		Tooltip.activateTooltips(context, getAttributes());
	}

	/** 
	 * Encode the HTML code of the button.
	 * 
	 * @param context the current FacesContext
	 * @param attrs the attribute list
	 * @throws IOException thrown if something's wrong with the ResponseWriter
	 */
	public void encodeHTML(FacesContext context, Map<String, Object> attrs) throws IOException {
		ResponseWriter rw = context.getResponseWriter();

		Object value = attrs.get(VALUE);
                String style=asString(attrs.get(STYLE));
                
		rw.startElement(BUTTON, this);
		rw.writeAttribute(ID, getClientId(context), ID);
		rw.writeAttribute(NAME, getClientId(context), NAME);
		rw.writeAttribute(TYPE, BUTTON, null);
                if(style!=null) { rw.writeAttribute(STYLE,style,STYLE); }
		rw.writeAttribute(CLASS, getStyleClasses(attrs), CLASS);
		
		Tooltip.generateTooltip(context, attrs, rw);

		
		final String clickHandler = encodeClick(context, attrs);
		if (null != clickHandler && clickHandler.length()>0) {
			rw.writeAttribute(CLICK, clickHandler, null);
		}
		String d = asString(attrs.get(DISMISS));
		if (d != null) {
			rw.writeAttribute(DATA_DISMISS, d, null);
		}
		boolean disabled = (toBool(attrs.get(DISABLED)));
		if (disabled) {
			rw.writeAttribute(DISABLED, DISABLED, null);
		}

		// Encode attributes (HTML 4 pass-through + DHTML)
		renderPassThruAttributes(context, this, ALLBUTTON_ATTRS);

		String icon = asString(attrs.get(ICON));
                String faicon = asString(attrs.get(ICONAWESOME));
                boolean fa=false; //flag to indicate wether the selected icon set is Font Awesome or not.
                if(faicon != null) { icon=faicon; fa=true; }
		if (icon != null) {
			Object ialign = attrs.get(ICON_ALIGN); // Default Left
			//!//boolean white=null!=attrs.get(LOOK);
			if (ialign != null && ialign.equals(RIGHT)) {
				rw.writeText(value + SP, null);
				R.encodeIcon(rw, this, icon, fa);
                                //!//R.encodeIcon(rw, this, icon, white);
			} else {
				R.encodeIcon(rw, this, icon, fa);
                                //!//R.encodeIcon(rw, this, icon, white);
				rw.writeText(SP + value, null);
			}

		} else {
			rw.writeText(value, null);
		}

		rw.endElement(BUTTON);
	}


	/**
	 * Renders the Javascript code dealing with the click event.
	 * If the developer provides their own onclick handler, is precedes the generated Javascript code.
	 * @param context The current FacesContext.
	 * @param attrs the attribute list
	 * @return some Javascript code, such as "window.location.href='/targetView.jsf';"
	 */
	private String encodeClick(FacesContext context, Map<String, Object> attrs) {
		String js;
		String userClick = getOnclick();
		if (userClick != null) {
			js = userClick;
		} // +COLON; }
		else {
			js = "";
		}
		
		String fragment = asString(attrs.get(FRAGMENT));
		String outcome = getOutcome();
		
		if (canOutcomeBeRendered(attrs, fragment, outcome)) {
			outcome = (outcome == null) ? context.getViewRoot().getViewId() : outcome;
			
			String url = determineTargetURL(context, outcome);
	
			if (url != null) {
				if (fragment != null) {
					url += "#" + fragment;
				}
				js += "window.location.href='" + url + "';";
			}
		}

		return js;
	}

	/**
	 * Do we have to suppress the target URL? 
	 * @param attrs the component's attribute list
	 * @param fragment the fragment of the URL behind the hash (outcome#fragment)
	 * @param outcome the value of the outcome attribute
	 * @return true if the outcome can be rendered.
	 */
	private boolean canOutcomeBeRendered(Map<String, Object> attrs, String fragment, String outcome) {
		boolean renderOutcome=true;
		if (null==outcome && attrs.containsKey("ng-click")) {
			String ngClick=asString(attrs.get("ng-click"));
			if (null!=ngClick && (ngClick.length()>0)) {
				if (fragment==null) {
					renderOutcome=false;
				}
			}
		}
		return renderOutcome;
	}

	/**
	 * Translate the outcome attribute value to the target URL. 
	 * @param context the current FacesContext
	 * @param outcome the value of the outcome attribute
	 * @return the target URL of the navigation rule (or the outcome if there's not navigation rule)
	 */
	private String determineTargetURL(FacesContext context, String outcome) {
		ConfigurableNavigationHandler cnh = (ConfigurableNavigationHandler) context.getApplication().getNavigationHandler();
		NavigationCase navCase = cnh.getNavigationCase(context, null, outcome);
		/*
		 * Param Name: javax.faces.PROJECT_STAGE Default Value: The default value is ProjectStage#Production but IDE can set it differently
		 * in web.xml Expected Values: Development, Production, SystemTest, UnitTest Since: 2.0
		 * 
		 * If we cannot get an outcome we use an Alert to give a feedback to the Developer if this build is in the Development Stage
		 */
		if (navCase == null) {
			if (FacesContext.getCurrentInstance().getApplication().getProjectStage().equals(ProjectStage.Development)) {
				return "alert('WARNING! " + W_NONAVCASE_BUTTON + "');";
			} else {
				return "";
			}
		} // throw new FacesException("The outcome '"+outcome+"' cannot be resolved."); }
		String vId = navCase.getToViewId(context);

		Map<String, List<String>> params = getParams(navCase, this);
		String url;
		url = context.getApplication().getViewHandler()
				.getBookmarkableURL(context, vId, params, isIncludeViewParams() || navCase.isIncludeViewParams());
		return url;
	}

	/**
	 * Find all parameters to include by looking at nested uiparams and params of navigation case
	 */
	protected static Map<String, List<String>> getParams(NavigationCase navCase, Button button) {
		Map<String, List<String>> params = new LinkedHashMap<String, List<String>>();

		// UIParams
		for (UIComponent child : button.getChildren()) {
			if (child.isRendered() && (child instanceof UIParameter)) {
				UIParameter uiParam = (UIParameter) child;

				if (!uiParam.isDisable()) {
					List<String> paramValues = params.get(uiParam.getName());
					if (paramValues == null) {
						paramValues = new ArrayList<String>();
						params.put(uiParam.getName(), paramValues);
					}

					paramValues.add(String.valueOf(uiParam.getValue()));
				}
			}
		}

		// NavCase Params
		Map<String, List<String>> navCaseParams = navCase.getParameters();
		if (navCaseParams != null && !navCaseParams.isEmpty()) {
			for (Map.Entry<String, List<String>> entry : navCaseParams.entrySet()) {
				String key = entry.getKey();

				// UIParams take precedence
				if (!params.containsKey(key)) {
					params.put(key, entry.getValue());
				}
			}
		}

		return params;
	}

	/**
	 * Collects the CSS classes of the button.
	 * @param attrs the attribute list.
	 * @return the CSS classes (separated by a space).
	 */
	private static String getStyleClasses(Map<String, Object> attrs) {
		StringBuilder sb;
		sb = new StringBuilder(40); // optimize int
		sb.append("btn");
		String size = asString(attrs.get(SIZE));
		if (size != null) {
			sb.append(" btn-").append(size);
		}

		String look = asString(attrs.get(LOOK));
		if (look != null) {
			sb.append(" btn-").append(look);
		} else {
			sb.append(" btn-default");
		}

		if (toBool(attrs.get(DISABLED))) {
			sb.append(SP + DISABLED);
		}
		// TODO add styleClass and class support
		String sclass = asString(attrs.get(STYLECLASS));
		if (sclass != null) {
			sb.append(" ").append(sclass);
		}

		return sb.toString().trim();
	}

    /**
     * <p>Return the identifier of the component family to which this
     * component belongs.  This identifier, in conjunction with the value
     * of the <code>rendererType</code> property, may be used to select
     * the appropriate {@link Renderer} for this component instance.</p>
     */
	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	/**
	 * Method temporarily copied from CoreRenderer. Should be replaced by a call of CoreRenderer in the long run.
	 * @param context the current FacesContext
	 * @param component the current component
	 * @param attrs the component's attribute list
	 * @throws IOException thrown if something's wrong with the response writer.
	 */
	
    protected void renderPassThruAttributes(FacesContext context, UIComponent component, String[] attrs) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        //pre-defined attributes
        if(attrs != null && attrs.length > 0) {
            for(String attribute : attrs) {
                Object value = component.getAttributes().get(attribute);

                if(shouldRenderAttribute(value))
                    writer.writeAttribute(attribute, value.toString(), attribute);
            }
        }
    }

    /**
     * Detects whether an attribute is a default value or not.
     * Method temporarily copied from CoreRenderer. Should be replaced by a call of CoreRenderer in the long run.
	 * 
     * @param value the value to be checked
     * @return true if the value is not the default value
     */
    protected boolean shouldRenderAttribute(Object value) {
        if(value == null)
            return false;

        if(value instanceof Boolean) {
            return ((Boolean) value).booleanValue();
        }
        else if(value instanceof Number) {
            Number number = (Number) value;

            if (value instanceof Integer)
                return number.intValue() != Integer.MIN_VALUE;
            else if (value instanceof Double)
                return number.doubleValue() != Double.MIN_VALUE;
            else if (value instanceof Long)
                return number.longValue() != Long.MIN_VALUE;
            else if (value instanceof Byte)
                return number.byteValue() != Byte.MIN_VALUE;
            else if (value instanceof Float)
                return number.floatValue() != Float.MIN_VALUE;
            else if (value instanceof Short)
                return number.shortValue() != Short.MIN_VALUE;
        }

        return true;
    }

}
