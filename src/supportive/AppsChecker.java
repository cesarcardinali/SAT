package supportive;


import java.util.ArrayList;


public class AppsChecker
{
	static ArrayList<String> audioAppsList;
	static ArrayList<String> gpsAppsList;
	static ArrayList<String> gamesList;
	
	public static void init()
	{
		audioAppsList.add("mediaserver");
		audioAppsList.add("tunein");
		audioAppsList.add("slacker");
		audioAppsList.add("pandora");
		audioAppsList.add("sirius");
		audioAppsList.add("android.music");
		audioAppsList.add("saavn");
		audioAppsList.add("com.audible.application");
		audioAppsList.add("spotify");
		audioAppsList.add("fmradio");
		
		gpsAppsList.add("runtastic");
		gpsAppsList.add("runkeeper");
		gpsAppsList.add("pedometer");
		gpsAppsList.add("plusgps");
		gpsAppsList.add("cc.pacer.androidapp");
		gpsAppsList.add("com.stt.android");
		

		gamesList.add("com.supercell.boombeach");
		gamesList.add("com.supercell.clashofclans");
		gamesList.add("com.blizzard.wtcg.hearthstone");
	}
	
	public boolean isAudioApp(String process)
	{
		for (String app : audioAppsList)
		{
			if(process.contains(app))
			{
				return true;
			}
		}
		
		return false;
	}
}
