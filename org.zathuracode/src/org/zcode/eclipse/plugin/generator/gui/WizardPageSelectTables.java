package org.zcode.eclipse.plugin.generator.gui;

import java.sql.SQLException;
import java.util.ArrayList;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.zcode.eclipse.plugin.generator.utilities.EclipseGeneratorUtil;
import org.zcode.reverse.engine.ZathuraReverseEngineering;
import org.zcode.reverse.utilities.ZathuraReverseEngineeringUtil;


// TODO: Auto-generated Javadoc
/**
 * Zathura Generator.
 *
 * @author Diego Armando Gomez Mosquera (dgomez@vortexbird.com)
 * @version 1.0
 * @see WizardPage
 */
public class WizardPageSelectTables extends WizardPage {

	/** The cmb catlog schema. */
	private Combo cmbCatlogSchema;
	
	/** The list available tables. */
	private List listAvailableTables;
	
	/** The list selected tables. */
	private List listSelectedTables;
	
	/** The txt table filter. */
	private Text txtTableFilter;
	
	/** The lbl catalog schema. */
	private Label lblCatalogSchema;
	
	/** The btn table filter search. */
	private Button btnTableFilterSearch = null;
	
	/** The list catalogs. */
	private java.util.List<String> listCatalogs = null;
	
	/** The list schemas. */
	private java.util.List<String> listSchemas = null;
	
	/** The catalog name. */
	private String catalogName = null;
	// private String schemaName=null;
	/** The is schema. */
	private Boolean isSchema = null;

	// /Se debe cambiar por fabrica de obejtos para la version 2.1.1
	// TODO revisar esta locura
	/** The db. */
	public static String db = null;

	/**
	 * The Constructor.
	 */
	public WizardPageSelectTables() {
		super("wizardPage"); //$NON-NLS-1$
		setTitle(Messages.WizardPageSelectTables_1);
		setDescription(Messages.WizardPageSelectTables_2);
		setPageComplete(false);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);

		lblCatalogSchema = new Label(container, SWT.NONE);
		lblCatalogSchema.setBounds(10, 13, 112, 17);
		lblCatalogSchema.setText(Messages.WizardPageSelectTables_3);

		cmbCatlogSchema = new Combo(container, SWT.NONE);
		cmbCatlogSchema.setEnabled(false);
		cmbCatlogSchema.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				txtTableFilter.setText(Messages.WizardPageSelectTables_4);
				listAvailableTables.removeAll();
				listSelectedTables.removeAll();
				btnTableFilterSearch.setEnabled(true);

