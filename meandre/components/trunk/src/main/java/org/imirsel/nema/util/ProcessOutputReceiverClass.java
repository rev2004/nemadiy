package org.imirsel.nema.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;


class ProcessOutputReceiverClass implements Runnable
{
	// ------------------------------ FIELDS ------------------------------

	/**
	 * stream to receive data from child
	 */
	private final InputStream is;
	PrintStream cout;

	//--------------------------- CONSTRUCTORS ---------------------------

	/**
	 * constructor
	 *
	 * @param is stream to receive data from child
	 */
	ProcessOutputReceiverClass( InputStream is, PrintStream cout )
	{
		this.is = is;
		this.cout = cout;
	}

	// -------------------------- PUBLIC INSTANCE  METHODS --------------------------
	/**
	 * method invoked when Receiver thread started.  Reads data from child and displays in on System.out.
	 */
	public void run()
	{
		try
		{
			final BufferedReader br = new BufferedReader( new InputStreamReader( is ), 1048576 );
			String line;
			while ( ( line = br.readLine() ) != null )
			{
				cout.println( line );
				cout.flush();
			}
			br.close();
		}
		catch ( IOException e )
		{
			throw new IllegalArgumentException( "IOException receiving data from child process." );
		}
	}


}
