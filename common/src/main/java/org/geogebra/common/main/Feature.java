package org.geogebra.common.main;

public enum Feature {
	ALL_LANGUAGES,

	LOCALSTORAGE_FILES,

	TUBE_BETA,

	EXERCISES,

	TOOL_EDITOR,

	POLYGON_TRIANGULATION,

	DATA_COLLECTION,

	IMPLICIT_SURFACES,

	CONTOUR_PLOT_COMMAND,

	LOG_AXES,

	HIT_PARAMETRIC_SURFACE,

	PARAMETRIC_SURFACE_IS_REGION,

	EXAM_TABLET,

	ACRA,

	ANALYTICS,

	SAVE_SETTINGS_TO_FILE,

	HANDWRITING,

	AV_DEFINITION_AND_VALUE,

	CONVEX_HULL_3D,

	/** GGB-334, TRAC-3401 */
	ADJUST_WIDGETS,

	/** GGB-944 */
	EXPORT_ANIMATED_PDF,

	/** GGB-776 */
	ABSOLUTE_TEXTS,

	/** MOB-788 */
	MOBILE_USE_FBO_FOR_3D_IMAGE_EXPORT,

	/** GGB-1263 */
	AUTOSCROLLING_SPREADSHEET,

	/** GGB-1252 */
	KEYBOARD_BEHAVIOUR,

	/** MOW */
	WHITEBOARD_APP,

	/**
	 * GGB-1398 + GGB-1529
	 */
	SHOW_ONE_KEYBOARD_BUTTON_IN_FRAME,

	/** MOW-97 */
	ERASER, ROUNDED_POLYGON,

	/** MOW-175 */
	MOW_CONTEXT_MENU,

	/** MOV-169 */
	DYNAMIC_STYLEBAR,

	/** MOW-29 */
	MOW_TOOLBAR,

	MOW_PEN_IS_LOCUS,

	MOW_PEN_EVENTS,

	/** MOW-105 */
	MOW_PEN_SMOOTHING,

	/** GGB-1617 */
	AUTOMATIC_DERIVATIVES,

	/** SolveQuartic in CAS GGB-1635 */
	SOLVE_QUARTIC,

	/** MOW-166 */
	MOW_AXES_STYLE_SUBMENU,

	/** MOW-55 */
	MOW_BOUNDING_BOXES,

	/** MOW-320 */
	MOW_PIN_IMAGE,
	
	/** MOW-239 */
	MOW_IMPROVE_CONTEXT_MENU,

	/** MOW-251, MOW-197 */
	MOW_CLEAR_VIEW_STYLEBAR,

	/** MOW-197 */
	MOW_COLORPOPUP_IMPROVEMENTS,

	/** MOW-88 */
	MOW_DIRECT_FORMULA_CONVERSION,

	/** MOW-368 */
	MOW_IMAGE_DIALOG_UNBUNDLED,

	/** GGB-1697 */
	AV_ITEM_DESIGN,

	EXPORT_SCAD_IN_MENU,

	EXPORT_COLLADA_IN_MENU,

	EXPORT_OBJ_IN_MENU,

	/** GGB-1876 */
	DOUBLE_ROUND_BRACKETS,

	/** GGB-1708 */
	INPUT_BAR_ADD_SLIDER,

	/** GGB-1916 */
	DEFAULT_OBJECT_STYLES,
	
	/** GGB-2008 */
	OBJECT_DEFAULTS_AND_COLOR,

	SHOW_STEPS,

	/** GGB-1907 */
	DYNAMIC_STYLEBAR_SELECTION_TOOL,

	/** GGB-1910 */
	LABEL_SETTING_ON_STYLEBAR,

	/** GGB-1966 */
	FUNCTIONS_DYNAMIC_STYLEBAR_POSITION,

	CENTER_STANDARD_VIEW,

	SURFACE_2D,

	/** GGB-1982 */
	OPENING_DYNAMIC_STYLEBAR_ON_FIXED_GEOS,

	/** GGB-1985*/
	FLOATING_SETTINGS,

	/** GGB-2005 */
	TOOLTIP_DESIGN,

	/** GGB-1986 */
	DIALOG_DESIGN,

	INITIAL_PORTRAIT,

