package io.evercam.network.cambase;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class CambaseModel
{
	private final String URL = "http://www.cambase.io:80/api/v1/models";
	private JSONObject jsonObject;

	public CambaseModel(String modelId) throws CambaseException
	{
		try
		{
			HttpResponse<JsonNode> response = Unirest.get(URL + '/' + modelId).asJson();
			if (response.getCode() == 200)
			{
				JSONObject modelJsonObject = response.getBody().getObject()
						.getJSONObject("models");
				this.jsonObject = modelJsonObject;
			}
			else
			{
				throw new CambaseException(response.getCode() + " " + response.getBody().toString());
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

	public ArrayList<String> getThumnailUrls() throws CambaseException
	{
		ArrayList<String> urlArray = new ArrayList<String>();
		if (jsonObject != null)
		{
			try
			{
				JSONArray urlJsonArray = jsonObject.getJSONArray("images");
				if (urlJsonArray.length() > 0)
				{
					for (int index = 0; index < urlJsonArray.length(); index++)
					{
						urlArray.add(urlJsonArray.getJSONObject(index).getString("url"));
					}
				}
			}
			catch (JSONException e)
			{
				throw new CambaseException(e);
			}
		}
		return urlArray;
	}
}
