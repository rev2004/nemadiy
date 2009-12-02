package org.imirsel.nema.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;


class ProcessOutputReceiver implements Runnable
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
	ProcessOutputReceiver( InputStream is, PrintStream cout )
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
					final BufferedReader br = new BufferedReader( new InputStreamReader( is ));
					String line;
					while ( ( line = br.readLine() ) != null )
					{
						//System.out.println( line );
						cout.println( line );
						cout.flush();
					}
					br.close();
				}
				catch ( IOException e )
				{
					throw new IllegalArgumentException( "IOException receiving data from child process." );
				}
		
		
		
		
		/*
		
		int bufferSize = 1048576; // 1 mb buffer
		byte[] OutputBuffer = new byte[bufferSize];
		int OutputBufferIndex = 0;
		int numBytes = 0;
		
		BufferedInputStream bis = new BufferedInputStream(is, bufferSize);

		while (true) {
			int numBytesAvailable = 0;
			try {
				numBytesAvailable = bis.available();
				cout.println("bis.available: " + numBytesAvailable);
				cout.flush();
			} catch (IOException e) {
				System.out.println("inputStream.available() error!!!");
				e.printStackTrace();
			}

			if (numBytesAvailable == 0) {
				break;
			}

			try {
				numBytes = bis.read(OutputBuffer, OutputBufferIndex, numBytesAvailable);
				if (numBytes != numBytesAvailable) {
					System.out.println("numBytes != numBytesAvailable");
					throw new IOException();
				}
			} catch (IOException e) {
				System.out.println("inputStream.read() error!!!");
				e.printStackTrace();
			}

			cout.print(new String(OutputBuffer, OutputBufferIndex, numBytesAvailable));
			cout.flush();
			OutputBufferIndex += numBytesAvailable;
		}
		try {
			bis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("error closing buffered input stream!!!");
			e.printStackTrace();
		}
		
		*/

	}
}
