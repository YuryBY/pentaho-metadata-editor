package org.pentaho.pms.auto;

/**
 * Created by Yury_Bakhmutski on 7/28/2014.
 */

import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.junit.Before;
import org.junit.Test;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.Props;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.LogChannel;
import org.pentaho.di.core.logging.LogWriter;
import org.pentaho.di.ui.core.PropsUI;
import org.pentaho.pms.ui.MetaEditor;
import org.pentaho.pms.ui.concept.editor.Constants;
import org.pentaho.pms.ui.util.Const;
import org.pentaho.pms.ui.util.Splash;

public class MetaEditorAutoTest {
  // /to separate file
  private final static String CONNECTION_NAME = "Connection Name:";
  private final static String CONNECTION_TYPE = "Connection Type:";
  private final static String ACCESS = "Access:";
  private final static String HOST_NAME = "Host Name:";
  private final static String DATABASE_NAME = "Database Name:";
  private final static String PORT_NUMBER = "Port Number:";
  private final static String USER_NAME = "User Name:";
  private final static String PASSWORD = "Password:";

  private LogWriter logwriter;
  private LogChannel log = new LogChannel( MetaEditor.APPLICATION_NAME );

  @Before
  public void initLogger() {
    try {
      KettleEnvironment.init( false );
    } catch ( KettleException e1 ) {
      e1.printStackTrace();
    }

    System.setProperty( "java.naming.factory.initial", "org.osjava.sj.SimpleContextFactory" ); 
    System.setProperty( "org.osjava.sj.root", "simple-jndi" ); 
    System.setProperty( "org.osjava.sj.delimiter", "/" ); 

    try {
      logwriter = LogWriter.getInstance( Const.META_EDITOR_LOG_FILE, false );
    } catch ( KettleException e1 ) {
      e1.printStackTrace();
    }
  }
  
    @Test
    public void test() {
      Display display = new Display();
      
      if (!Props.isInitialized()) {
        Const.checkPentahoMetadataDirectory();
        PropsUI.init(display, Const.getPropertiesFile()); // things to remember...
      }
      
      Window.setDefaultImage(Constants.getImageRegistry(Display.getCurrent()).get("pentaho-icon")); 
      
      Splash splash = new Splash(display);

      final MetaEditor win = new MetaEditor(log, display);

      // Read kettle transformation specified on command-line?
     // if (args.length == 1 && !Const.isEmpty(args[0])) {
     //   if (CWM.exists(args[0])) // Only try to load the domain if it exists.
    /*    {
          win.cwm = CWM.getInstance(args[0]);
          CwmSchemaFactoryInterface cwmSchemaFactory = Settings.getCwmSchemaFactory();
          win.schemaMeta = cwmSchemaFactory.getSchemaMeta(win.cwm);
          win.setDomainName(args[0]);
          win.schemaMeta.clearChanged();*/
      //  } else {
          win.newFile();
      //  }
    //  } else {
/*        if (win.props.openLastFile()) {
          String lastfiles[] = win.props.getLastFiles();
          if (lastfiles.length > 0) {
            try {
              if (CWM.exists(lastfiles[0])) // Only try to load the domain if it exists.
              {
                win.cwm = CWM.getInstance(lastfiles[0]);
                CwmSchemaFactoryInterface cwmSchemaFactory = Settings.getCwmSchemaFactory();
                win.schemaMeta = cwmSchemaFactory.getSchemaMeta(win.cwm);
                win.setDomainName(lastfiles[0]);
                win.schemaMeta.clearChanged();
              } else {
                win.newFile();
              }
            } catch (Exception e) {
              log.logError(Messages.getString(
                  "MetaEditor.ERROR_0001_CANT_CHECK_DOMAIN_EXISTENCE", e.toString())); //$NON-NLS-1$
              log.logError(Const.getStackTracker(e));
            }
          } else {
            win.newFile();
          }
        } else {*/
          win.newFile();
       // }
    //  }

    splash.hide();

    win.open();

    // Matcher pushButtonToPrNextStep =
    // allOf(widqetOfType(Button.class),withStyle(SWT.PUSH),withRegex("Proceed to step (.*) >"));

    new Thread( new Runnable() {

      @Override
      public void run() {
        try {
          // see SWTBotPreferences class
          SWTBotPreferences.PLAYBACK_DELAY = 100;
          System.out.println( "----------------------" );
          SWTBot bot = new SWTBot( win.getShell() );
          
          //make screenshot:
          // bot.captureScreenshot( "c:\\temp\\111.bmp" );

          // all shells
          // SWTBotShell[] shells = bot.shells();

          // menu name to console
          // System.out.println( "bot.menu(" + Messages.getString( "MetaEditor.USER_FILE" ) + "):"
          // + bot.menu( Messages.getString( "MetaEditor.USER_FILE" ) ) );

          // menu using
          bot.menu( "File" ).menu( "New..." ).menu( "Connection..." ).click();

          SWTBotShell currentBotShell = bot.activeShell();
          // currentBotShell to console
          // System.out.println( "currentBotShell: " + currentBotShell );

          // getId() for some reason is null
          // System.out.println( "currentBotShell.getId(): " + currentBotShell.getId() );

          //using textInGroup
          currentBotShell.bot().textInGroup( "Settings", 0 ).setText( "51456" );

          // work with list:
          // currentBotShell.bot().list( 0 ).select( 2 );

          // next code gets quantity of cLabels on the shell
          int cLabelElementsQuantity = 0;
          try {
            currentBotShell.bot().clabel( 1000 ).getText();
          } catch ( IndexOutOfBoundsException e ) {
            String exceptionMessage = e.getMessage();
            int startIndex = exceptionMessage.indexOf( "Size: " );
            cLabelElementsQuantity = Integer.valueOf( exceptionMessage.substring( startIndex + 6 ) );
          }

          // example of using cLabelElementsQuantity
          for ( int j = 0; j < cLabelElementsQuantity; j++ ) {
            String currentLabelText = currentBotShell.bot().clabel( j ).getText();
            System.out.println( currentLabelText );

            if ( !currentLabelText.isEmpty() ) {
              String currentLabelTrimedText = currentLabelText.trim();
              if ( currentLabelTrimedText.equals( CONNECTION_NAME ) ) {
                // System.out.println( currentBotShell.bot().textWithLabel( currentLabelText ).setText( "MyConnection" )
                // );
                currentBotShell.bot().textWithLabel( currentLabelText ).setText( "MyConnection" );
                currentBotShell.bot().textWithLabel( currentLabelText ).setFocus();
              } else {
                if ( currentLabelTrimedText.equals( DATABASE_NAME ) ) {
                  currentBotShell.bot().textWithLabel( currentLabelText ).setText( "MyDatabase" );
                  currentBotShell.bot().textWithLabel( currentLabelText ).setFocus();
                }
              }
            }
          }
          System.out.println( "----------------------" );
        } catch ( Exception e ) {
          e.printStackTrace();
        }

      }
    } ).start();

    while ( !win.isDisposed() ) {
      if ( !win.readAndDispatch() )
        win.sleep();
    }
    win.dispose();

    // Close the logfile...
    logwriter.close();
  }

}

