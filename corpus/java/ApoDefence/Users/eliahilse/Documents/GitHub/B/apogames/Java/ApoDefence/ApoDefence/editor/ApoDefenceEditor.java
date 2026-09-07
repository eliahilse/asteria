package apoDefence.editor;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import apoDefence.ApoDefenceMain;

public class ApoDefenceEditor extends JComponent
{
	private static final long serialVersionUID = 1L;

	private ApoDefenceEditorGame	editor;
	private JScrollPane				ScrollPane;
	private ApoDefenceEditorHud		editorHud;
	
	public ApoDefenceEditor( ApoDefenceMain main )
	{
		super();
		
		this.setLayout( null );
		
		this.editor		= new ApoDefenceEditorGame();
        this.editor.setVisible( true );
        
        this.ScrollPane		= new JScrollPane( this.editor );
        this.ScrollPane.setLocation( 0, 0 );
        this.ScrollPane.setSize ( 640, 330 );
        this.ScrollPane.setVisible( true );
        
        this.add( this.ScrollPane );
        
        this.editorHud		= new ApoDefenceEditorHud( main, this.editor );
        this.editorHud.setLocation( 0, 330 );
        this.editorHud.setSize ( 640, 150 );
        this.editorHud.setVisible( true );
        
        this.add( this.editorHud );
        
        this.editor.setHud( this.editorHud );
	}

}
