package org.geogebra.common.euclidian.draw;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.stream.Collectors;

import org.geogebra.common.euclidian.BaseControllerTest;
import org.geogebra.common.kernel.geos.GeoBoolean;
import org.geogebra.common.kernel.geos.GeoText;
import org.geogebra.test.EventAcumulator;
import org.junit.Test;

public class DrawBooleanTest extends BaseControllerTest {

	@Test
	public void hitBooleanWithDynamicCaptionShouldBeOneEvent() {
		GeoBoolean check = (GeoBoolean) add("a=true");
		GeoText caption = (GeoText) add("caption=\"foo\"");
		check.setDynamicCaption(caption);
		check.setEuclidianVisible(true);
		check.setAbsoluteScreenLoc(100, 100);
		check.updateRepaint();
		EventAcumulator listener = new EventAcumulator();
		getApp().getEventDispatcher().addEventListener(listener);
		click(101, 101);
		assertEquals(Collections.singletonList("UPDATE a"), listener.getEvents().stream()
				.filter(evt -> evt.startsWith("UPDATE")).collect(
				Collectors.toList()));
	}
}