				validatePageComplete();

			}
		});
		cmbCatlogSchema.setBounds(128, 10, 332, 29);

		listAvailableTables = new List(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		listAvailableTables.setBounds(10, 101, 195, 206);

		Label lblAvailableTables = new Label(container, SWT.NONE);
		lblAvailableTables.setBounds(10, 78, 154, 17);
		lblAvailableTables.setText(Messages.WizardPageSelectTables_5);

		listSelectedTables = new List(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		listSelectedTables.setBounds(383, 101, 195, 206);

		Label lblSelectedTables = new Label(container, SWT.NONE);
		lblSelectedTables.setBounds(384, 78, 194, 17);
		lblSelectedTables.setText(Messages.WizardPageSelectTables_6);

		Button btnUpdateList = new Button(container, SWT.NONE);
		btnUpdateList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				try {
					resetForm();

					listCatalogs = ZathuraReverseEngineeringUtil.getCatalogs();
					listSchemas = ZathuraReverseEngineeringUtil.getSchemas();

					if (listSchemas != null && listSchemas.size() > 1) {
						if (listCatalogs != null && listCatalogs.size() == 1) {
							catalogName = listCatalogs.get(0);
						} else {
							catalogName = null;
						}

						isSchema = true;
						lblCatalogSchema.setText(Messages.WizardPageSelectTables_7);
						for (String catalogSchemaName : listSchemas) {
							cmbCatlogSchema.add(catalogSchemaName);
						}
					} else if (listCatalogs != null && listCatalogs.size() > 0) {
						isSchema = false;
						lblCatalogSchema.setText(Messages.WizardPageSelectTables_8);
						for (String catalogSchemaName : listCatalogs) {
							cmbCatlogSchema.add(catalogSchemaName);
						}

					}
					if (cmbCatlogSchema.getItems() != null && cmbCatlogSchema.getItems().length > 0) {
						cmbCatlogSchema.setEnabled(true);
						cmbCatlogSchema.select(0);
					}

					validatePageComplete();
				} catch (SQLException e1) {
					e1.printStackTrace();
					setPageComplete(false);
				} catch (Exception ex) {
					ex.printStackTrace();
					setPageComplete(false);
				}
			}

		});
		btnUpdateList.setBounds(466, 10, 98, 25);
		btnUpdateList.setText(Messages.WizardPageSelectTables_9);

		Button btnAdd = new Button(container, SWT.NONE);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String selectionIndices[] = listAvailableTables.getSelection();
				for (String item : selectionIndices) {
					listSelectedTables.add(item);
					listAvailableTables.remove(item);
				}
				validatePageComplete();
			}
		});
		btnAdd.setBounds(237, 137, 112, 29);
		btnAdd.setText(Messages.WizardPageSelectTables_10);

		Button btnAddAll = new Button(container, SWT.NONE);
		btnAddAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String selectionIndices[] = listAvailableTables.getItems();
				for (String item : selectionIndices) {
					listSelectedTables.add(item);
					listAvailableTables.remove(item);
				}
				validatePageComplete();
			}
		});
		btnAddAll.setBounds(237, 172, 112, 29);
		btnAddAll.setText(Messages.WizardPageSelectTables_11);

		Button btnRemove = new Button(container, SWT.NONE);
		btnRemove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String selectionIndices[] = listSelectedTables.getSelection();
				for (String item : selectionIndices) {
					listAvailableTables.add(item);
					listSelectedTables.remove(item);
				}
				validatePageComplete();
			}
		});
		btnRemove.setBounds(237, 207, 112, 29);
		btnRemove.setText(Messages.WizardPageSelectTables_12);

		Button btnRemoveAll = new Button(container, SWT.NONE);
		btnRemoveAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String selectionIndices[] = listSelectedTables.getItems();
				for (String item : selectionIndices) {
					listAvailableTables.add(item);
					listSelectedTables.remove(item);
				}
				validatePageComplete();
			}
		});
		btnRemoveAll.setBounds(237, 242, 112, 29);
		btnRemoveAll.setText(Messages.WizardPageSelectTables_13);

		txtTableFilter = new Text(container, SWT.BORDER);
		txtTableFilter.setText(Messages.WizardPageSelectTables_14);
		txtTableFilter.setBounds(128, 39, 332, 23);

		Label lblTableFilter = new Label(container, SWT.NONE);
		lblTableFilter.setBounds(10, 42, 112, 15);
		lblTableFilter.setText(Messages.WizardPageSelectTables_15);

		btnTableFilterSearch = new Button(container, SWT.NONE);
		btnTableFilterSearch.setEnabled(false);
		btnTableFilterSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					listAvailableTables.removeAll();
					listSelectedTables.removeAll();

					if (isSchema == true) {
						java.util.List<String> tableInfos = ZathuraReverseEngineeringUtil.getTables(catalogName, cmbCatlogSchema.getText(), txtTableFilter.getText());
						for (String tableInfo : tableInfos) {
							listAvailableTables.add(tableInfo);
						}
					} else {
						java.util.List<String> tableInfos = ZathuraReverseEngineeringUtil.getTables(cmbCatlogSchema.getText(), null, txtTableFilter.getText());
						for (String tableInfo : tableInfos) {
							listAvailableTables.add(tableInfo);
						}
					}
				} catch (Exception e2) {
					// ignore
					setPageComplete(false);
				}
			}
		});
		btnTableFilterSearch.setBounds(466, 37, 98, 25);
		btnTableFilterSearch.setText(Messages.WizardPageSelectTables_16);

	}

	/**
	 * Validate page complete.
	 */
	private void validatePageComplete() {

		try {
			if (listSelectedTables != null && listSelectedTables.getItemCount() > 0) {
				setPageComplete(true);

				// Si es por Catalogs
				if (listCatalogs != null && listCatalogs.size() > 0) {

					EclipseGeneratorUtil.catalog = cmbCatlogSchema.getText();
					EclipseGeneratorUtil.schema = null;
					EclipseGeneratorUtil.catalogAndSchema = ZathuraReverseEngineering.CATALOG;

					EclipseGeneratorUtil.tablesList = loadTablesList();

				}
				// Si es por schema
				if (listSchemas != null && listSchemas.size() > 0) {

					EclipseGeneratorUtil.catalog = null;
					EclipseGeneratorUtil.schema = cmbCatlogSchema.getText();
					EclipseGeneratorUtil.catalogAndSchema = ZathuraReverseEngineering.SCHEMA;

					EclipseGeneratorUtil.tablesList = loadTablesList();
				}

				// Ambos para db2/400 usa catalog y schema
				if (listCatalogs != null && listCatalogs.size() > 0 && listSchemas != null && listSchemas.size() > 0 && db.equals("JTOpen(AS/400)") == true) { //$NON-NLS-1$

					EclipseGeneratorUtil.catalog = catalogName;
					EclipseGeneratorUtil.schema = cmbCatlogSchema.getText();
					EclipseGeneratorUtil.catalogAndSchema = ZathuraReverseEngineering.CATALOG_SCHEMA;
					EclipseGeneratorUtil.tablesList = loadTablesList();
				}

			} else {
				throw new Exception(Messages.WizardPageSelectTables_18);
			}

			setPageComplete(true);
			setErrorMessage(null);

		} catch (Exception e) {
			setPageComplete(false);
			setErrorMessage(e.getMessage());
		}

	}

	/**
	 * Load tables list.
	 *
	 * @return the list< string>
	 */
	private java.util.List<String> loadTablesList() {
		java.util.List<String> tablesList = new ArrayList<String>();
		String[] tableItems = listSelectedTables.getItems();
		for (String tableName : tableItems) {
			tablesList.add(tableName);
		}
		return tablesList;
	}

	/**
	 * Reset form.
	 */
	public void resetForm() {
		txtTableFilter.setText(Messages.WizardPageSelectTables_19);
		cmbCatlogSchema.removeAll();
		listAvailableTables.removeAll();
		listSelectedTables.removeAll();
		cmbCatlogSchema.setEnabled(false);
		lblCatalogSchema.setText(Messages.WizardPageSelectTables_20);
		setPageComplete(false);
		btnTableFilterSearch.setEnabled(false);
	}
}
