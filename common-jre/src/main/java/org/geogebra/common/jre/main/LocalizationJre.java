package org.geogebra.common.jre.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import org.geogebra.common.main.App;
import org.geogebra.common.main.Feature;
import org.geogebra.common.main.Localization;
import org.geogebra.common.util.StringUtil;
import org.geogebra.common.util.lang.Language;

/**
 * common jre localization
 */
public abstract class LocalizationJre extends Localization {

	private ResourceBundle rbmenu;
	private ResourceBundle rbmenuTT;
	private ResourceBundle rbcommand;
	private ResourceBundle rbcommandOld;
	private ResourceBundle rberror;
	private ResourceBundle rbcolors;
	private ResourceBundle rbsymbol;

	private Locale tooltipLocale = null;
	/** application */
	protected App app;
	private boolean tooltipFlag = false;
	// supported GUI languages (from properties files)
	protected ArrayList<Locale> supportedLocales = null;

	protected Locale currentLocale = Locale.ENGLISH;

	/**
	 * @param dimension 3 for 3D
	 */
	public LocalizationJre(int dimension) {
		this(dimension, 15);
	}

	/**
	 * @param dimension 3 for 3D, 2 otherwise
	 * @param maxFigures maximum digits
	 */
	public LocalizationJre(int dimension, int maxFigures) {
		super(dimension, maxFigures);
	}

	/**
	 * @param app application
	 */
	final public void setApp(App app) {
		this.app = app;
	}

	public Locale getLocale() {
		return currentLocale;
	}

	@Override
	final public void setTooltipFlag() {
		if (tooltipLocale != null) {
			tooltipFlag = true;
		}
	}

	/**
	 * Stop forcing usage of tooltip locale for translations
	 */
	@Override
	final public void clearTooltipFlag() {
		tooltipFlag = false;
	}

	@Override
	final public String getCommand(String key) {

		app.initTranslatedCommands();

		try {
			return rbcommand.getString(key);
		} catch (Exception e) {
			return key;
		}
	}

	@Override
	final public String getMenu(String key) {
		if (key == null) {
			return "";
		}

		if (tooltipFlag) {
			return getMenuTooltip(key);
		}

		if (rbmenu == null) {
			rbmenu = createBundle(getMenuRessourcePath(), currentLocale);
		}

		try {
			return rbmenu.getString(key);
		} catch (Exception e) {
			return key;
		}
	}

	/**
	 * @param key key name
	 * @param locale locale
	 * @return bundle for key & locale
	 */
	abstract protected ResourceBundle createBundle(String key, Locale locale);

	/** @return path of Menu bundle */
	abstract protected String getMenuRessourcePath();

	/** @return path of Command bundle */
	abstract protected String getCommandRessourcePath();

	/** @return path of Color bundle */
	abstract protected String getColorRessourcePath();

	/** @return path of Error bundle */
	abstract protected String getErrorRessourcePath();

	/** @return path of Symbol bundle */
	abstract protected String getSymbolRessourcePath();

	@Override
	final public String getMenuTooltip(String key) {

		if (tooltipLocale == null) {
			return getMenu(key);
		}

		if (rbmenuTT == null) {
			rbmenuTT = createBundle(getMenuRessourcePath(), tooltipLocale);
		}

		try {
			return rbmenuTT.getString(key);
		} catch (Exception e) {
			return key;
		}
	}

	@Override
	final public String getError(String key) {
		if (rberror == null) {
			rberror = createBundle(getErrorRessourcePath(), currentLocale);
		}

		try {
			return rberror.getString(key);
		} catch (Exception e) {
			return key;
		}
	}

	@Override
	final public String getSymbol(int key) {
		if (rbsymbol == null) {
			initSymbolResourceBundle();
		}

		String ret = null;

		try {
			ret = rbsymbol.getString("S." + key);
		} catch (Exception e) {
			// do nothing
		}

		if ("".equals(ret)) {
			return null;
		}
		return ret;
	}

	@Override
	final public String getLanguage() {
		return getLocale().getLanguage();
	}

	@Override
	final public String getLocaleStr() {
		return getLocale().toString();
	}

