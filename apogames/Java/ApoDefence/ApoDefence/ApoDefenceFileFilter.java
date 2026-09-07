package apoDefence;

import java.io.File;

/**
 * Klasse, um im OpenDialog nur bestimmte Dateien zu sehen und
 * nicht alle anderen Dateiformate
 * @author Dirk Aporius
 *
 */
public class ApoDefenceFileFilter extends javax.swing.filechooser.FileFilter 
{
	String s	= "";
	
	public ApoDefenceFileFilter( String s )
	{
		this.s		= s;
	}
	
	public String getLevelName()
	{
		return "."+this.s;
	}
	
    public String getExtension(File file) 
    {
        String s = "";
        String s1 = file.getName();
        int i = s1.lastIndexOf(46);
        if(i > 0 && i < s1.length() - 1)
            s = s1.substring(i + 1).toLowerCase();
        return s;
    }

    /**
     * was soll nur angezeigt werden
     */
    public String getDescription() 
    {
        return "Nur "+this.s+"-Dateien";
    }

    /**
     * akzeptiert nur class Dateien, alle anderen nimmt er nicht an
     */
    public boolean accept(File file) 
    {
        if(file.isDirectory())
            return true;
        String s = getExtension(file);
        if(s != null)
            return ((s.equals(this.s)));
        else
            return false;
    }
        
    public ApoDefenceFileFilter() 
    {
    }
}
