package org.geogebra.common.properties.impl.objects;

import java.util.Arrays;
import java.util.List;

import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.kernelND.GeoElementND;
import org.geogebra.common.main.Localization;
import org.geogebra.common.properties.impl.AbstractNamedEnumeratedProperty;
import org.geogebra.common.properties.impl.objects.delegate.CaptionStyleDelegate;
import org.geogebra.common.properties.impl.objects.delegate.GeoElementDelegate;
import org.geogebra.common.properties.impl.objects.delegate.NotApplicablePropertyException;

/**
 * Caption style
 */
public class CaptionStyleProperty extends AbstractNamedEnumeratedProperty<Integer> {

	private static final List<Integer> labelModes = Arrays.asList(
			GeoElementND.LABEL_DEFAULT,
			GeoElementND.LABEL_NAME,
			GeoElementND.LABEL_NAME_VALUE,
			GeoElementND.LABEL_VALUE,
			GeoElementND.LABEL_CAPTION);

	private final GeoElementDelegate delegate;

	/***/
	public CaptionStyleProperty(Localization localization, GeoElement geoElement)
			throws NotApplicablePropertyException {
		super(localization, "stylebar.Caption");
		delegate = new CaptionStyleDelegate(geoElement);
		setValues(labelModes.toArray(new Integer[0]));
		setValueNames("Hidden", "Name", "NameAndValue", "Value", "Caption");
	}

	@Override
	public Integer getValue() {
		GeoElement element = delegate.getElement();
		if (!element.isLabelVisible()) {
			return labelModes.get(0);
		}
		int labelMode = element.getLabelMode();
		int index = labelModes.indexOf(labelMode);
		return index >= 0 ? labelMode : labelModes.get(1);
	}

	@Override
	protected void doSetValue(Integer value) {
		GeoElement element = delegate.getElement();
		element.setLabelMode(value);
		element.setLabelVisible(value != GeoElementND.LABEL_DEFAULT);
		element.updateRepaint();
	}

	@Override
	public boolean isEnabled() {
		return delegate.isEnabled();
	}
}
