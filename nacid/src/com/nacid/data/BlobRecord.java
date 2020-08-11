package com.nacid.data;

import com.nacid.data.annotations.Column;
import com.nacid.data.annotations.Table;
import com.nacid.data.common.BinaryStream;

import java.sql.Timestamp;

/**
 * Created by georgi.georgiev on 02.02.2015.
 */
@Table(name = "blobs.blobs", sequence = "blobs_id_seq")
public class BlobRecord {
    private int id;
    private String filename;
    private String contentType;
    private int filesize;
    private BinaryStream content;
    @Column(name="created_date")
    private Timestamp dateCreated;
    @Column(name="rudi_user_created")
    private Integer userCreated;
    @Column(name = "file_location")
    private String fileLocation;

    public BlobRecord() {
    }

    public BlobRecord(int id, String filename, String contentType, int filesize, BinaryStream content, Timestamp dateCreated, Integer userCreated, String fileLocation) {
        this.id = id;
        this.filename = filename;
        this.contentType = contentType;
        this.filesize = filesize;
        this.content = content;
        this.dateCreated = dateCreated;
        this.userCreated = userCreated;
        this.fileLocation = fileLocation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getFilesize() {
        return filesize;
    }

    public void setFilesize(int filesize) {
        this.filesize = filesize;
    }

    public BinaryStream getContent() {
        return content;
    }

    public void setContent(BinaryStream content) {
        this.content = content;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Integer getUserCreated() {
        return userCreated;
    }

    public void setUserCreated(Integer userCreated) {
        this.userCreated = userCreated;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }
}
