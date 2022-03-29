package org.geogebra.common.io;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.himamis.retex.editor.share.editor.SyntaxHint;
import com.himamis.retex.editor.share.input.KeyboardInputAdapter;
import com.himamis.retex.editor.share.meta.MetaModel;
import com.himamis.retex.renderer.share.platform.FactoryProvider;

public class SyntaxHintCheck {

	private static MathFieldCommon mfc;

	/**
	 * Reset LaTeX factory
	 */
	@BeforeClass
	public static void prepare() {
		if (FactoryProvider.getInstance() == null) {
			FactoryProvider.setInstance(new FactoryProviderCommon());
		}
	}

	@Before
	public void setUp() {
		mfc = new MathFieldCommon(new MetaModel(), null);
	}

	@Test
	public void readPlaceholdersInitial() {
		KeyboardInputAdapter.onKeyboardInput(mfc.getInternal(), "FitPoly(<Points>, <Degree>)");
		SyntaxHint hint = mfc.getInternal().getSyntaxHint();
		assertEquals("FitPoly(", hint.getPrefix());
		assertEquals("Points", hint.getActive());
		assertEquals(", Degree)", hint.getSuffix());
	}

	@Test
	public void readPlaceholdersAfterComma() {
		KeyboardInputAdapter.onKeyboardInput(mfc.getInternal(), "FitPoly(<Points>, <Degree>)");
		EditorTyper typer = new EditorTyper(mfc);
		typer.type("{(1,1)},");
		SyntaxHint hint = mfc.getInternal().getSyntaxHint();
		assertEquals("FitPoly(Points, ", hint.getPrefix());
		assertEquals("Degree", hint.getActive());
		assertEquals(")", hint.getSuffix());
	}
}
