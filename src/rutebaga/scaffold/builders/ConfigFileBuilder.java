package rutebaga.scaffold.builders;

import java.util.HashMap;
import java.util.Map;

import rutebaga.scaffold.Builder;
import rutebaga.scaffold.MasterScaffold;

public abstract class ConfigFileBuilder implements Builder, ReaderProcessor
{
	public String[] availableIds()
	{
		return properties.keySet().toArray(new String[0]);
	}

	private Map<String, Map<String, String>> properties = new HashMap<String, Map<String, String>>();

	private Map<String, String> current;

	public ConfigFileBuilder()
	{
		String path = getDefaultFileName();
		new FileProcessor().execute(this, path);
	}

	public void processLine(String readData)
	{
		String[] tokens = readData.split(" ");
		if (tokens == null)
			return;
		if (tokens.length == 0)
			return;
		if (tokens.length == 1)
		{
			current = new HashMap<String, String>();
			properties.put(tokens[0], current);
		}
		else
		{
			current.put(tokens[0], tokens[1]);
		}
	}

	public Map<String, Map<String, String>> getProperties()
	{
		return properties;
	}

	protected Object getObjectFor(String id, String property, MasterScaffold scaffold)
	{
		return scaffold.get(properties.get(id).get(property));
	}

	protected abstract String getDefaultFileName();

	protected String getProperty(String id, String property)
	{
		return properties.get(id).get(property);
	}

	public Map<String, String> getMap(String id)
	{
		return properties.get(id);
	}
}