package no.evote.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import no.evote.exception.EvoteException;

public class ZipFileIterator implements Iterator<File> {
	private final ZipInputStream zipInputStream;
	private final File temporaryDirectory;
	private File nextFile = null;

	public ZipFileIterator(final File zipFile, final File temporaryDirectory) throws FileNotFoundException {
		FileInputStream fileInputStream = new FileInputStream(zipFile);
		zipInputStream = new ZipInputStream(fileInputStream);

		this.temporaryDirectory = temporaryDirectory;
	}

	@Override
	public boolean hasNext() {
		if (nextFile != null) {
			return true;
		} else {

			File file = null;
			ZipEntry zipEntry;
			do {
				try {
					zipEntry = zipInputStream.getNextEntry();

					if (zipEntry != null) {
						file = IOUtil.makeFile(temporaryDirectory, zipEntry, zipInputStream);
					}

				} catch (IOException e) {
					throw new EvoteException(e.getMessage(), e);
				}
			} while (file == null && zipEntry != null);

			nextFile = file;

			if (file == null) {
				return false;
			}

			return true;
		}
	}

	@Override
	@SuppressWarnings("PMD.CompareObjectsWithEquals")
	public File next() {
		try {
			return nextFile;
		} finally {
			nextFile = null;
		}
	}

	@Override
	public void remove() {
		throw new EvoteException("Not implemented.");
	}

	public void close() throws IOException {
		if (zipInputStream != null) {
			zipInputStream.close();
		}
	}

}
