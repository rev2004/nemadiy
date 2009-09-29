import java.util.Date;


/**
 * @stereotype entity
 */

public class Flow {
	private Long id;
    private String name;
    private String description;
    private Date dateCreated;
    private String keyWords;
    private Boolean isTemplate;
    private String url;
    private Long creatorId;
    private Flow instanceOf;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	public String getKeyWords() {
		return keyWords;
	}
	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}
	public Boolean getIsTemplate() {
		return isTemplate;
	}
	public void setIsTemplate(Boolean isTemplate) {
		this.isTemplate = isTemplate;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Long getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}
	public Flow getInstanceOf() {
		return instanceOf;
	}
	public void setInstanceOf(Flow instanceOf) {
		this.instanceOf = instanceOf;
	}
    
    
}
