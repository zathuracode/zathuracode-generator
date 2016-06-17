package org.zcode.eclipse.plugin.generator.gui;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

// TODO: Auto-generated Javadoc
/**
 * Zathura Generator.
 *
 * @author Diego Armando Gomez Mosquera (dgomez@vortexbird.com)
 * @version 1.0
 * @see WizardPage
 */
public class WizardPageGenerationProcessProgress extends WizardPage {

	/** The txt summary. */
	private Text txtSummary;

	/**
	 * Create the wizard.
	 */
	public WizardPageGenerationProcessProgress() {
		super("wizardPage"); //$NON-NLS-1$
		setTitle(Messages.WizardPageGenerationProcessProgress_1);
		
		// setImageDescriptor(ResourceManager.getPluginImageDescriptor(ZathuraGeneratorActivator.getDefault(),
		// "icons/balvardi-Robotic7070.png"));
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

		final Group generationProcessProgressGroup = new Group(container, SWT.NONE);
		generationProcessProgressGroup.setText(Messages.WizardPageGenerationProcessProgress_2);
		generationProcessProgressGroup.setBounds(10, 10, 581, 288);

		final ScrolledComposite scrolledComposite = new ScrolledComposite(generationProcessProgressGroup, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setBounds(10, 25, 561, 253);

		txtSummary = new Text(scrolledComposite, SWT.BORDER);
		txtSummary.setEditable(false);

		txtSummary.setSize(561, 253);
		scrolledComposite.setContent(txtSummary);
	}

}
