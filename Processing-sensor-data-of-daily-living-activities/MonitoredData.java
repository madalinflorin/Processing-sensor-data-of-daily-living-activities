import java.util.Date;

public class MonitoredData {
	private Date startTime;
	private Date endTime;
	private String activityLabel;
	private long hoursDuration;
	private long minutesDuration;
	private long secondsDuration;

	public MonitoredData(Date a, Date b, String c,int d,int e,int f) {
		this.startTime = a;
		this.endTime = b;
		this.activityLabel = c;
		this.hoursDuration=d;
		this.minutesDuration=e;
		this.secondsDuration=f;
	}
	public MonitoredData() {
	
	}

	public Date getStartTime() {
		return this.startTime;
	}

	public Date getEndTime() {
		return this.endTime;
	}

	public String getActivityLabel() {
		return this.activityLabel;
	}
	
	public long getDurationHours() {
		return this.hoursDuration;
	}
	
	public long getDurationMinutes() {
		return this.minutesDuration;
	}
	
	public long getDurationSeconds() {
		return this.secondsDuration;
	}
	
	@SuppressWarnings("deprecation")
	public int getDay() {
		return this.startTime.getDate();
	}

	public void setStartTime(Date a) {
		this.startTime = a;
	}

	public void setEndTime(Date a) {
		this.endTime = a;

	}

	public void setActivityLabel(String a) {
		this.activityLabel = a;
	}
	
	public void setDurationHours(long a) {
		this.hoursDuration = a;
	}
	
	public void setDurationMinutes(long a) {
		this.minutesDuration = a;
	}
	
	public void setDurationSeconds(long a) {
		this.secondsDuration = a;
	}
	
	public String toString() {
		
		return startTime+" "+endTime+" "+activityLabel;
	}
}