	@Override
	public String getLanguageTag() {
		return getLocale().toLanguageTag();
	}

	@Override
	final public String getSymbolTooltip(int key) {
		if (rbsymbol == null) {
			initSymbolResourceBundle();
		}

		String ret = null;

		try {
			ret = rbsymbol.getString("T." + key);
		} catch (Exception e) {
			// do nothing
		}

		if ("".equals(ret)) {
			return null;
		}
		return ret;
	}

	private void initSymbolResourceBundle() {
		rbsymbol = createBundle(getSymbolRessourcePath(), currentLocale);
	}

	@Override
	final public void initCommand() {
		if (rbcommand == null) {
			rbcommand = createBundle(getCommandRessourcePath(), getCommandLocale());
		}

	}

	private void initColorsResourceBundle() {
		rbcolors = createBundle(getColorRessourcePath(), currentLocale);
	}

	final protected void updateResourceBundles() {
		if (rbmenu != null) {
			rbmenu = createBundle(getMenuRessourcePath(), currentLocale);
		}
		if (rberror != null) {
			rberror = createBundle(getErrorRessourcePath(), currentLocale);
		}

		if (rbcommand != null) {
			rbcommand = createBundle(getCommandRessourcePath(), getCommandLocale());
		}
		if (rbcolors != null) {
			rbcolors = createBundle(getColorRessourcePath(), currentLocale);
		}
		if (rbsymbol != null) {
			rbsymbol = createBundle(getSymbolRessourcePath(), currentLocale);
		}
	}

	/**
	 * @return whether properties bundles were initiated (at least plain)
	 */
	final public boolean propertiesFilesPresent() {
		return rbmenu != null;
	}

	/**
	 * @param s language for tooltips
	 * @return success
	 */
	final public boolean setTooltipLanguage(String s) {
		Locale locale = null;

		for (int i = 0; i < getSupportedLocales().size(); i++) {
			if (getSupportedLocales().get(i).toString().equals(s)) {
				locale = getSupportedLocales().get(i);
				break;
			}
		}

		boolean updateNeeded = rbmenuTT != null;

		rbmenuTT = null;

		if (locale == null) {
			tooltipLocale = null;
		} else if (currentLocale.toString().equals(locale.toString())) {
			tooltipLocale = null;
		} else {
			tooltipLocale = locale;
		}
		return updateNeeded;
	}

	/**
	 * @return tootlip loacle
	 */
	final public Locale getTooltipLocale() {
		return tooltipLocale;
	}

	@Override
	final public String getTooltipLanguageString() {
		if (tooltipLocale == null) {
			return null;
		}
		return tooltipLocale.toString();
	}

	@Override
	final public String getColor(String key) {

		if (key == null) {
			return "";
		}

		if ((key.length() == 5)
				&& StringUtil.toLowerCaseUS(key).startsWith("gray")) {
			return StringUtil.getGrayString(key.charAt(4), this);
		}

		if (rbcolors == null) {
			initColorsResourceBundle();
		}

		try {
			return rbcolors.getString(StringUtil.toLowerCaseUS(key));
		} catch (Exception e) {
			return key;
		}
	}

	@Override
	final public String reverseGetColor(String locColor) {
		String str = StringUtil.removeSpaces(StringUtil.toLowerCaseUS(locColor));
		if (rbcolors == null) {
			initColorsResourceBundle();
		}

		try {

			Enumeration<String> enumer = rbcolors.getKeys();
			while (enumer.hasMoreElements()) {
				String key = enumer.nextElement();
				if (str.equals(StringUtil.removeSpaces(
						StringUtil.toLowerCaseUS(rbcolors.getString(key))))) {
					return key;
				}
			}

			return str;
		} catch (Exception e) {
			return str;
		}
	}

	/**
	 * @return list of suported locales
	 */
	protected ArrayList<Locale> getSupportedLocales() {
		return getSupportedLocales(app.has(Feature.ALL_LANGUAGES));
	}

	private ArrayList<Locale> buildSupportedLocales(boolean prerelease) {
		Language[] languages = getSupportedLanguages(prerelease);
		Locale[] locales = getLocales(languages);
		List<Locale> localeList = Arrays.asList(locales);

		return new ArrayList<>(localeList);
	}

