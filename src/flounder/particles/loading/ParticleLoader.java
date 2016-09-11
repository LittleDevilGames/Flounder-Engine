package flounder.particles.loading;

import flounder.engine.*;
import flounder.particles.*;
import flounder.resources.*;
import flounder.textures.*;

import java.io.*;
import java.lang.ref.*;
import java.util.*;

/**
 * Class capable of loading particle from a .particle file.
 */
public class ParticleLoader {
	private static Map<String, SoftReference<ParticleTemplate>> loadedTypes = new HashMap<>();

	public static ParticleTemplate load(String name) {
		SoftReference<ParticleTemplate> ref = loadedTypes.get(name);
		ParticleTemplate data = ref == null ? null : ref.get();

		if (data == null) {
			FlounderEngine.getLogger().log(name + " is being loaded into a particle type right now!");
			loadedTypes.remove(name);

			// Creates the file reader.
			MyFile saveFile = new MyFile(FlounderParticles.PARTICLES_LOC, name + ".particle");

			try {
				BufferedReader fileReader = saveFile.getReader();

				if (fileReader == null) {
					FlounderEngine.getLogger().error("Error creating reader the particle file: " + saveFile);
					return null;
				}

				// Loaded data.
				String particleName = "unnamed";
				String textureFile = "/";
				String numberOfRows = "1";
				String lifeLength = "1.0f";
				String scale = "1.0f";

				// Current line.
				String line;

				// Each line read loop.
				while ((line = fileReader.readLine()) != null) {
					// Entity General Data.
					if (line.contains("ParticleData")) {
						while (!(line = fileReader.readLine()).contains("};")) {
							if (line.contains("Name")) {
								particleName = line.replaceAll("\\s+", "").replaceAll(";", "").substring("Name:".length());
							} else if (line.contains("Texture")) {
								textureFile = line.replaceAll("\\s+", "").replaceAll(";", "").substring("Texture:".length());
							} else if (line.contains("NumberOfRows")) {
								numberOfRows = line.replaceAll("\\s+", "").replaceAll(";", "").substring("NumberOfRows:".length());
							} else if (line.contains("LifeLength")) {
								lifeLength = line.replaceAll("\\s+", "").replaceAll(";", "").substring("LifeLength:".length());
							} else if (line.contains("Scale")) {
								scale = line.replaceAll("\\s+", "").replaceAll(";", "").substring("Scale:".length());
							}
						}
					}
				}

				Texture texture = Texture.newTexture(new MyFile(textureFile)).create();
				texture.setNumberOfRows(Integer.parseInt(numberOfRows));
				data = new ParticleTemplate(particleName, texture, Float.parseFloat(lifeLength), Float.parseFloat(scale));
			} catch (IOException e) {
				FlounderEngine.getLogger().error("File reader for particle " + saveFile.getPath() + " did not execute successfully!");
				FlounderEngine.getLogger().exception(e);
				return null;
			}

			loadedTypes.put(name, new SoftReference<>(data));
		}

		return data;
	}
}
