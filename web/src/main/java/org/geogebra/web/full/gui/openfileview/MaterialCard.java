package org.geogebra.web.full.gui.openfileview;

import java.util.List;

import org.geogebra.common.move.ggtapi.GroupIdentifier;
import org.geogebra.common.move.ggtapi.models.Material;
import org.geogebra.web.full.css.MaterialDesignResources;
import org.geogebra.web.full.gui.CardInfoPanel;
import org.geogebra.web.full.gui.browser.MaterialCardController;
import org.geogebra.web.html5.gui.util.LayoutUtilW;
import org.geogebra.web.html5.gui.util.NoDragImage;
import org.geogebra.web.html5.main.AppW;
import org.geogebra.web.shared.components.dialog.ComponentDialog;
import org.geogebra.web.shared.components.dialog.DialogData;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

/**
 * Material card
 */
public class MaterialCard extends FlowPanel implements MaterialCardI {
	private AppW app;
	// image of material
	private FlowPanel imgPanel;
	// material information
	private CardInfoPanel infoPanel;
	private ContextMenuButtonMaterialCard moreBtn;
	private FlowPanel visibilityPanel;
	private MaterialCardController controller;

	/**
	 * @param m
	 *            material
	 * @param app
	 *            see {@link AppW}
	 */
	public MaterialCard(final Material m, final AppW app) {
		this.app = app;
		controller = new MaterialCardController(app);
		controller.setMaterial(m);
		initGui();
		this.addDomHandler(event -> openMaterial(), ClickEvent.getType());
	}

	/**
	 * Open this material.
	 */
	protected void openMaterial() {
		app.getGuiManager().getBrowseView().closeAndSave(obj -> controller.loadOnlineFile());
	}

	private void initGui() {
		this.setStyleName("materialCard");
		// panel containing the preview image of material
		imgPanel = new MaterialImagePanel(getMaterial());
		this.add(imgPanel);
		// panel containing the info regarding the material

		moreBtn = new ContextMenuButtonMaterialCard(app, getMaterial(), this);
		// panel for visibility state
		visibilityPanel = new FlowPanel();
		visibilityPanel.setStyleName("visibilityPanel");
		updateVisibility(getMaterial().getVisibility());
		infoPanel = isOwnMaterial()
				? new CardInfoPanel(getMaterial().getTitle(), visibilityPanel)
				: new CardInfoPanel(getMaterial().getTitle(), getCardAuthor());

		infoPanel.add(moreBtn);
		this.add(infoPanel);
	}

	private boolean isOwnMaterial() {
		return app.getLoginOperation().getResourcesAPI().owns(getMaterial());
	}

	private String getCardAuthor() {
		return getMaterial().getCreator() != null
				? getMaterial().getCreator().getDisplayName()
				: "";
	}

	/**
	 * @return represented material
	 */
	Material getMaterial() {
		return controller.getMaterial();
	}

	@Override
	public void remove() {
		removeFromParent();
	}

	/**
	 * Actually delete the file.
	 */
	protected void onConfirmDelete() {
		controller.onConfirmDelete(this);
	}

	/**
	 * Change name on card and rename via API
	 * @param text new name
	 */
	public void rename(String text) {
		String oldTitle = infoPanel.getCardId();
		infoPanel.setCardId(text);
		controller.rename(text, this, oldTitle);
	}

	/**
	 * Call API to copy yhe material.
	 */
	public void copy() {
		controller.copy();
	}

	@Override
	public void onDelete() {
		DialogData data = new DialogData(null, "Cancel", "Delete");
		ComponentDialog removeDialog = new RemoveDialog(app, data, this);
		removeDialog.show();
		removeDialog.setOnPositiveAction(this::onConfirmDelete);
	}

	/**
	 * @return card title
	 */
	public String getCardTitle() {
		return getMaterial().getTitle();
	}

	/**
	 * @param visibility
	 *            material visibility
	 */
	public void updateVisibility(String visibility) {
		NoDragImage visibiltyImg = new NoDragImage(
				MaterialDesignResources.INSTANCE.mow_card_public(), 24);
		Label visibilityTxt = new Label(
				app.getLocalization().getMenu("Public"));
		switch (visibility) {
		case "P":
			app.getLoginOperation().getResourcesAPI()
					.getGroups(getMaterial().getSharingKeyOrId(), null,
							this::showSharedIcon);
			visibiltyImg = new NoDragImage(
					MaterialDesignResources.INSTANCE.mow_card_private(), 24);
			visibilityTxt = new Label(app.getLocalization().getMenu("Private"));
			break;
		case "S":
			visibiltyImg = new NoDragImage(
					MaterialDesignResources.INSTANCE.mow_card_shared(), 24);
			visibilityTxt = new Label(app.getLocalization().getMenu("Shared"));
			break;
		case "O":
			visibiltyImg = new NoDragImage(
					MaterialDesignResources.INSTANCE.mow_card_public(), 24);
			visibilityTxt = new Label(app.getLocalization().getMenu("Public"));
			break;
		default:
			break;
		}
		visibilityPanel.clear();
		visibilityPanel
				.add(LayoutUtilW.panelRow(visibiltyImg, visibilityTxt));
	}

	private void showSharedIcon(List<GroupIdentifier> strings) {
		if (strings != null && !strings.isEmpty()) {
			updateVisibility("S");
		}
	}

	public void setLabels() {
		updateVisibility(getMaterial().getVisibility());
	}
}