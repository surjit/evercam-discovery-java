package io.evercam.network;

import io.evercam.Auth;
import io.evercam.Defaults;
import io.evercam.EvercamException;
import io.evercam.Model;
import io.evercam.Vendor;
import io.evercam.network.discovery.DiscoveredCamera;
import io.evercam.network.query.EvercamQuery;

public abstract class EvercamQueryRunnable implements Runnable
{
	private boolean withDefaults = false;
	private DiscoveredCamera discoveredCamera;

	public EvercamQueryRunnable(DiscoveredCamera discoveredCamera)
	{
		this.discoveredCamera = discoveredCamera;
	}

	@Override
	public void run()
	{
		try
		{
			if (withDefaults)
			{
				EvercamDiscover.printLogMessage("Retrieving defaults for camera "
						+ discoveredCamera.getIP());
				Model defaultModel = Vendor.getById(discoveredCamera.getVendor()).getDefaultModel();
				Defaults defaults = defaultModel.getDefaults();
				String username = defaults.getAuth(Auth.TYPE_BASIC).getUsername();
				String password = defaults.getAuth(Auth.TYPE_BASIC).getPassword();
				String jpgUrl = defaults.getJpgURL();
				String h264Url = defaults.getH264URL();
				
				discoveredCamera.setUsername(username);
				discoveredCamera.setPassword(password);
				discoveredCamera.setJpg(jpgUrl);
				discoveredCamera.setH264(h264Url);
				
				discoveredCamera.setVendorThumbnail(EvercamQuery
						.getVendorThumbnailUrl(discoveredCamera.getVendor()));
				
				if(discoveredCamera.hasModel())
				{
					discoveredCamera.setModelThumbnail(EvercamQuery
							.getModelThumbnailUrl(discoveredCamera.getModel()));
				}
				
				if(!discoveredCamera.hasModelThumbnailUrl())
				{
					discoveredCamera.setModelThumbnail(defaultModel.getThumbnailUrl());
				}
			}
		}
		catch (EvercamException e)
		{
			if (Constants.ENABLE_LOGGING)
			{
				e.printStackTrace();
			}
		}

		onFinished();
	}

	public EvercamQueryRunnable withDefaults(boolean withDefaults)
	{
		this.withDefaults = withDefaults;
		return this;
	}

	public abstract void onFinished();
}
