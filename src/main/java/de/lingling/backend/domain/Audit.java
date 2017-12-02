package de.lingling.backend.domain;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Audit implements Serializable {

    private static final long serialVersionUID = -8938796362769926071L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, name = Columns.AUDIT_ALEXA_ID)
    private String alexaId;

    @Column(nullable = false, name = Columns.AUDIT_ACTION)
    private Action action;

    @Column(nullable = true, name = Columns.AUDIT_RETURNED_SENTENCE)
    private String returnedValue;

    @Column(nullable = true, name = Columns.AUDIT_UTTERANCE)
    private String utterance;

    @Column(nullable = false, name = Columns.AUDIT_TIMESTAMP)
    private Instant timestamp;

    public Audit() {
    }

    public Audit(final String alexaId, final String utterance, final Action action) {
        this.alexaId = alexaId;
        this.utterance = utterance;
        this.action = action;
        timestamp = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public String getAlexaId() {
        return alexaId;
    }

    public void setAlexaId(final String alexaId) {
        this.alexaId = alexaId;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(final Action action) {
        this.action = action;
    }

    public String getUtterance() {
        return utterance;
    }

    public void setUtterance(final String utterance) {
        this.utterance = utterance;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getReturnedValue() {
        return returnedValue;
    }

    public void setReturnedValue(final String returnedValue) {
        this.returnedValue = returnedValue;
    }
}
