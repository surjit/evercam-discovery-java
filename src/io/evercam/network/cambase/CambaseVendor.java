package io.evercam.network.cambase;

import org.json.JSONException;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class CambaseVendor
{
	private final String URL = "http://www.cambase.io:80/api/v1/vendors";
	private JSONObject jsonObject;

	public CambaseVendor(String id) throws CambaseException
	{
		try
		{
			HttpResponse<JsonNode> response = Unirest.get(URL + '/' + id).asJson();
			if (response.getCode() == 200)
			{
				JSONObject manufactureObject = response.getBody().getObject();
				this.jsonObject = manufactureObject.getJSONObject("data");
			}
		}

		catch (UnirestException e)
		{
			throw new CambaseException(e);
		}
		catch (JSONException e)
		{
			throw new CambaseException(e);
		}
	}

	public String getLogoUrl()
	{
		if (jsonObject != null)
		{
			try
			{
				return jsonObject.getString("logo");
			}
			catch (JSONException e)
			{
				return "";
			}
		}
		return "";
	}
}
