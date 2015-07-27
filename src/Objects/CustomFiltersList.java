package Objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CustomFiltersList extends ArrayList<CustomFilterItem>{

	private static final long serialVersionUID = -170544270549969807L;

	public CustomFiltersList() {
		super();
	}
	
	public int indexOf(CustomFilterItem obj){
		for(int i=0;i<this.size();i++){
			if(obj.getName().equals(this.get(i).getName())){
				return i;
			}
		}
		return -1;
	}
	
	public int indexOf(String name){
		for(int i=0;i<this.size();i++){
			if( name.equals( this.get(i).getName())){
				return i;
			}
		}
		return -1;
	}
	
	
	public class itensComparator implements Comparator<CustomFilterItem> {
	    public int compare(CustomFilterItem p1, CustomFilterItem p2) {
	        return (int) p1.getName().compareTo(p2.getName());
	    }
	}
	
	public void sortItens(){
		Collections.sort(this, new itensComparator());
		Collections.reverse(this);
	}
}
