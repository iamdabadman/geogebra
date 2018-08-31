package org.geogebra.common.euclidian;

import org.geogebra.common.awt.GFont;
import org.geogebra.common.awt.GRectangle;
import org.geogebra.common.kernel.geos.GeoText;
import org.geogebra.common.kernel.kernelND.GeoPointND;

/**
 * Handling text editor in Euclidian View.
 * 
 * @author laszlo
 *
 */
public interface TextController {

	/**
	 * Creates in-place editable GeoText
	 * 
	 * @param loc
	 *            Text location.
	 * @param rw
	 *            specifies if RealWorld coordinates are used.
	 * @return the created GeoText object.
	 */
	GeoText createText(GeoPointND loc, boolean rw);

	/**
	 * Edit text
	 * 
	 * @param geo
	 *            to edit
	 */
	void edit(GeoText geo);

	/**
	 * 
	 * @return the bounding rectangle of the current editor.
	 */
	public GRectangle getEditorBounds();

	/**
	 * Sets editor font.
	 * 
	 * @param font
	 *            to set.
	 */
	void setEditorFont(GFont font);
}
