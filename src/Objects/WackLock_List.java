package Objects;

import java.util.ArrayList;

public class WackLock_List extends ArrayList<wakelockItem> {

	private static final long serialVersionUID = 9080435546773455435L;
	
	public int wlIndexOf(wakelockItem o) {
		wakelockItem wl;
		for(int i=0;i<this.size();i++){
			wl = this.get(i);
			if( wl.getLock().equals(o.getLock()) && wl.getTag().equals(o.getTag()) )
			{
				return i;
			}
		}
		return -1;
	}
	
	public int wlIndexOf_v2(wakelockItem o) {
		wakelockItem wl;
		for(int i=0;i<this.size();i++){
			wl = this.get(i);
			if (wl.getTag().equals(o.getTag()) && ( wl.getProcess().equals(o.getProcess()) ||  wl.getUid().equals(o.getUid()) ) )
			{
				return i;
			}
		}
		return -1;
	}
	
	public int wlMatchUidTag(String uid, String tag) {
		wakelockItem wl;
		for(int i=0;i<this.size();i++){
			wl = this.get(i);
			if( wl.getUid().equals(uid) && wl.getTag().equals(tag) )
			{
				return i;
			}
		}
		return -1;
	}	
}
