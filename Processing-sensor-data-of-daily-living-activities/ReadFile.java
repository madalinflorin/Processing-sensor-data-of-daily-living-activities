import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReadFile {
	private final static DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
	private final static DateFormat formatWithoutTime = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
	private final static String fileName = "C:\\Users\\MADALIN\\Desktop\\Activities.txt";
	private static int i = 1;

	public static ArrayList<MonitoredData> readFromFile() {
		ArrayList<MonitoredData> dataM = new ArrayList<>();
		// read file into stream, try-with-resources
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

			stream.forEach(line -> {
				String[] sir = line.split("\t");
				MonitoredData data = new MonitoredData();
				try {
					Date startDate = format.parse(sir[0]);
					Date endDate = format.parse(sir[2]);
					long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
					long diff = TimeUnit.MILLISECONDS.toSeconds(diffInMillies) % 60;
					long diff1 = TimeUnit.MILLISECONDS.toMinutes(diffInMillies) % 60;
					long diff2 = TimeUnit.MILLISECONDS.toHours(diffInMillies);

					data.setDurationHours(diff2);
					data.setDurationMinutes(diff1);
					data.setDurationSeconds(diff);
					data.setStartTime(startDate);
					data.setEndTime(endDate);
					data.setActivityLabel(sir[4]);
					dataM.add(data);

				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			});

		} catch (IOException e) {
			e.printStackTrace();
		}
		return dataM;
	}

	public static long counterDays(ArrayList<MonitoredData> list) {

		ArrayList<String> dates = new ArrayList<>();

		list.stream().forEach(elem -> {
			dates.add(formatWithoutTime.format(elem.getStartTime()));
			dates.add(formatWithoutTime.format(elem.getEndTime()));
		});

		return dates.stream().distinct().count();
	}

	public static HashMap<String, Integer> getActivity(ArrayList<MonitoredData> list) {
		HashMap<String, Integer> hActivity = new HashMap<>();

		list.stream().forEach(elem -> {
			String activity = elem.getActivityLabel();
			Integer v = hActivity.get(activity);
			if (v == null)
				hActivity.put(activity, 1);
			else
				hActivity.put(activity, ++v);
		});

		return hActivity;

	}

	public static HashMap<String, Integer> getActivitySub5(ArrayList<MonitoredData> list) {
		HashMap<String, Integer> hActivity = new HashMap<>();

		list.stream().forEach(elem -> {
			String activity = elem.getActivityLabel();
			Integer v = hActivity.get(activity);
			if (v == null && elem.getDurationMinutes() < 5 && elem.getDurationHours() == 0)
				hActivity.put(activity, 1);
			else if (elem.getDurationMinutes() < 5 && elem.getDurationHours() == 0)
				hActivity.put(activity, ++v);

		});

		list.stream().forEach(elem -> {
			String activity = elem.getActivityLabel();
			Integer v = hActivity.get(activity);
			if (v == null)
				hActivity.put(activity, 0);

		});

		return hActivity;

	}
	
	public static HashMap<String, Long> getDurationActivity(ArrayList<MonitoredData> list) {
		HashMap<String, Long> hActivity = new HashMap<>();

		list.stream().forEach(elem -> {
			String activity = elem.getActivityLabel();
			long hours = 0, minutes = 0, seconds = 0;
			hours = elem.getDurationHours();
			minutes = elem.getDurationMinutes();
			seconds = elem.getDurationSeconds();
			long timeSeconds = seconds + 60 * minutes + 3600 * hours;
			Long v = hActivity.get(activity);
			if (v == null)
				hActivity.put(activity, timeSeconds);
			else
				hActivity.put(activity, v + timeSeconds);
		});

		return hActivity;

	}

	public static void p4(ArrayList<MonitoredData> list) throws FileNotFoundException {

		PrintWriter writerFile = new PrintWriter("f4.txt");

		Map<Integer, Map<String, Long>> theMap = list.stream().collect(Collectors.groupingBy(MonitoredData::getDay,
				Collectors.groupingBy(MonitoredData::getActivityLabel, Collectors.counting())));

		theMap.forEach((x, y) -> writerFile.println(x + " " + y));

		writerFile.close();

	}

	public static void p7(HashMap<String, Integer> list) throws FileNotFoundException {

		PrintWriter writerFile = new PrintWriter("f7.txt");

		Set<String> activities3 = list.keySet();
		activities3.stream().forEach(activity -> writerFile.println(activity + " : " + list.get(activity)));

		writerFile.close();

	}

	public static void main(String args[]) {
		ArrayList<MonitoredData> dataM = ReadFile.readFromFile();

		HashMap<String, Integer> h = getActivity(dataM);
		HashMap<String, Long> h1 = getDurationActivity(dataM);
		HashMap<String, Integer> h2 = getActivitySub5(dataM);
		HashMap<String, Integer> h3 = new HashMap<>();
		Set<String> activities = h.keySet();
		Set<String> activities1 = h1.keySet();
		Set<String> activities2 = h2.keySet();
		System.out.println("Nr. days of monitored data appears in the log: ");
		long counterDays = counterDays(dataM);
		System.out.println(counterDays);
		System.out.println();
		System.out.println("Nr. of each activity over the entire monitoring period. ");
		activities.stream().forEach(activity -> System.out.println(activity + " : " + h.get(activity)));
		System.out.println();
		System.out.println("Nr. of each activity for each day over the monitoring period: -> f4.txt");
		System.out.println();
		System.out.println("Duration recorded on each line: ");
		activities1.stream().forEach(activity -> System.out.println(activity + "- Hours: " + h1.get(activity) / 3600
				+ ", minutes: " + h1.get(activity) / 60 % 60 + ", seconds: " + h1.get(activity) % 60));
		System.out.println();
		System.out.println(
				"Activities that have 90% of the monitoring records with duration less than 5\r\n" + "minutes: -> f7.txt");
		System.out.println();
		System.out.println("BONUS: Activities with duration less than 5 minutes"); 
		
		activities2.stream().forEach(activity -> System.out.println(activity + " : "
		 + h2.get(activity)));
		activities.stream().forEach(activity -> {
			if (h.get(activity) * 90 / 100 <= h2.get(activity))
				h3.put(activity, i);
			i++;
		});
		try {
			p4(dataM);
			p7(h3);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
