package org.zcode.eclipse.plugin.generator.gui;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.internal.core.PackageFragment;
import org.eclipse.jdt.internal.ui.dialogs.PackageSelectionDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.m2e.core.internal.IMavenConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.zcode.eclipse.plugin.generator.utilities.EclipseGeneratorUtil;
import org.zcode.generator.utilities.GeneratorUtil;
import org.zcode.generator.utilities.JalopyCodeFormatter;
import org.zcode.reverse.utilities.ZathuraReverseEngineeringUtil;



// TODO: Auto-generated Javadoc
/**
 * Zathura Generator.
 *
 * @author Diego Armando Gomez Mosquera (dgomez@vortexbird.com)
 * @version 1.0
 * @see WizardPage
 */
@SuppressWarnings("restriction")
public class WizardPageChooseSourceFolderAndPackage extends WizardPage {
	
	/** The txt package. */
	private Text txtPackage;
	
	/** The txt java source folder. */
	private Text txtJavaSourceFolder;
	
	/** The project. */
	private IProject project;
	
	/** The btn package. */
	private Button btnPackage; 
	
	/** The txt web root. */
	private Text txtWebRoot;
	
	/** The txt lib. */
	private Text txtLib;
	
	/** The cmb project. */
	private Combo cmbProject;
	
	/** The wizard main new package wizard page. */
	private  WizardMainNewPackageWizard wizardMainNewPackageWizardPage;
	
	/** The btn new package. */
	private Button btnNewPackage;
	
	/** The btn web root. */
	private Button btnWebRoot;
	
	/** The lbl lib.*/
	private Label lblLibraries;
	
	private Boolean isFrontend = true;
	
