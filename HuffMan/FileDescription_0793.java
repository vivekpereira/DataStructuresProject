// Vivek Pereira  cs610 0793 prp
import java.io.Serializable;

public class FileDescription_0793 implements Serializable {
	private int fileSize = 0;
	private String fileName;

	private int[] frequency = new int[256];

	public int getFileSize_0793() {
		return fileSize;
	}

	public void setFileSize_0793(int fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileName_0793() {
		return fileName;
	}

	public void setFileName_0793(String fileName) {
		this.fileName = fileName;
	}

	public int[] getFrequency_0793() {
		return frequency;
	}

	public void setFrequency_0793(int[] frequency) {
		this.frequency = frequency;
	}
}
