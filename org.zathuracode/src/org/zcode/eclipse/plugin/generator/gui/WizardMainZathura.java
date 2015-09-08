package org.zcode.eclipse.plugin.generator.gui;

import java.lang.reflect.InvocationTargetException;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.zcode.eclipse.plugin.generator.ZathuraGeneratorActivator;
import org.zcode.eclipse.plugin.generator.utilities.EclipseGeneratorUtil;
import org.zcode.eclipse.plugin.generator.utilities.RunningGeneration;
import org.zcode.eclipse.plugin.generator.utilities.ZathuraGeneratorLog;
import org.zcode.swt.utilities.ResourceManager;

import com.sun.tools.internal.ws.wsdl.document.jaxws.Exception;



/**
 * Zathuracode Generator.
 *
 * @author Diego Armando Gomez Mosquera (dgomez@vortexbird.com)
 * @version 1.0
 * @see Wizard
 */
public class WizardMainZathura extends Wizard {
	
	//WizardPage
	/** The wizard generator chose. */
	public WizardPageChooseGenerator wizardGeneratorChose;
	
	//WizardPage Generator
	/** The wizard select db connection. */
	public WizardPageSelectDBConnection wizardSelectDBConnection;
	
	/** The wizard database connection. */
	public WizardPageDatabaseConnection wizardDatabaseConnection;
	
	/** The wizard select tables. */
	public WizardPageSelectTables wizardSelectTables;
	
	/** The wizard choose source folder and package. */
	public WizardPageChooseSourceFolderAndPackage wizardChooseSourceFolderAndPackage;
	
	


	/**
	 * The Constructor.
	 */
	public WizardMainZathura() {
		super();
		setWindowTitle(Messages.title);
		setDefaultPageImageDescriptor(ResourceManager.getPluginImageDescriptor(ZathuraGeneratorActivator.getDefault(), "icons/balvardi-Robotic7070.jpg")); //$NON-NLS-1$
		EclipseGeneratorUtil.reset();
		EclipseGeneratorUtil.wizardMain=this;		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		super.addPages();
		//Creo el wizard y lo asigno al administrado
		
		wizardSelectDBConnection=new WizardPageSelectDBConnection();
		addPage(wizardSelectDBConnection);
		
		wizardSelectTables=new WizardPageSelectTables();
		addPage(wizardSelectTables);
		
		wizardChooseSourceFolderAndPackage=new WizardPageChooseSourceFolderAndPackage();
		addPage(wizardChooseSourceFolderAndPackage);		
		
		wizardGeneratorChose=new WizardPageChooseGenerator();
		addPage(wizardGeneratorChose);
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		try {
			
			
			
			ProgressMonitorDialog progressMonitorDialogReverseEngineering=new ProgressMonitorDialog(getShell());
			progressMonitorDialogReverseEngineering.run(true, true,new org.zcode.eclipse.plugin.generator.utilities.RunningGenerationReverseEngineering(getShell()));
			
			ProgressMonitorDialog progressMonitorDialogGeneration=new ProgressMonitorDialog(getShell());
			progressMonitorDialogGeneration.run(true, true,new RunningGeneration(getShell()));
			
			
			
			
		} catch (InvocationTargetException e) {
          MessageDialog.openError(getShell(), Messages.WizardMainZathura_2, e.toString());
          ZathuraGeneratorLog.logError(e);
        } catch (InterruptedException e) {
          MessageDialog.openInformation(getShell(), Messages.WizardMainZathura_3, e.toString());
          ZathuraGeneratorLog.logError(e);
        }			
		return true;
	}
	
	/**
	 * Se usa para saber si se puede finalizar el Wizard.
	 *
	 * @return true, if can finish
	 */
	@Override
	public boolean canFinish(){
		IWizardPage page = getContainer().getCurrentPage();
		if(page == wizardGeneratorChose){
			return wizardGeneratorChose.isPageComplete();
		}
		return false;
	}

}