	/**
	 * Create the wizard.
	 */
	public WizardPageChooseSourceFolderAndPackage() {
		super("wizardPage"); //$NON-NLS-1$
		setTitle(Messages.WizardPageChooseSourceFolderAndPackage_1);
		setDescription(Messages.WizardPageChooseSourceFolderAndPackage_2);
		//setImageDescriptor(ResourceManager.getPluginImageDescriptor(ZathuraGeneratorActivator.getDefault(), "icons/NewRDBDatabaseWiz.gif"));
		//setImageDescriptor(ResourceManager.getPluginImageDescriptor(ZathuraGeneratorActivator.getDefault(), "icons/balvardi-Robotic7070.png"));
		setPageComplete(false);
		EclipseGeneratorUtil.makeItXml=true;
		EclipseGeneratorUtil.isFrontend=true;
		
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

		final Group choosePathGroup = new Group(container, SWT.NONE);
		choosePathGroup.setText(Messages.WizardPageChooseSourceFolderAndPackage_3);
		choosePathGroup.setBounds(0, 0, 581, 212);

		final Label lblJavaSourceFolder = new Label(choosePathGroup, SWT.NONE);
		lblJavaSourceFolder.setText(Messages.WizardPageChooseSourceFolderAndPackage_4);
		lblJavaSourceFolder.setBounds(10, 55, 99, 21);

		txtJavaSourceFolder = new Text(choosePathGroup, SWT.BORDER);
		txtJavaSourceFolder.setEditable(false);
		txtJavaSourceFolder.setBounds(140, 52, 338, 24);

		final Button btnJavaSourceFolder = new Button(choosePathGroup, SWT.NONE);
		btnJavaSourceFolder.setEnabled(false);
		btnJavaSourceFolder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				handleBrowseSourceFolder();
				validatePageComplete();
				txtPackage.setText(""); //$NON-NLS-1$
				
			}
		});
		btnJavaSourceFolder.setText(Messages.WizardPageChooseSourceFolderAndPackage_6);
		btnJavaSourceFolder.setBounds(484, 52, 87, 27);

		final Label lblJavaPackage = new Label(choosePathGroup, SWT.NONE);
		lblJavaPackage.setText(Messages.WizardPageChooseSourceFolderAndPackage_7);
		lblJavaPackage.setBounds(10, 93, 124, 21);

		txtPackage = new Text(choosePathGroup, SWT.BORDER);
		txtPackage.setEditable(false);
		txtPackage.setBounds(140, 90, 247, 24);

		btnPackage = new Button(choosePathGroup, SWT.NONE);
		btnPackage.setEnabled(false);
		btnPackage.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				handleBrowsePackage();
				validatePageComplete();
			}
		});
		btnPackage.setText(Messages.WizardPageChooseSourceFolderAndPackage_8);
		btnPackage.setBounds(484, 90, 87, 27);
		
		Label lblWebRoot = new Label(choosePathGroup, SWT.NONE);
		lblWebRoot.setText(Messages.WizardPageChooseSourceFolderAndPackage_9);
		lblWebRoot.setBounds(10, 132, 124, 21);
		
		lblLibraries = new Label(choosePathGroup, SWT.NONE);
		lblLibraries.setText(Messages.WizardPageChooseSourceFolderAndPackage_10);
		lblLibraries.setBounds(10, 171, 124, 31);
		
		txtWebRoot = new Text(choosePathGroup, SWT.BORDER);
		txtWebRoot.setEditable(false);
		txtWebRoot.setBounds(140, 129, 338, 24);
		
		txtLib = new Text(choosePathGroup, SWT.BORDER);
		txtLib.setEditable(false);
		txtLib.setBounds(140, 168, 338, 24);
		
		btnWebRoot = new Button(choosePathGroup, SWT.NONE);
		btnWebRoot.setEnabled(false);
		btnWebRoot.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				handleBrowseWebRootFolder();
				validatePageComplete();
			}
		});
		btnWebRoot.setText(Messages.WizardPageChooseSourceFolderAndPackage_11);
		btnWebRoot.setBounds(484, 128, 87, 27);
		
		cmbProject = new Combo(choosePathGroup, SWT.NONE);
		cmbProject.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String projectSelected=cmbProject.getText();
				project=ResourcesPlugin.getWorkspace().getRoot().getProject(projectSelected);
				if(project!=null){
					btnJavaSourceFolder.setEnabled(true);
					btnWebRoot.setEnabled(true);
					
					String pathFilePOM = project.getLocation().toString() + GeneratorUtil.slash + GeneratorUtil.pomFile;
					EclipseGeneratorUtil.pomXmlFile = new File(pathFilePOM);
					EclipseGeneratorUtil.isMavenProject = EclipseGeneratorUtil.pomXmlFile.exists();
					EclipseGeneratorUtil.project=project;
					
					if (EclipseGeneratorUtil.isMavenProject) {						
						txtLib.setText("pom.xml"); //$NON-NLS-1$
						EclipseGeneratorUtil.libFolderPath = ""; //$NON-NLS-1$
					}
				}			
			}
		});
		cmbProject.setBounds(140, 15, 338, 28);
		IProject projectArray[]=ResourcesPlugin.getWorkspace().getRoot().getProjects();
			for (IProject iProject : projectArray) {
				
				try {
					if(iProject.isOpen()==true &&  iProject.hasNature(JavaCore.NATURE_ID)==true && iProject.hasNature(IMavenConstants.NATURE_ID)==true){
						cmbProject.add(iProject.getName());
					}
				} catch (CoreException e1) {
					e1.printStackTrace();
				}				
			}	
		Label lblProject = new Label(choosePathGroup, SWT.NONE);
		lblProject.setBounds(10, 21, 99, 17);
		lblProject.setText(Messages.WizardPageChooseSourceFolderAndPackage_18);
		
		btnNewPackage = new Button(choosePathGroup, SWT.NONE);
		btnNewPackage.setEnabled(false);
		btnNewPackage.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
					
				wizardMainNewPackageWizardPage=new WizardMainNewPackageWizard();
			    wizardMainNewPackageWizardPage.setiProject(project);
			    
			   	    
			   
				 WizardDialog dlg = new WizardDialog(getShell(),wizardMainNewPackageWizardPage);
				 
				 wizardMainNewPackageWizardPage.getNewPackageWizardPage().setPageComplete(false);
				 wizardMainNewPackageWizardPage.getNewPackageWizardPage().setPackageText("",true); //$NON-NLS-1$
				 
				 
				 int resultado=dlg.open();
				 	if(resultado!= WizardDialog.CANCEL && wizardMainNewPackageWizardPage.getNewPackageWizardPage().getPackageText()!=null && wizardMainNewPackageWizardPage.getNewPackageWizardPage().getPackageText().toString().trim().equals("")!=true){ //$NON-NLS-1$
				 		txtPackage.setText(wizardMainNewPackageWizardPage.getNewPackageWizardPage().getPackageText());
				 		EclipseGeneratorUtil.javaEntityPackage=txtPackage.getText();
				 	}
				 	
				 validatePageComplete();
				 
			}
		});
		btnNewPackage.setBounds(393, 90, 87, 27);
		btnNewPackage.setText(Messages.WizardPageChooseSourceFolderAndPackage_21);
		
		Button bRadioJPAReverseEngineering = new Button(container, SWT.RADIO);
		bRadioJPAReverseEngineering.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setDescription(Messages.WizardPageChooseSourceFolderAndPackage_22);
				EclipseGeneratorUtil.makeItXml=false;
				loadListGeneratorsNextWizard();
			}
		});
		bRadioJPAReverseEngineering.setBounds(10, 246, 189, 22);
		bRadioJPAReverseEngineering.setText(Messages.WizardPageChooseSourceFolderAndPackage_23);
		
		Button bRadioHibernateReverseEngineering = new Button(container, SWT.RADIO);
		bRadioHibernateReverseEngineering.setSelection(true);
		bRadioHibernateReverseEngineering.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setDescription(Messages.WizardPageChooseSourceFolderAndPackage_24);
				EclipseGeneratorUtil.makeItXml=true;
				loadListGeneratorsNextWizard();
			}
		});
		bRadioHibernateReverseEngineering.setBounds(10, 218, 232, 22);
		bRadioHibernateReverseEngineering.setText(Messages.WizardPageChooseSourceFolderAndPackage_25);
		
		Label label = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setText(Messages.WizardPageChooseSourceFolderAndPackage_label_text);
		label.setBounds(10, 282, 552, 2);
		
		Button btnFrontendCheckButton = new Button(container, SWT.CHECK);
		btnFrontendCheckButton.setSelection(true);
		btnFrontendCheckButton.addSelectionListener(new SelectionAdapter() {
		@Override
			public void widgetSelected(SelectionEvent e) {
				isFrontend = (!isFrontend ? true : false);
				System.out.println(isFrontend);
				EclipseGeneratorUtil.isFrontend = isFrontend;
			}
		});
		btnFrontendCheckButton.setBounds(10, 294, 189, 20);
		btnFrontendCheckButton.setText(Messages.WizardPageChooseSourceFolderAndPackage_btnCheckButton_text);
		
		
	}
	
	/**
	 * Handle browse source folder.
	 */
	private void handleBrowseSourceFolder() {
		
		ContainerSelectionDialog dialog = new ContainerSelectionDialog(getShell(), project, false,	Messages.WizardPageChooseSourceFolderAndPackage_27);	
		dialog.showClosedProjects(false);
		
		

		if (dialog.open() == Window.OK) {
		
			Object[] result = dialog.getResult();
			if (result.length == 1) {
				
				    txtJavaSourceFolder.setText(((Path) result[0]).toString());
					//String fullPathWorkspace = GeneratorUtil.replaceAll(project.getLocation().toString(), project.getFullPath().toString(), "");
					String fullPathWorkspace = project.getLocation().toString().replaceAll(project.getFullPath().toString(), ""); //$NON-NLS-1$
					EclipseGeneratorUtil.fullPathProject=project.getLocation().toString();
					EclipseGeneratorUtil.project=project;
					EclipseGeneratorUtil.projectName=EclipseGeneratorUtil.project.getName();
					EclipseGeneratorUtil.workspaceFolderPath=fullPathWorkspace;
					
					EclipseGeneratorUtil.javaSourceFolderPath = EclipseGeneratorUtil.workspaceFolderPath+txtJavaSourceFolder.getText()+GeneratorUtil.slash;
					btnPackage.setEnabled(true);
					btnNewPackage.setEnabled(true);
				
			}
		}
	}
	
	/**
	 * Handle browse package.
	 */
	private void handleBrowsePackage() {

		
		IPackageFragment[] packages =null;
		IJavaProject javaProject = JavaCore.create(project);
		IJavaElement javaElementArray[]=null;
		ArrayList<IJavaElement>	javaElementsList=new ArrayList<IJavaElement>();
		
		
		//Si el projecto no esta abierto se cancela el proceso
		if(javaProject.isOpen()==false){
			MessageDialog.openError(getShell(), Messages.WizardPageChooseSourceFolderAndPackage_29, Messages.WizardPageChooseSourceFolderAndPackage_30);
			return;
		}
		
		//Lee los paquetes solo del proyecto
		try {
			packages = javaProject.getPackageFragments();
			
			for (IPackageFragment iPackageFragment : packages) {
				if(iPackageFragment.getKind() == IPackageFragmentRoot.K_SOURCE){
					javaElementsList.add(iPackageFragment);
				}
			}		
			if(javaElementsList.size()>0){
				javaElementArray=new IJavaElement[javaElementsList.size()];
				javaElementArray=javaElementsList.toArray(javaElementArray);
			}
		} catch (JavaModelException e) {
			MessageDialog.openError(getShell(), Messages.WizardPageChooseSourceFolderAndPackage_31, e.getMessage());
		}
		
		
			Shell shell = getShell();
			IJavaSearchScope iJavaSearchScope = SearchEngine.createJavaSearchScope(javaElementArray,false);
			PackageSelectionDialog packageSelectionDialog = new PackageSelectionDialog(shell, new ProgressMonitorDialog(shell),PackageSelectionDialog.F_REMOVE_DUPLICATES|PackageSelectionDialog.F_HIDE_EMPTY_INNER,iJavaSearchScope);
			
			packageSelectionDialog.setTitle(Messages.WizardPageChooseSourceFolderAndPackage_32);
			packageSelectionDialog.setMessage(Messages.WizardPageChooseSourceFolderAndPackage_33);
	
			if (packageSelectionDialog.open() == Window.OK) {
				Object results[] = packageSelectionDialog.getResult();
				if (results != null && results.length > 0) {
					PackageFragment packageFragment = (PackageFragment) results[0];
					txtPackage.setText(packageFragment.getElementName());
					EclipseGeneratorUtil.javaEntityPackage=packageFragment.getElementName();
				}
			}
	}
	
	
		//Se usa para validar si la pagina se completo y poder activar el next o el finish segun sea necesario	
		/**
		 * Validate page complete.
		 */
		private void validatePageComplete(){			
			try {
				if(txtJavaSourceFolder==null || txtJavaSourceFolder.getText().equals("")==true){ //$NON-NLS-1$
					throw new Exception(Messages.WizardPageChooseSourceFolderAndPackage_35);
				}
				
				if(txtPackage==null ||  txtPackage.getText().equals("")==true ){ //$NON-NLS-1$
					throw new Exception(Messages.WizardPageChooseSourceFolderAndPackage_37);
				}
				
				if(txtWebRoot==null || txtWebRoot.getText().equals("")==true){ //$NON-NLS-1$
					throw new Exception(Messages.WizardPageChooseSourceFolderAndPackage_39);
				}	
				
				if(txtLib==null || txtLib.getText().equals("")==true){ //$NON-NLS-1$
					throw new Exception(Messages.WizardPageChooseSourceFolderAndPackage_41);		
				}

				//Valida si el paquete esta bien escrito porque el generador lo crea si no existe
				ZathuraReverseEngineeringUtil.validarPackage(txtPackage.getText());				
				
				EclipseGeneratorUtil.companyDomainName = txtPackage.getText();
				EclipseGeneratorUtil.destinationDirectory = txtJavaSourceFolder.getText();
				
				setPageComplete(true);
				setErrorMessage(null);
				
			} catch (Exception e) {
				setPageComplete(false);
				setErrorMessage(e.getMessage());
			}
		
		
	}
		
		
		/**
		 * Handle browse web root folder.
		 */
		private void handleBrowseWebRootFolder() {
			
			ContainerSelectionDialog dialog = new ContainerSelectionDialog(getShell(), project, false,	Messages.WizardPageChooseSourceFolderAndPackage_42);
			dialog.showClosedProjects(false);
			
			if (dialog.open() == Window.OK) {
				Object[] result = dialog.getResult();
				if (result.length == 1) {
					txtWebRoot.setText(((Path) result[0]).toString());
					EclipseGeneratorUtil.webRootFolderPath=EclipseGeneratorUtil.workspaceFolderPath+txtWebRoot.getText()+GeneratorUtil.slash;
					
				}
			}
		}
		
		/**
		 * Handle browse lib folder.
		 */
		private void handleBrowseLibFolder() {
			ContainerSelectionDialog dialog = new ContainerSelectionDialog(getShell(), project, false,	Messages.WizardPageChooseSourceFolderAndPackage_43);
			dialog.showClosedProjects(false);
			
			if (dialog.open() == Window.OK) {
				Object[] result = dialog.getResult();
				if (result.length == 1) {
					txtLib.setText(((Path) result[0]).toString());
					EclipseGeneratorUtil.libFolderPath=EclipseGeneratorUtil.workspaceFolderPath+txtLib.getText()+org.zcode.generator.utilities.GeneratorUtil.slash;				
				}
			}
		}
		
		/**
		 * Load list generators next wizard.
		 */
		private void loadListGeneratorsNextWizard(){
			Object object=getNextPage();
			if(object instanceof WizardPageChooseGenerator){
				WizardPageChooseGenerator wizardChooseGenerator=(WizardPageChooseGenerator)object;
				
				wizardChooseGenerator.loadListGeneratorsVersion3_1();
			}
			
		}
}
