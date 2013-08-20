package ap.minecraft.chunkthing.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

public class FileHandler {
	private String fileName;
	private File file;

	public FileHandler(String _fileName, boolean open) {
		this.fileName = _fileName;
		if (open) {
			this.open();
		}
	}
	
	public void open()  {
		if(this.fileName != null) {
			this.file = new File(this.fileName);
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(this.file);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			GZIPInputStream giz = null;
			try {
				giz = new GZIPInputStream(fis);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				byte b = (byte)giz.read();
				System.out.println("Byte b: " + b);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
}