	/**
	 * Returns the supported locales.
	 * @param prerelease if the app is in prerelease
	 * @return locales that the app can handle
	 */
	public ArrayList<Locale> getSupportedLocales(boolean prerelease) {
		if (supportedLocales == null) {
			supportedLocales = buildSupportedLocales(prerelease);
		}
		return supportedLocales;
	}

	@Override
	final protected boolean isCommandChanged() {
		return rbcommandOld != rbcommand;
	}

	@Override
	final protected void setCommandChanged(boolean b) {
		rbcommandOld = rbcommand;

	}

	@Override
	final protected boolean isCommandNull() {
		return rbcommand == null;
	}

	final protected String getLanguage(Locale locale) {
		return locale.getLanguage();
	}

	final protected String getCountry(Locale locale) {
		return locale.getCountry();
	}

	protected String getVariant(Locale locale) {
		return locale.getVariant();
	}

	/**
	 * @param locale current locale
	 */
	public void setLocale(Locale locale) {
		currentLocale = getClosestSupportedLocale(locale);
		updateResourceBundles();
	}

	/**
	 * @return locale for command translation
	 */
	protected Locale getCommandLocale() {
		Language language = Language.getLanguage(getLanguage());
		if (areEnglishCommandsForced() || (language != null && !language.hasTranslatedKeyboard())) {
			return Locale.ENGLISH;
		}
		return currentLocale;
	}

	/**
	 * Returns a locale that is supported and is closest to the query locale.
	 * If present, it will return the perfect match.
	 * Else, it looks for the closest, more general locale (for example it returns 'en'
	 * for the query 'en-CA').
	 * Else, it looks for the closest, more specific locale (for example it returns 'zh-CN'
	 * for the query 'zh' out of the supported locales ['zh-Hant-TW', 'zh-CN', 'zh-TW']).
	 * Else, it looks for a matching language.
	 * Else, it returns the default locale (English).
	 * @param query query locale
	 * @return closest supported locale
	 */
	protected Locale getClosestSupportedLocale(Locale query) {
		Set<String> subtags = convertToSubtagSet(query);

		Locale match = null;
		int generalScore = 0;
		int specificScore = Integer.MAX_VALUE;
		for (Locale locale : getSupportedLocales()) {
			if (locale.toLanguageTag().equals(query.toLanguageTag())) {
				// A perfect match found, return early
				return locale;
			} else if (locale.getLanguage().equals(query.getLanguage())) {
				// The language matches
				Set<String> supportedLocaleSubtags = convertToSubtagSet(locale);
				if (subtags.containsAll(supportedLocaleSubtags)
						&& supportedLocaleSubtags.size() > generalScore) {
					// A closer, more general match found
					match = locale;
					generalScore = supportedLocaleSubtags.size();
				} else if (generalScore == 0 && supportedLocaleSubtags.containsAll(subtags)
						&& supportedLocaleSubtags.size() < specificScore) {
					// A closer more specific match found, and no general match found
					specificScore = supportedLocaleSubtags.size();
					match = locale;
				} else if (match == null) {
					// Store a match to the language if there is none yet.
					match = locale;
				}
			}
		}
		if (match != null) {
			return match;
		}

		return Locale.ENGLISH;
	}

	private static Set<String> convertToSubtagSet(Locale locale) {
		return new HashSet<>(Arrays.asList(locale.toLanguageTag().split("-")));
	}

	/**
	 * Converts the language to a locale object.
	 * @param language the language to convert to.
	 * @return converted locale
	 */
	public Locale convertToLocale(Language language) {
		return Locale.forLanguageTag(language.toLanguageTag());
	}

	/**
	 * Get an array of locales from languages.
	 * @param languages array of languages
	 * @return an array of locales
	 */
	@Override
	public Locale[] getLocales(Language[] languages) {
		Locale[] locales = new Locale[languages.length];
		for (int i = 0; i < languages.length; i++) {
			Language language = languages[i];
			locales[i] = convertToLocale(language);
		}
		return locales;
	}

}
