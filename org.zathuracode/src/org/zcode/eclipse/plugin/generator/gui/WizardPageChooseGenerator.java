package org.zcode.eclipse.plugin.generator.gui;


import java.util.HashMap;

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.zcode.eclipse.plugin.generator.utilities.EclipseGeneratorUtil;
import org.zcode.generator.factory.ZathuraGeneratorFactory;
import org.zcode.generator.model.GeneratorModel;
import org.zcode.generator.utilities.GeneratorUtil;


// TODO: Auto-generated Javadoc
/**
 * Zathura Generator.
 *
 * @author Diego Armando Gomez Mosquera (dgomez@vortexbird.com)
 * @version 1.0
 * @see WizardPage
 */
public class WizardPageChooseGenerator extends WizardPage {
	
	/** The list generators version 3.1 */
	private List listGeneratorsVersion3_1;
	
	/** The list generators. */
	private List listGenerators;
	
	/** The bwr description. */
	private Browser bwrDescription;
	
	private ComboViewer comboViewer;
	
	// Load the zathura Generators names
	/** The generators. */
	private HashMap<String, GeneratorModel> theGenerators = ZathuraGeneratorFactory.getTheZathuraGenerators();

	/**
	 * Create the wizard.
	 */
	public WizardPageChooseGenerator() {
		super("wizardPage"); //$NON-NLS-1$
		setTitle(Messages.title);
		setDescription(Messages.WizardPageChooseGenerator_2);
		setPageComplete(false);
	}

	/**
	 * Create contents of the wizard.
	 *
	 * @param parent the parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		//
		setControl(container);

		final Group generatorChoiseGroup = new Group(container, SWT.NONE);
		generatorChoiseGroup.setText(Messages.WizardPageChooseGenerator_3);
		generatorChoiseGroup.setBounds(10, 10, 580, 348);
		
		
		final Label lblGeneratorVersion3_1 = new Label(generatorChoiseGroup, SWT.NONE);
		lblGeneratorVersion3_1.setText(Messages.WizardPageChooseGenerator_6);
		lblGeneratorVersion3_1.setBounds(10, 22, 99, 17);
		
		listGeneratorsVersion3_1 = new List(generatorChoiseGroup, SWT.BORDER);
		listGeneratorsVersion3_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				
				String architectureSelected = (listGeneratorsVersion3_1.getSelection()[0]);
				String architectureName = ZathuraGeneratorFactory.getGeneratorNameForGuiName(architectureSelected);
				EclipseGeneratorUtil.zathuraGeneratorName = architectureName;
				GeneratorModel generatorModel = theGenerators.get(architectureName);
				if (generatorModel != null) {
					//Se reemplaza el texto ${RELATIVE_PATH} por la ruta relativa del proyecto,
					//para que al momento de mostrar el html, se muestren las imagenes
					String descriptionHTML = generatorModel.getDescription().replace("${RELATIVE_PATH}", GeneratorUtil.getFullPath()); //$NON-NLS-1$
					bwrDescription.setText(descriptionHTML);
					setPageComplete(true);
				}
			}
		});
		listGeneratorsVersion3_1.setBounds(10, 45, 204, 277);

		bwrDescription = new Browser(generatorChoiseGroup, SWT.NONE);
		bwrDescription.setBounds(227, 45, 342, 277);

		//loadListGeneratorsVersion3_0();
		loadListGeneratorsVersion3_1();
	}


	
	
	/**
	 * Load list generators version 4.0.
	 */
	public void loadListGeneratorsVersion3_1() {
		if (listGeneratorsVersion3_1 != null) {
			listGeneratorsVersion3_1.removeAll();
			for (GeneratorModel generatorModel : theGenerators.values()) {				
					listGeneratorsVersion3_1.add(generatorModel.getGuiName());
			}
		}

		if (bwrDescription != null) {
			bwrDescription.setText(""); //$NON-NLS-1$
		}
	}
	
}
