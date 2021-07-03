package cyph3r.myutils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class EncryptFile {
	public static int encryptFile(Path path) {
		byte[] bytes = new byte[]{};


		try {
			bytes = Files.readAllBytes(path);
		} catch (IOException e) {
			e.printStackTrace();
		}

		int[] d = new int[bytes.length];
		for (int i = 0; i < d.length; i++) {
			d[i] = Byte.toUnsignedInt(bytes[i]);
			d[i] <<= 0;
		}
		System.out.println(Arrays.toString(d));
		System.out.println(bytes.length);
		return 0;
	}

	public static void main(String[] args) {
		encryptFile(Paths.get("C:\\Mystuff\\Music\\[Drumstep] - Au5 & Fractal - Halcyon [Monstercat Release].m4a"));

	}
}
