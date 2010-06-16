package org.imirsel.nema.propertyeditors;

import java.beans.PropertyEditorSupport;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.hibernate.collection.PersistentMap;
import org.springframework.core.CollectionFactory;

public class CustomMapPropertyEditor extends PropertyEditorSupport
{

	private final Class mapType;

	public CustomMapPropertyEditor(Class mapType)
	{
		if (mapType == null)
		{
			throw new IllegalArgumentException("Map type is required");
		}
		if (!Set.class.isAssignableFrom(mapType))
		{
			if (!Map.class.isAssignableFrom(mapType))
			{
				throw new IllegalArgumentException("Map type [" + mapType.getName() + "] does not implement [java.util.Map]");
			}
		}
		this.mapType = mapType;
	}

	@SuppressWarnings("unchecked")
	public void setValue(Object value)
	{
		if (value == null)
		{
			super.setValue(createMap(this.mapType, 0));
		}

		else if (this.mapType.isInstance(value))
		{
			super.setValue(value);
		}
		else if (value instanceof PersistentMap)
		{
			Map source = (PersistentMap) value;
			Map target = createMap(this.mapType, source.size());

			target.putAll((Map) convertElement(value));

			super.setValue(target);
		}
		else if (value instanceof Map)
		{
			Map source = (Map) value;
			Map target = createMap(this.mapType, source.size());

			target.putAll((Map) convertElement(source));

			super.setValue(target);
		}
		else if (value.getClass().isArray())
		{
			Map target = (Map) convertElement(value);

			super.setValue(target);
		}
		else
		{
			Map target = createMap(this.mapType, 1);
			Map convertedSource = (Map) convertElement(value);

			target.putAll(convertedSource);

			super.setValue(target);
		}
	}

	protected Map createMap(Class collectionType, int initialCapacity)
	{
		if (!collectionType.isInterface())
		{
			try
			{
				return (Map) collectionType.newInstance();
			}
			catch (Exception ex)
			{
				throw new IllegalArgumentException("Could not instantiate map class [" + collectionType.getName() + "]: " + ex.getMessage());
			}
		}
		else
		{
			return CollectionFactory.createLinkedMapIfPossible(initialCapacity);
		}
	}

	@SuppressWarnings("unchecked")
	protected Object convertElement(Object element)
	{
		Map<String, String> aux = new HashMap<String, String>(0);

		if (element.getClass().isArray())
		{
			int tamanhoLista;
			tamanhoLista = Array.getLength(element);
			aux = new HashMap<String, String>(tamanhoLista);

			StringTokenizer st;
			for (int i = 0; i < tamanhoLista; i++)
			{
				st = new StringTokenizer(String.valueOf(Array.get(element, i)), "#");
				aux.put(st.nextToken(), st.nextToken());
			}
		}
		else if (element instanceof String)
		{
			aux = new HashMap<String, String>(1);
			StringTokenizer st = new StringTokenizer((String)element, "#");
			aux.put(st.nextToken(), st.nextToken());
		}
		else
		{
			Class[] classes = element.getClass().getInterfaces();
			for (Class class1 : classes)
			{
				if (class1.equals(Map.class))
				{
					aux.putAll( (Map<String, String>) element );
				}

			}
		}

		return aux;
	}

	public void setAsText(String text) throws IllegalArgumentException
	{
		setValue(text);
	}

	public String getAsText()
	{
		return null;
	}

}