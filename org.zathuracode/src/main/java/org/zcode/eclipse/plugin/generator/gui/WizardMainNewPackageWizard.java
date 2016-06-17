package org.zcode.eclipse.plugin.generator.gui;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.ui.wizards.NewPackageWizardPage;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.zcode.eclipse.plugin.generator.ZathuraGeneratorActivator;
import org.zcode.swt.utilities.ResourceManager;


// TODO: Auto-generated Javadoc
/**
 * Zathuracode Generator.
 *
 * @author Diego Armando Gomez Mosquera (dgomez@vortexbird.com)
 * @version 1.0
 * @see Wizard
 */
public class WizardMainNewPackageWizard extends Wizard {

	
	/** The i project. */
	private IProject iProject;
	
	/** The new package wizard page. */
	private NewPackageWizardPage newPackageWizardPage=null;
	
	
	/**
	 * The Constructor.
	 */
	public WizardMainNewPackageWizard() {
		super();
		
		setWindowTitle(Messages.title);
		setDefaultPageImageDescriptor(ResourceManager.getPluginImageDescriptor(ZathuraGeneratorActivator.getDefault(), Messages.WizardMainNewPackageWizard_1));
		newPackageWizardPage=new NewPackageWizardPage();
		newPackageWizardPage.setPageComplete(false);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {		

		addPage(newPackageWizardPage);
		newPackageWizardPage.setPageComplete(false);
		newPackageWizardPage.setPackageText("",true); //$NON-NLS-1$
		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		try {
			if(newPackageWizardPage.getPackageText().trim().equals("")==true){ //$NON-NLS-1$
				return false;
			}
				
				
			newPackageWizardPage.createPackage(null);
			return true;
		} catch (CoreException e) {
			MessageDialog.openError(getShell(),Messages.WizardMainNewPackageWizard_4,e.getMessage());
		} catch (InterruptedException e) {
			MessageDialog.openError(getShell(),Messages.WizardMainNewPackageWizard_5,e.getMessage());
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#canFinish()
	 */
	@Override
	public boolean canFinish() {
		IWizardPage page = getContainer().getCurrentPage();
		if(page == newPackageWizardPage && newPackageWizardPage.isPageComplete()==true){
			return true;
		}
		return false;
	}
	
	


	/**
	 * Gets the new package wizard page.
	 *
	 * @return the newPackageWizardPage
	 */
	public NewPackageWizardPage getNewPackageWizardPage() {
		return newPackageWizardPage;
	}


	/**
	 * Sets the new package wizard page.
	 *
	 * @param newPackageWizardPage the newPackageWizardPage to set
	 */
	public void setNewPackageWizardPage(NewPackageWizardPage newPackageWizardPage) {
		this.newPackageWizardPage = newPackageWizardPage;
	}


	/**
	 * Geti project.
	 *
	 * @return the i project
	 */
	public IProject getiProject() {
		return iProject;
	}


	/**
	 * Seti project.
	 *
	 * @param iProject the i project
	 */
	public void setiProject(IProject iProject) {
		this.iProject = iProject;
	}
	
	

}
