package org.garion.games.jeopardy.util;

import java.io.InputStream;
import gnu.io.CommPortIdentifier; 
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent; 
import gnu.io.SerialPortEventListener; 
import java.util.Enumeration;

import javax.swing.SwingUtilities;

public class ExternalPlayerReader implements Runnable, SerialPortEventListener {

		SerialPort serialPort;
	        /** The port we're normally going to use. */
		private static final String PORT_NAMES[] = { 
				"/dev/tty.usbserial-A9007UX1", // Mac OS X
				"/dev/tty.usbserial-A700fkGp",
				"/dev/tty.usbmodem411",
				"/dev/ttyUSB0", // Linux
				"COM3", // Windows
				};
		/** Buffered input stream from the port */
		private InputStream input;

		/** Milliseconds to block while waiting for port open */
		private static final int TIME_OUT = 2000;
		/** Default bits per second for COM port. */
		private static final int DATA_RATE = 9600;

		private final ExternalPlayerController controller;
		public ExternalPlayerReader(ExternalPlayerController controller) {
			this.controller = controller;
		}
		
		private void init() {
			CommPortIdentifier portId = null;
			
			@SuppressWarnings("unchecked")
			Enumeration<CommPortIdentifier> portEnum = (Enumeration<CommPortIdentifier>)CommPortIdentifier.getPortIdentifiers();

			// iterate through, looking for the port
			while (portEnum.hasMoreElements()) {
				CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
				for (String portName : PORT_NAMES) {
					if (currPortId.getName().equals(portName)) {
						portId = currPortId;
						break;
					}
				}
			}

			if (portId == null) {
				System.out.println("Could not find COM port, options were:");
				while(portEnum.hasMoreElements()) {
				  System.out.println(portEnum.nextElement());
				}
				return;
			}

			try {
				// open serial port, and use class name for the appName.
				serialPort = (SerialPort) portId.open(this.getClass().getName(),
						TIME_OUT);

				// set port parameters
				serialPort.setSerialPortParams(DATA_RATE,
						SerialPort.DATABITS_8,
						SerialPort.STOPBITS_1,
						SerialPort.PARITY_NONE);

				// open the streams
				input = serialPort.getInputStream();

				// add event listeners
				serialPort.addEventListener(this);
				serialPort.notifyOnDataAvailable(true);
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}

		/**
		 * This should be called when you stop using the port.
		 * This will prevent port locking on platforms like Linux.
		 */
		public synchronized void close() {
			if (serialPort != null) {
				serialPort.removeEventListener();
				serialPort.close();
			}
		}

		/**
		 * Handle an event on the serial port. Read the data and print it.
		 */
		public synchronized void serialEvent(SerialPortEvent oEvent) {
			if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
				try {
					int available = input.available();
					final byte chunk[] = new byte[available];
					input.read(chunk, 0, available);

					// Displayed results are codepage dependent
					final String result = new String(chunk);
					System.out.print(result);
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							
							try { 
								int r = Integer.parseInt(result);
								if(r == -1)
									controller.resetPlayer();
								else
									controller.signalPlayer("Player " + (r+1));
							} catch (Throwable t) {
								// there's nothing here we can do. swallow it.
							}
						}
					});
					
				} catch (Exception e) {
					System.err.println(e.toString());
				}
			}
			// Ignore all the other eventTypes, but you should consider the other ones.
		}

		@Override
		public void run() {
			init();
		}
	
}
