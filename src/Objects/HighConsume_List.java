package Objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HighConsume_List extends ArrayList<highConsumeItem>{

	private static final long serialVersionUID = -170544270549969807L;

	public HighConsume_List() {
		super();
	}
	
	public int indexOf(highConsumeItem obj){
		for(int i=0;i<this.size();i++){
			if(obj.getPID().equals(this.get(i).getPID()))
			{
				return i;
			}
		}
		return -1;
	}
	
	public int indexOf(String proc){
		for(int i=0;i<this.size();i++){
			if( proc.equals( this.get(i).getProcess() ) )
			{
				return i;
			}
		}
		return -1;
	}
	
	
	public class itensComparator implements Comparator<highConsumeItem> {
	    public int compare(highConsumeItem p1, highConsumeItem p2) {
	        return (int) (Float.compare(p1.getOccurencesTotal(), p2.getOccurencesTotal()));
	    }
	}
	
	public void sortItens(){
		Collections.sort(this, new itensComparator());
		Collections.reverse(this);
	}
}
