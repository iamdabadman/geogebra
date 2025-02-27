package org.geogebra.test.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.geogebra.common.util.AsyncOperation;
import org.geogebra.common.util.debug.Log;

/**
 * Collects strings and prints them into file.
 *
 */
public class ReportBuilder implements AsyncOperation<String> {
	OutputStreamWriter isw = null;

	/**
	 * @param filename
	 *            report filename
	 */
	public ReportBuilder(String filename) {
		final String path = "build" + File.separator + "reports";
		File dir = new File(path);
		dir.mkdirs();
		File f = new File(path + File.separator + filename);

		try {
			isw = new OutputStreamWriter(new FileOutputStream(f));
		} catch (FileNotFoundException e) {
			Log.debug(e);
		}
		Log.debug("file:///" + f.getAbsolutePath());
	}

	@Override
	public void callback(String content) {
		try {
			isw.write(content);
		} catch (IOException e) {
			Log.debug(e);
		}
	}

	/**
	 * Close the stream.
	 */
	public void close() {
		if (isw != null) {
			try {
				isw.close();
			} catch (IOException e) {
				Log.debug(e);
			}
		}
	}
}
