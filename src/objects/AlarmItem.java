package objects;

import java.util.Date;

public class AlarmItem {
	
	private Date begin, end, last;
	private String type;
	private String process;
	private String action;
	private String log;
	private int occurences;
	private int warning;

	public AlarmItem(Date dat, String typ, String proc, String act, String log){
		begin = dat;
		end = dat;
		last = dat;
		type = typ;
		process = proc;
		action = act;
		this.log = log;
		occurences = 1;
		warning = 0;
	}
	
	public static void main(String[] args) {
		//AlarmItem a = new AlarmItem();
		System.out.println("Date: " + 16589758/1000 % 60);
	}

	
	/*
	 * Getters and Setters
	 */
	public Date getBegin() {
		return begin;
	}

	public Date getEnd() {
		return end;
	}

	public Date getInstant() {
		return last;
	}

	public String getType() {
		return type;
	}

	public String getProcess() {
		return process;
	}

	public String getAction() {
		return action;
	}

	public int getOccurences() {
		return occurences;
	}
	
	public int getWarning() {
		return warning;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public void setInstant(Date last) {
		this.last = last;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
	public void setWarning(int w) {
		warning = w;
	}
	
	
	
	//Others:
	public void addLogLine(String log) {
		this.log = this.log + "\n" + log;
	}

	public void addOccurence() {
		occurences++;
	}
	
	public long getDurationMs(){
		return end.getTime() - begin.getTime();
	}
	
	public String getDuration(){
		try{
			long diff = getDurationMs();
	        long diffSeconds = diff / 1000 % 60;
	        long diffMinutes = diff / (60 * 1000) % 60;
	        long diffHours = diff / (60 * 60 * 1000);
			return diffHours+"h"+diffMinutes+"m"+diffSeconds+"s";
		}catch(Exception e){
			return "Error: " + e.toString();
		}
	}	
	//Update item
	public void alarmUpdate(Date parsedDate, String line){
		addOccurence();
		addLogLine(line);
		last = end;
		end = parsedDate;
		
		long difTempo = end.getTime() - last.getTime();
		if (difTempo < 20000) {
			warning = warning + 10;
		} else if (difTempo < 60000){
			warning = warning + 5;
		} else{
			warning = warning + 1;
		}
		warning = warning/2;
	}
	
	public String toString(){
		return "Process: *" + process + "*\nAction: " + getAction() +
				"\nType: " + getType() + 
				"\nOccurrences: " + getOccurences() + 
				"\n" + "{noformat}\n" + log + "\n{noformat}\n";
	}
}



//Fazer ponderacao no warning level baseado no numero total de ocorrencias e no warning entre cada uma delas