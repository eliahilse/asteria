package apoDefence;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JComponent;
import javax.swing.JFrame;

import apoDefence.editor.ApoDefenceEditor;
import apoDefence.game.ApoDefenceGame;
import apoDefence.options.ApoDefenceOptions;

/**
 * Startklasse, die die einzelnen JComponent handelt
 * @author Dirk Aporius
 *
 */
public class ApoDefenceMain extends JFrame
{
	private static final long 		serialVersionUID = 1L;

	private ApoDefenceEditor	editor;
	
	private ApoDefenceGame			game;
	private ApoDefenceOptions		options;
	
	private JComponent				currentComponent;
	
	private Cursor					defenceCursor;
	
	public ApoDefenceMain()
	{
		super();
		
		this.setTitle( "=== ApoDefence ===" );
		
		this.setLayout( null );
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.editor			= new ApoDefenceEditor( this );
		this.editor.setLocation( 0, 0 );
		this.editor.setSize( 640, 480 );
		
		this.game			= new ApoDefenceGame( this );
		this.game.setSize( 640, 480 );
		this.game.setLocation( 0, 0 );
		
		this.options		= new ApoDefenceOptions( this, this.game );
		this.options.setSize( 640, 480 );
		this.options.setLocation( 0, 0 );
		
		this.currentComponent	= this.options;
		
		this.add( this.currentComponent );
		
		this.addKeyListener( this.game );
		
		this.setUndecorated( false );
		this.setResizable( false );
		
		ApoDefenceImage	image		= new ApoDefenceImage();
		this.setIconImage( image.getPic( "/images/icon.gif", false ) );
		
		
		this.defenceCursor = Toolkit.getDefaultToolkit().createCustomCursor( image.getPic( "/images/mouse_cursor.png", false ), new Point(5,2), "DefenceC");
        this.setCursor( this.defenceCursor );
		
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	/**
	 * gibt die Größe der Applikation zurück
	 */
	public Dimension getPreferredSize()
	{
		return new Dimension( 640 + 6, 480 + 32);
	}
	
	/**
	 *	entfernt das jetztige JPanel und sagt, dass das aktuelle JPanel nun das Spielfeld ist
	 */
	public void setGame()
	{
		super.remove( this.currentComponent );
		this.currentComponent	= this.game;
		this.requestFocus();
		this.game.start();
		super.add( this.currentComponent );
		super.validate();
		this.currentComponent.repaint();
	}
	
	/**
	 *	entfernt das jetztige JPanel und sagt, dass das aktuelle JPanel nun die Optionen darstellen
	 */
	public void setOptions()
	{
		this.game.stop();
		super.remove( this.currentComponent );
		this.currentComponent	= this.options;
		super.add( this.currentComponent );
		super.validate();
		this.currentComponent.repaint();
	}
	
	/**
	 *	entfernt das jetztige JPanel und sagt, dass das aktuelle JPanel nun den Editor darstellen
	 */
	public void setEditor()
	{
		super.remove( this.currentComponent );
		this.currentComponent	= this.editor;
		super.add( this.currentComponent );
		super.validate();
		this.currentComponent.repaint();
	}
	
	public static void main( String[] args )
	{
		new ApoDefenceMain();
	}

	/**
	 * gibt das JComponent Objekt des Spieles zurück
	 * @return gibt das JComponent Objekt des Spieles zurück
	 */
	public ApoDefenceGame getGame()
	{
		return this.game;
	}
	
	/**
	 * gibt zurück, ob die Werte die eingegeben sind auch die Original Werte sind
	 * @return gibt zurück, ob die Werte die eingegeben sind auch die Original Werte sind
	 */
	public boolean isOriginal()
	{
		return this.options.isOriginal();
	}

}
