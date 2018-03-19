package org.geogebra.web.html5.main;

import com.google.gwt.core.client.JavaScriptObject;

public class JsEval {
	public static native void evalScriptNative(String script) /*-{
		$wnd.eval(script);
	}-*/;

	public static native void callNativeJavaScript(String funcname) /*-{
		if ($wnd[funcname]) {
			$wnd[funcname]();
		}
	}-*/;

	public static native void callNativeJavaScript(String funcname,
			String arg) /*-{
		if ($wnd[funcname]) {
			$wnd[funcname](arg);
		}
	}-*/;

	public static native void callNativeJavaScriptMultiArg(String funcname,
			JavaScriptObject arg) /*-{
		if ($wnd[funcname]) {
			$wnd[funcname](arg);
		}
	}-*/;

	public static native void callNativeJavaScriptMultiArg(String funcname,
			String arg0, String arg1) /*-{
		if ($wnd[funcname]) {
			$wnd[funcname](arg0, arg1);
		}
	}-*/;

	public static native void runCallback(JavaScriptObject onLoadCallback,
			JavaScriptObject ref) /*-{
		if (typeof onLoadCallback === "function") {
			onLoadCallback(ref);
		}
	}-*/;

	public static void callAppletJavaScript(String fun, Object arg0,
			Object arg1) {
		if (arg0 == null && arg1 == null) {
			JsEval.callNativeJavaScript(fun);
		} else if (arg0 != null && arg1 == null) {
			// Log.debug("calling function: " + fun + "(" + arg0.toString()
			// + ")");
			JsEval.callNativeJavaScript(fun, arg0.toString());
		} else if (arg0 != null && arg1 != null) {
			JsEval.callNativeJavaScriptMultiArg(fun, arg0.toString(),
					arg1.toString());
		}

	}
}
