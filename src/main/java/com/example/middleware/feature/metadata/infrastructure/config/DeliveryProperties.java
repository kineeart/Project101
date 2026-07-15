package com.example.middleware.feature.metadata.infrastructure.config;

public class DeliveryProperties {

    private String outputFolder;
    private String filePrefix;
    private String extension;
    private String delimiter;
    private boolean includeHeader;
    private boolean includeTrailer;
    private String trailerTemplate;

    public String getOutputFolder() {
        return outputFolder;
    }

    public void setOutputFolder(String outputFolder) {
        this.outputFolder = outputFolder;
    }

    public String getFilePrefix() {
        return filePrefix;
    }

    public void setFilePrefix(String filePrefix) {
        this.filePrefix = filePrefix;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public boolean isIncludeHeader() {
        return includeHeader;
    }

    public void setIncludeHeader(boolean includeHeader) {
        this.includeHeader = includeHeader;
    }

    public boolean isIncludeTrailer() {
        return includeTrailer;
    }

    public void setIncludeTrailer(boolean includeTrailer) {
        this.includeTrailer = includeTrailer;
    }

    public String getTrailerTemplate() {
        return trailerTemplate;
    }

    public void setTrailerTemplate(String trailerTemplate) {
        this.trailerTemplate = trailerTemplate;
    }
}