	/** MOW-261 */
	MOW_COLOR_FILLING_LINE,

	/** MOW-269 */
	MOW_MULTI_PAGE,

	/** GGB-2015 */
	GEO_AV_DESCRIPTION,

	WEB_SWITCH_APP_FOR_FILE,

	/** GGB-1717 */
	IMAGE_EXPORT,

	/** GGB-2053 */
	TAB_ON_GUI,

	/** MOB-1293 */
	SELECT_TOOL_NEW_BEHAVIOUR,

	/** GGB-2118 */
	PREVIEW_POINTS,

	/** GGB-2183 change sin(15) -> sin(15deg) */
	AUTO_ADD_DEGREE,

	/** GGB-2222 change asin(0.5) -> asind(0.5) */
	CHANGE_INVERSE_TRIG_TO_DEGREES,

	/** GGB-2170 for k12 */
	SLIDER_STYLE_OPTIONS,

	/** MOB-1310 */
	SHOW_HIDE_LABEL_OBJECT_DELETE_MULTIPLE,

	/** MOB-1319 */
	MOB_NOTIFICATION_BAR_TRIGGERS_EXAM_ALERT_IOS_11,

	/** GGB-2203 */
	HELP_AND_SHORTCUTS,

	/** GGB-2203 */
	HELP_AND_SHORTCUTS_IMPROVEMENTS,

	/** GGB-2347 */
	READ_DROPDOWNS,

	/** GGB-2204 */
	TAB_ON_MENU,

	/** GGB-2215 */
	ARIA_CONTEXT_MENU,

	/** MOW-390 GGB */
	WHOLE_PAGE_DRAG,

	/** GGB-650 */
	GGB_WEB_ASSEMBLY,

	/** MOW-285 */
	MOW_BOUNDING_BOX_FOR_PEN_TOOL,

	/** GGB-2258 */
	VOICEOVER_CURSOR,

	/** GGB-2346 */
	CURRENCY_UNIT,

	/** MOW-360, MOW-381, MOW-382 */
	MOW_CROP_IMAGE,
	
	/** MOW-379, MOW-380 */
	MOW_IMAGE_BOUNDING_BOX,

	/** MOW-336 */
	MOW_DRAG_AND_DROP_PAGES,

	/** MOW-336 */
	MOW_DRAG_AND_DROP_ANIMATION,

	/** MOW-345 */
	MOW_MOVING_CANVAS,

	/** MOW-349 */
	MOW_AUDIO_TOOL,

	/** MOW-299 */
	MOW_VIDEO_TOOL,

	/** MOW-278 */
	MOW_HIGHLIGHTER_TOOL,

	/** MOW-459 */
	MOW_DOUBLE_CANVAS,

	COMMAND_HOLES, WEB_CLASSIC_FLOATING_MENU,

	/** AND-875 */
	MOB_LOAD_SAVE_FOR_PRE_LOLLIPOP,

	/** MOB-1471 */
	MOB_DEFAULT_SLIDER_INCREMENT_VALUE,

	/** IGR-604 */
	MOB_TABBED_SETTINGS_PANEL,

	/** MOB-1463 */
	MOB_DISABLE_3D_COMMANDS,

	/** AND-722 */
	MOB_MATERIAL_INPUT,

	/** GGB-2318 */
	SPLIT_INTEGRAL_IF,

	/** AND-679 */
	MOB_AND_QUEUE_ON_GL_THREAD,

	/** IGR-748 */
	MOB_EXPORT_IMAGE,

	/** IGR-601 */
	MOB_HELP_FEEDBACK,

	/** MOB-1513 */
	MOB_PACK_JOIN_POINTS,

	/** MOB-1514 */
	MOB_PACK_CONIC,

	/** MOB-1515 */
	MOB_PACK_POINTS,

	/** MOB-1537 */
	MOB_PREVIEW_WHEN_EDITING,

	/** MOB-1516 */
	MOB_PACK_ALL_CURVES,

	/** MOB-1153 */
	MOB_FIX_DONT_SWITCH_TO_AV,

	/** GGB-2366 */
	TIKZ_AXES,

	/** AND-1061 */
	MOB_EXAM_MODE_EXIT_DIALOG_NEW
}

