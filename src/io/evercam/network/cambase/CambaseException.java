package io.evercam.network.cambase;

import org.json.JSONException;

import com.mashape.unirest.http.exceptions.UnirestException;

public class CambaseException extends Exception
{
	public CambaseException(String message)
	{
		super(message);
	}
	
	public CambaseException(UnirestException e)
	{
		super(e);
	}
	
	public CambaseException(JSONException e)
	{
	    super(e);
    }

}
