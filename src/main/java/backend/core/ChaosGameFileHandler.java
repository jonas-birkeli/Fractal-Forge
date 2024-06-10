package backend.core;

import backend.geometry.Complex;
import backend.geometry.MatrixNxN;
import backend.geometry.Vector;
import backend.transforms.AffineTransform;
import backend.transforms.JuliaTransform;
import backend.transforms.Transform;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Logger;


/**
 * Represents a chaos game file handler.
 * Supports reading and writing chaos game descriptions from and to files.
 *
 * @version 1.1
 * @author proggang
 * @since 04.04.2024
 */
public class ChaosGameFileHandler {

  /**
   * Prevent instantiation.
   *
   * @since 1.0
   */
  protected ChaosGameFileHandler() {
    // Prevent instantiation
  }

  /**
   * Reads a chaos game description from the given file.
   * The file should contain the type of the chaos game description.
   * Disregards lines starting with '#'.
   *
   * @param path the path of the file to read from
   * @return the chaos game description read from the file
   * @since 1.0
   */
  public static ChaosGameDescription readFromFile(String path) {
    // Initialize variables to null to avoid NullPointerExceptions.

    ChaosGameDescription.ChaosGameDescriptionBuilder builder = new ChaosGameDescription
        .ChaosGameDescriptionBuilder();

    try (Scanner scanner = new Scanner(new File(path))) {
      String type = scanner.nextLine().replaceAll("\\s*#.*", "").trim();
      // Used to determine the type of the chaos game description.

      String minCoordsLine = scanner.nextLine().replaceAll("\\s*#.*", "").trim();
      String maxCoordsLine = scanner.nextLine().replaceAll("\\s*#.*", "").trim();

      builder.minCoords(getVectorFromString(minCoordsLine));
      builder.maxCoords(getVectorFromString(maxCoordsLine));

      if (type.equals("Affine2D")) {
        boolean end = false;
        while (!end) {
          String line = scanner.nextLine().replaceAll("\\s*#.*", "").trim();
          if (line.isEmpty()) {
            // Check whether the line below is empty, like in the state save config file.
            // Skip the empty line.
            end = true;
          } else if (line.equals("Probability")) {
            // Check if the line is "Probability".
            // Skip the line and read the next line.
            String probabilityLine = scanner.nextLine().replaceAll("\\s*#.*", "").trim();
            builder.probability(getVectorFromString(probabilityLine));
            end = true;
          } else {
            builder.addTransform(getTransformFromString(line));
          }


          // Check if there are no more lines to read.
          if (!scanner.hasNextLine()) {
            end = true;
          }
        }
      } else if (type.equals("Julia")) {
        boolean end = false;
        while (!end) {
          String line = scanner.nextLine().replaceAll("\\s*#.*", "").trim();
          if (line.isEmpty()) {
            // Check whether the line below is empty, like in the state save config file.
            // Skip the empty line.
            System.out.println("empty");
            end = true;
          } else if (line.equals("Probability")) {
            // Check if the line is "Probability".
            // Skip the line and read the next line.
            String probabilityLine = scanner.nextLine().replaceAll("\\s*#.*", "").trim();
            builder.probability(getVectorFromString(probabilityLine));
            end = true;
          } else {
            Vector v = getVectorFromString(line);
            Complex point = new Complex(v.getElement(0), v.getElement(1));
            // Only supports JuliaTransforms with 2 elements, hardcoded.

            builder.addTransform(new JuliaTransform(point, 1));
          }


          // Check if there are no more lines to read.
          if (!scanner.hasNextLine()) {
            end = true;
          }
        }

      }
    } catch (NoSuchElementException e) {
      Logger.getGlobal()
          .severe("File is empty or does not contain a valid chaos game description." + e);
      return null;
    } catch (FileNotFoundException e) {
      Logger.getGlobal().severe("File not found: " + path + e);
      return null;
    } catch (Exception e) {
      Logger.getGlobal().severe("Error reading chaos game description from file: " + path + e);
      return null;
    }

    return builder.build();
  }

  /**
   * Writes the given chaos game description to the given file.
   *
   * @param description the chaos game description to write
   * @param path the path of the file to write to
   * @since 1.0
   */
  public static void writeToFile(ChaosGameDescription description, String path) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
      // Copilot suggested using the instanceof operator
      // to determine the type of the chaos game description.
      if (description.getTransforms().get(0) instanceof AffineTransform) {
        writer.write("Affine2D");
      } else if (description.getTransforms().get(0) instanceof JuliaTransform) {
        writer.write("Julia");
      }

      writer.write("\n");
      writer.write(description.getMinCoords().getElement(0)
          + ", "
          + description.getMinCoords().getElement(1));
      writer.write("\n");
      writer.write(description.getMaxCoords().getElement(0)
          + ", "
          + description.getMaxCoords().getElement(1));
      writer.write("\n");

      for (Transform transform : description.getTransforms()) {
        writer.write(transform.getDetailsAsString());
        writer.write("\n");
      }

      if (description.getProbability() != null) {
        writer.write("Probability");
        writer.write("\n");
        for (int i = 0; i < description.getProbability().getSize() - 1; i++) {
          writer.write("" + description.getProbability().getElement(i));
          writer.write(", ");
        }
        writer.write(
            ""
                + description.getProbability().getElement(
                    description.getProbability().getSize() - 1
            )
        );
        writer.write("\n");
      }

    } catch (IOException e) {
      throw new IOException("Could not write to file. " + e);
    }
  }

  /**
   * Generates a transform based on the given comma separated string.
   * The string should contain the values of the matrix and the vector with 6 double values.
   *
   * @param line the comma separated string containing the values of the matrix and the vector
   * @return the transform generated from the given string
   * @throws IllegalArgumentException if the given string does not contain 6 double values
   * @since 1.1
   */
  private static Transform getTransformFromString(String line) throws IllegalArgumentException {
    String[] splitLine = line.split(", ");
    Double[] values = new Double[splitLine.length];

    for (int i = 0; i < splitLine.length; i++) {
      values[i] = Double.parseDouble(splitLine[i]);
    }
    int size = (int) Math.floor(Math.sqrt(values.length));
    // Floors the size
    // example: sqrt(72) = 8.4852813742
    // floor(8.4852813742) = 8
    // 8*8 + 8 = 72

    double[] matrixChunk = new double[size * size];
    double[] vectorChunk = new double[size];

    for (int i = 0; i < size * size; i++) {
      matrixChunk[i] = values[i];
    }

    // Copy remaining elements for vectorChunk
    for (int i = size * size, j = 0; i < values.length; i++, j++) {
      vectorChunk[j] = values[i];
    }

    MatrixNxN m = new MatrixNxN(matrixChunk);
    Vector v = new Vector(vectorChunk);

    return new AffineTransform(m, v);
  }

  /**
   * Generates a vector based on the given comma separated string.
   * The string should contain the values of the vector with two double values.
   *
   * @param line the comma separated string containing the values of the vector
   * @return the vector generated from the given string
   * @since 1.1
   */
  private static Vector getVectorFromString(String line) {
    String[] splitLine = line.trim().split(", ");
    double[] values = new double[splitLine.length];

    for (int i = 0; i < splitLine.length; i++) {
      values[i] = Double.parseDouble(splitLine[i]);
    }

    return new Vector(values);
  }
}