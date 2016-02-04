package tv.amwa.maj.meta.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import tv.amwa.maj.industry.MediaSetAdd;
import tv.amwa.maj.industry.MediaPropertySetter;
import tv.amwa.maj.industry.MetadataObject;

/**
 * <p>Collection of Java methods required to manipulate a {@linkplain PropertyValue property value}
 * of an {@linkplain TypeDefinitionImpl AAF type}. Implementations of this class scan a set of 
 * methods to see if any are annotated with AAF-specific annotations 
 * ({@link MediaPropertySetter}, {@link MediaSetAdd} etc.). If they are, methods of this class allow
 * invocation of those methods.</p>
 * 
 *
 *
 */
public abstract class MethodBag {

	private Method getter;
	private String propertyName;
	
	/**
	 * <p>Create a method bag from a given getter method for a property value, a 
	 * set of candidate manipulation methods and the defined name of the related 
	 * property.</p>
	 * 
	 * @param getter Getter method used to retrieve the property value.
	 * @param candidateMethods List of candidate manipulation methods.
	 * @param propertyName Name of the property this method bag relates to.
	 */
	public MethodBag(
			Method getter,
			Method[] candidateMethods,
			String propertyName) {
		
		this.getter = getter;
		this.propertyName = propertyName;
	}
	
	public Object get(
			MetadataObject mdObject) 
		throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		
		return getter.invoke(mdObject);
	}
	
	public String getPropertyName() {
		
		return propertyName;
	}
	
	public String getGetterName() {
		
		return getter.getName();
	}
}
