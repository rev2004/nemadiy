package org.imirsel.nema.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Proxy;

/**
 * Represents a Meandre flow.
 * 
 * @author shirk
 * @since 0.4.0
 */
@Entity
@Table(name = "flow")
@Proxy(lazy = false)
public class Flow implements Serializable {

    public enum FlowType {

        UNKNOWN("Unknown"), INHERITS("Inherits"), FEATURE_EXTRACTION("Feature Extraction"), CLASSIFICATION(
        "Classification"), EVALUATION("Evaluation"), ANALYSIS(
        "Analysis");
        String name;

        private FlowType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }

        public static FlowType toFlowType(String typeName) {
            if (typeName.equals(INHERITS.getName())) {
                return INHERITS;
            } else if (typeName.equals(FEATURE_EXTRACTION.getName())) {
                return FEATURE_EXTRACTION;
            } else if (typeName.equals(CLASSIFICATION.getName())) {
                return CLASSIFICATION;
            } else if (typeName.equals(EVALUATION.getName())) {
                return EVALUATION;
            } else if (typeName.equals(ANALYSIS.getName())) {
                return ANALYSIS;
            } else {
                return UNKNOWN;
            }
        }
    }
    /**
     * Version of this class.
     */
    private static final long serialVersionUID = 8013326562531128503L;
    private Long id;
    private String name;
    private String description;
    private Date dateCreated = new Date();
    private String keyWords;
    private Boolean template = false;
    private String typeName;
    private String uri;
    private Long creatorId;
    private String submissionCode;
    private Flow instanceOf = null;
    private Long taskId;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "description", nullable = false, length = 10000)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "dateCreated", nullable = false)
    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Column(name = "keyWords", nullable = false)
    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    @Column(name = "isTemplate", nullable = false)
    public Boolean isTemplate() {
        return template;
    }

    public void setTemplate(Boolean template) {
        this.template = template;
    }

    @Column(name = "typeName", nullable = false)
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Transient
    public FlowType getType() {
        return FlowType.toFlowType(typeName);
    }

    public void setType(FlowType flowType) {
        this.typeName = flowType.getName();
    }

    @Column(name = "uri", nullable = false, length = 20000000)
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Column(name = "creatorId", nullable = false)
    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public void setSubmissionCode(String submissionCode) {
        this.submissionCode = submissionCode;
    }

    @Column(name = "submissionCode", nullable = true)
    public String getSubmissionCode() {
        return submissionCode;
    }

    @JoinColumn(name = "instanceOf", nullable = true)
    @ManyToOne(fetch = FetchType.EAGER)
    public Flow getInstanceOf() {
        return instanceOf;
    }

    public void setInstanceOf(Flow instanceOf) {
        this.instanceOf = instanceOf;
    }

    @Column(name="taskId")
    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Flow other = (Flow) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }
}
