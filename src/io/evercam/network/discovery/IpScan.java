package io.evercam.network.discovery;

import io.evercam.network.Constants;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class IpScan
{
	private ScanResult scanResult;
	public static final int DEFAULT_TIME_OUT = 2500;
	public static final int DEFAULT_FIXED_POOL = 40;
	public ExecutorService pool;
	private int pt_move = 2; // 1=backward 2=forward

	public IpScan(ScanResult scanResult)
	{
		this.scanResult = scanResult;
	}

	public void scanAll(ScanRange scanRange)
	{
		long ip = scanRange.getRouterIp();
		long start = scanRange.getScanStart();
		long end = scanRange.getScanEnd();
		pool = Executors.newFixedThreadPool(DEFAULT_FIXED_POOL);
		if (ip <= end && ip >= start)
		{
			launch(start);

			long pt_backward = ip;
			long pt_forward = ip + 1;
			long size_hosts = scanRange.size() - 1;

			for (int i = 0; i < size_hosts; i++)
			{
				// Set pointer if of limits
				if (pt_backward <= start)
				{
					pt_move = 2;
				}
				else if (pt_forward > end)
				{
					pt_move = 1;
				}
				// Move back and forth
				if (pt_move == 1)
				{
					launch(pt_backward);
					pt_backward--;
					pt_move = 2;
				}
				else if (pt_move == 2)
				{
					launch(pt_forward);
					pt_forward++;
					pt_move = 1;
				}
			}
		}
		else
		{
			for (long i = start; i <= end; i++)
			{
				launch(i);
			}
		}
		pool.shutdown();
		try
		{
			if (!pool.awaitTermination(3600, TimeUnit.SECONDS))
			{
				pool.shutdownNow();
			}
		}
		catch (InterruptedException e)
		{
			pool.shutdownNow();
			Thread.currentThread().interrupt();
		}
	}

	private void launch(long i)
	{
		if (!pool.isShutdown())
		{
			pool.execute(new SingleRunnable(IpTranslator.getIpFromLongUnsigned(i), scanResult));
		}
	}

	public void scanSingleIp(String ip, int timeout)
	{
		try
		{
			InetAddress h = InetAddress.getByName(ip);
			if (h.isReachable(timeout))
			{
				scanResult.onActiveIp(ip);
			}
		}
		catch (UnknownHostException e)
		{
			if(Constants.ENABLE_LOGGING)
			{
				e.printStackTrace();
			}
		}
		catch (IOException e)
		{
			if(Constants.ENABLE_LOGGING)
			{
				e.printStackTrace();
			};
		}
		scanResult.onIpScanned(ip);
	}

	private class SingleRunnable implements Runnable
	{
		private String ip;

		SingleRunnable(String ip, ScanResult scanResult)
		{
			this.ip = ip;
		}

		@Override
		public void run()
		{
			scanSingleIp(ip, DEFAULT_TIME_OUT);
		}

	}
}
