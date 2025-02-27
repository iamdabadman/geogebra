package org.geogebra.desktop.export.pstricks;

import org.geogebra.common.awt.GGraphics2D;
import org.geogebra.common.awt.GShape;
import org.geogebra.common.export.pstricks.GeoGebraToPgf;
import org.geogebra.common.kernel.arithmetic.FunctionalNVar;
import org.geogebra.common.kernel.arithmetic.Inequality;
import org.geogebra.common.main.App;

public class GeoGebraToPgfD extends GeoGebraToPgf {

	public GeoGebraToPgfD(App app) {
		super(app);
	}

	@Override
	protected GGraphics2D createGraphics(FunctionalNVar ef,
			Inequality inequality) {
		return new ExportGraphicsPgf(ef, inequality);
	}

	class ExportGraphicsPgf extends ExportGraphicsD {

		public ExportGraphicsPgf(FunctionalNVar geo, Inequality ineq) {

			super(geo, ineq);
		}

		@Override
		public void fill(GShape s) {

			superFill(s, ineq, geo, ds);

		}
	}
}
