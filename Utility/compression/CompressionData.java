package compression;

//Reference: https://en.wikipedia.org/wiki/Data_compression_ratio
public class CompressionData {
	public double compressionRatio(double uncompressedSize, double compressedSize) {
		return uncompressedSize / compressedSize;
	}
	
	public double spaceSavings(double uncompressedSize, double compressedSize) {
		return 1 - (compressedSize / uncompressedSize);
	}
}
