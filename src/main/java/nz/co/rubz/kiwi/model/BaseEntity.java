package nz.co.rubz.kiwi.model;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class BaseEntity implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@JsonIgnore(true)
	private ObjectId id;

	public ObjectId getId() {
		if (id == null ){
			return new ObjectId();
		}
		return id;
	}

	public void setId(final ObjectId id) {
		this.id = id;
	}
}
