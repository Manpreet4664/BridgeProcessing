// Reference 1:https://www.journaldev.com/867/java-read-text-file
// Reference 2:https://www.javatpoint.com/java-printwriter-class
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class BridgeProcessing {

	public static final String BRIDGE_FDB_FILE = "BridgeFDB.txt";
	public static final String RANDOM_FRAMES_FILE = "RandomFrames.txt";

	private static HashMap<String, String> bridgeMap = new HashMap<>();
	private static HashMap<String, String[]> randomFrameMap = new HashMap<>();

	public static void main(String s[]) throws IOException {
		readFDBTextFile();
		readRandomFramesTextFile();
		performBridgeOperation(bridgeMap, randomFrameMap);
	}

	/**
	 * @return This method will be used to read the FDB Data and put it in a HashMap
	 */
	private static HashMap<String, String> readFDBTextFile() {
		String firstLine;
		String secondLine;
		try {
			FileReader fileReader = new FileReader(BRIDGE_FDB_FILE);
			BufferedReader bufferReader = new BufferedReader(fileReader);
			firstLine = bufferReader.readLine();
			while (firstLine != null) {
				secondLine = bufferReader.readLine();
				bridgeMap.put(firstLine, secondLine);
				firstLine = bufferReader.readLine();
			}
			fileReader.close();
			bufferReader.close();

		} catch (Exception e) {
			e.getMessage();
		}
		return bridgeMap;
	}

	/**
	 * @return This method will be used to read the random frames for which bridge
	 *         operation needs to be performed
	 */
	@SuppressWarnings("rawtypes")
	private static HashMap<String, String[]> readRandomFramesTextFile() {
		String firstLine;
		String secondLine;
		String thirdLine;

		try {
			FileReader fileReader = new FileReader(RANDOM_FRAMES_FILE);
			BufferedReader bufferReader = new BufferedReader(fileReader);
			firstLine = bufferReader.readLine();
			while (firstLine != null) {
				thirdLine = bufferReader.readLine();
				secondLine = bufferReader.readLine();
				String destination[] = new String[2];
				destination[0] = thirdLine;
				destination[1] = secondLine;
				randomFrameMap.put(firstLine, destination);
				firstLine = bufferReader.readLine();
			}
			fileReader.close();
			bufferReader.close();
		} catch (Exception e) {
			e.getMessage();
		}
		return randomFrameMap;
	}

	/**
	 * @param bridgeMap
	 * @param randomFrameMap
	 * @throws IOException This method will be used to perform the bridge operation
	 *                     and store the output in a text file.
	 */
	@SuppressWarnings("rawtypes")
	private static void performBridgeOperation(HashMap<String, String> bridgeMap,
			HashMap<String, String[]> randomFrameMap) throws IOException {

		StringBuilder outputString = new StringBuilder();
		for (Map.Entry randomMapElement : randomFrameMap.entrySet()) {
			String randomKey = (String) randomMapElement.getKey();
			for (Map.Entry bridgeMapElement : bridgeMap.entrySet()) {
				String bridgeKey = (String) bridgeMapElement.getKey();
				if (randomKey.equalsIgnoreCase(bridgeKey)) {
					String destination[] = (String[]) randomMapElement.getValue();
					if (destination[1].equals(bridgeMapElement.getValue())) {
						if (!destination[0].equals(bridgeMapElement.getKey())) {
							if (bridgeMap.containsKey((destination[0]))) {
								if (destination[1].equals(bridgeMap.get(destination[0]))) {
									outputString.append(bridgeKey + " " + destination[0] + " "
											+ bridgeMapElement.getValue() + " " + "Discard the frame");
									outputString.append("\n");
								} else {
									String val = bridgeMap.get(destination[0]);
									outputString.append(bridgeKey + " " + destination[0] + " "
											+ bridgeMapElement.getValue() + " " + "Forwarded on port " + val);
									outputString.append("\n");
								}
							}
						}
					} else {
						outputString.append(
								randomKey + " " + destination[0] + " " + destination[1] + " Broadcast the frame");
						outputString.append("\n");
					}
				}
			}

		}
		File outputFile = new File("BridgeOutput.txt");
		FileWriter fileWriter = new FileWriter(outputFile, true);
		PrintWriter printWriter = new PrintWriter(fileWriter);
		printWriter.print(outputString);
		printWriter.close();
	}
}
