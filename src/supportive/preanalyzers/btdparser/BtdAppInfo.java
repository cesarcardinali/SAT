package supportive.preanalyzers.btdparser;


public class BtdAppInfo
{
	private String name;
	private long   initialCpu;
	private long   initialRx;
	private long   initialTx;
	private long   deltaCpuTime;
	private long   deltaRx;
	private long   deltaTx;
	
	public BtdAppInfo(String nome, long cpu, long rx, long tx)
	{
		name = nome;
		initialCpu = cpu;
		initialRx = rx;
		initialTx = tx;
		deltaCpuTime = 0;
		deltaRx = 0;
		deltaTx = 0;
	}
	
	public void update(long cpu, long rx, long tx)
	{
		deltaCpuTime = cpu - initialCpu;
		deltaRx = rx - initialRx;
		deltaTx = tx - initialTx;
	}
	
	// Getters and Setters
	public String getName()
	{
		return name;
	}

	public long getDeltaCpuTime()
	{
		return deltaCpuTime;
	}

	public long getDeltaRx()
	{
		return deltaRx;
	}

	public long getDeltaTx()
	{
		return deltaTx;
	}
}
