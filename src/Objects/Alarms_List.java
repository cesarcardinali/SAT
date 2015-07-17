package Objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Alarms_List extends ArrayList<AlarmItem>{
	
	private static final long serialVersionUID = 9080435546773455435L;
	
	public int alarmIndexOf(AlarmItem o) {
		AlarmItem wl;
		for(int i=0;i<this.size();i++){
			wl = this.get(i);
			if( wl.getProcess().equals(o.getProcess()) && wl.getType().equals(o.getType()) &&  wl.getAction().equals(o.getAction()))
			{
				return i;
			}
		}
		return -1;
	}
	
	public class itensComparator implements Comparator<AlarmItem> {
	    public int compare(AlarmItem p1, AlarmItem p2) {
	        return (int) (Integer.compare(p1.getOccurences(), p2.getOccurences()));
	    }
	}
	
	public void sortItens(){
		Collections.sort(this, new itensComparator());
		Collections.reverse(this);
	}
}
