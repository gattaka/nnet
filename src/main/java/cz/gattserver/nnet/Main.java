package cz.gattserver.nnet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import cz.gattserver.nnet.net.NNet;
import cz.gattserver.nnet.net.Sigmoid;
import cz.gattserver.nnet.net.SmoothReLU;

public class Main {

	public static void main(String[] args) throws IOException {

		File trainImages = new File("train-images.idx3-ubyte");
		File trainLabels = new File("train-labels.idx1-ubyte");
		try (FileInputStream isi = new FileInputStream(trainImages);
				FileInputStream isl = new FileInputStream(trainLabels)) {

			// magic number
			byte[] buffer = new byte[4];
			isi.read(buffer);
			int magicNumber = ByteBuffer.wrap(buffer).getInt();
			if (magicNumber != 0x00000803)
				throw new IllegalArgumentException("Invalid Magic number of images file (should be 0x00000803)");

			isl.read(buffer);
			magicNumber = ByteBuffer.wrap(buffer).getInt();
			if (magicNumber != 0x00000801)
				throw new IllegalArgumentException("Invalid Magic number of images file (should be 0x00000803)");

			// number of images
			isi.read(buffer);
			long imageCount = 0;
			imageCount |= buffer[0] & 0xFF;
			imageCount <<= 8;
			imageCount |= buffer[1] & 0xFF;
			imageCount <<= 8;
			imageCount |= buffer[2] & 0xFF;
			imageCount <<= 8;
			imageCount |= buffer[3] & 0xFF;

			isl.read(buffer);
			long labelsCount = 0;
			labelsCount |= buffer[0] & 0xFF;
			labelsCount <<= 8;
			labelsCount |= buffer[1] & 0xFF;
			labelsCount <<= 8;
			labelsCount |= buffer[2] & 0xFF;
			labelsCount <<= 8;
			labelsCount |= buffer[3] & 0xFF;
			if (imageCount != labelsCount)
				throw new IllegalArgumentException("Image count and labels count must be the same");

			System.out.println("Image count: " + imageCount);

			// rows
			isi.read(buffer);
			int rows = ByteBuffer.wrap(buffer).getInt();

			// cols
			isi.read(buffer);
			int cols = ByteBuffer.wrap(buffer).getInt();
			System.out.println("Image dimensions: " + rows + "x" + cols);

			int pixels = rows * cols;
			int batchSize = 100;

			int results = 10;

			NNet net = new NNet(new int[] { pixels, results * 2, results }, new Sigmoid());

			int imagesProcessed = 0;
			int batchCount = (int) imageCount / batchSize;
			int batchId = 0;
			double[][][] trainBatchInputs = new double[batchCount][batchSize][pixels];
			double[][][] trainBatchOutputs = new double[batchCount][batchSize][results];

			while (imagesProcessed < imageCount) {
				for (int b = 0; b < batchSize; b++) {
					imagesProcessed++;
					if (imagesProcessed > imageCount)
						break;

					byte[] image = new byte[pixels];
					isi.read(image);
					byte[] label = new byte[1];
					isl.read(label);

					for (int r = 0; r < rows; r++) {
						for (int c = 0; c < cols; c++) {
							int i = r * cols + c;
							trainBatchInputs[batchId][b][i] = (image[i] & 0xFF) / 255.0;
							trainBatchOutputs[batchId][b][label[0]] = 1;
						}
					}
				}
				batchId++;
				System.out.println("Images processed " + imagesProcessed + "/" + imageCount);
			}

			batchId = 0;
			do {
				double[][] batchInputs = new double[batchSize][pixels];
				double[][] batchOutputs = new double[batchSize][results];
				for (int b = 0; b < batchSize; b++) {
					int rnd = (int) (Math.random() * batchCount);
					batchInputs = trainBatchInputs[rnd];
					batchOutputs = trainBatchOutputs[rnd];
				}

				net.train(batchInputs, batchOutputs, 0.8, 0.4);
				batchId = (batchId + 1) % batchCount;
			} while (net.getBatchAverageError() > 0.1);
			net.writeConfig(new File("target/nnet.json"));
		}

	}

}
