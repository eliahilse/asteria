package tutorVolley;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Laedt JAVA-Klassen aus einen anzugebenden Verzeichnis.
 * Stammt aus den Sun JAVA-Tutorien.
 * Wird hier genutzt, um die Hamster neu zu laden.
 */
public class TutorVolleyClassLoader extends ClassLoader
{
	private String root;

	public TutorVolleyClassLoader (String rootDir)
	{
		if (rootDir == null)
			throw new IllegalArgumentException ("Null root directory");
		root = rootDir;
	}
	
	@SuppressWarnings("unchecked")
	protected Class loadClass (String name, boolean resolve) 
	throws ClassNotFoundException
	{
		Class c;
		try
		{

			c = findLoadedClass (name);

			if (c == null)
			{
				// Convert class name argument to filename
				// Convert package names into subdirectories
				String filename = name.replace ('.', File.separatorChar) + ".class";
				
				try
				{
					byte data[] = loadClassData(filename);
					c = defineClass (name, data, 0, data.length);
				} catch (IOException e)
				{
					// tritt auf, wenn die Datei nicht gefunden wurde
				} catch (NoClassDefFoundError e)
				{
					// tritt auf, wenn die Datei gefunden wurde,
					// aber eine Klasse enthaelt, die nicht dem Dateinamen
					// enspricht (Windows findet Dateien
					// auch bei falscher Gross-Klein-Schreibung)
				}
			}

			// Since all support classes of loaded class use same class loader
			// must check subclass cache of classes for things like Object
			if (c == null)
			{
				try
				{
					c = findSystemClass (name);
				} catch (Exception e)
				{
				}
			}

			if (c == null)
			{
				throw new ClassNotFoundException (name);
			}
			if (resolve)
				resolveClass (c);

		} catch(Throwable ex)
		{
			throw new ClassNotFoundException (name);
		}

		return c;
	}

	private byte[] loadClassData (String filename) 
	throws IOException
	{

		// Create a file object relative to directory provided
		File f = new File (root, filename);
		
		//System.out.println("f = "+f.toString());

		// Get size of class file
		int size = (int)f.length();

		// Reserve space to read
		byte buff[] = new byte[size];

		// Get stream to read from
		FileInputStream fis = new FileInputStream(f);
		DataInputStream dis = new DataInputStream (fis);

		// Read in data
		dis.readFully (buff);

		// close stream
		dis.close();

		// return data
		return buff;
	}
}
