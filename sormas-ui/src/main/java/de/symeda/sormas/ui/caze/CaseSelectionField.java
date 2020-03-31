package de.symeda.sormas.ui.caze;

import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.caze.CaseCriteria;
import de.symeda.sormas.api.caze.CaseIndexDto;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.ui.utils.CssStyles;

@SuppressWarnings("serial")
public class CaseSelectionField extends CustomField<CaseIndexDto> {

	protected CaseCriteria criteria;
	protected CaseSelectionGrid grid;
	protected Consumer<Boolean> selectionChangeCallback;
	protected VerticalLayout mainLayout;

	public CaseSelectionField(CaseCriteria criteria) {
		this.criteria = criteria;

		mainLayout = new VerticalLayout();
		mainLayout.setSpacing(true);
		mainLayout.setSizeUndefined();
		mainLayout.setWidth(100, Unit.PERCENTAGE);
	}

	private void addInfoComponent() {
		HorizontalLayout infoLayout = new HorizontalLayout();
		infoLayout.setWidth(100, Unit.PERCENTAGE);
		infoLayout.setSpacing(true);
		Image icon = new Image(null, new ThemeResource("img/info-icon.png"));
		icon.setHeight(35, Unit.PIXELS);
		icon.setWidth(35, Unit.PIXELS);
		infoLayout.addComponent(icon);
		Label infoLabel = new Label(I18nProperties.getString(Strings.infoSearchCaseForContact), ContentMode.HTML);
		infoLabel.setWidth(100, Unit.PERCENTAGE);
		infoLayout.addComponent(infoLabel);
		infoLayout.setExpandRatio(infoLabel, 1);
		CssStyles.style(infoLayout, CssStyles.VSPACE_3);
		mainLayout.addComponent(infoLayout);
	}

	private void addFilterComponent() {
		HorizontalLayout filterLayout = new HorizontalLayout();
		filterLayout.setSpacing(true);

		TextField searchField = new TextField();
		searchField.setPlaceholder(I18nProperties.getString(Strings.promptSearch));
		searchField.setWidth(400, Unit.PIXELS);
		filterLayout.addComponent(searchField);

		Button searchButton = new Button(I18nProperties.getCaption(Captions.caseSearchCase));
		CssStyles.style(searchButton, ValoTheme.BUTTON_PRIMARY);
		searchButton.addClickListener(e -> {
			if (StringUtils.isNotEmpty(searchField.getValue())) {
				criteria.setSourceCaseInfoLike(searchField.getValue());
				grid.setCases(FacadeProvider.getCaseFacade().getIndexList(criteria, null, null, null));
			} else {
				criteria.setSourceCaseInfoLike(null);
				grid.clearCases();
			}
			grid.reload();
		});
		filterLayout.addComponent(searchButton);

		mainLayout.addComponent(filterLayout);
	}

	private void addGrid() {
		grid = new CaseSelectionGrid(null);
		grid.addSelectionListener(e -> {
			if (selectionChangeCallback != null) {
				selectionChangeCallback.accept(!e.getSelected().isEmpty());
			}
		});
		mainLayout.addComponent(grid);
	}

	@Override
	public CaseIndexDto getValue() {
		return (CaseIndexDto) grid.getSelectedRow();
	}

	@Override
	protected Component initContent() {
		addInfoComponent();
		addFilterComponent();
		addGrid();

		return mainLayout;
	}

	@Override
	protected void doSetValue(CaseIndexDto value) {
		super.setValue(value);
		grid.select(value);
	}

	public void setSelectionChangeCallback(Consumer<Boolean> callback) {
		this.selectionChangeCallback = callback;
	}

}
