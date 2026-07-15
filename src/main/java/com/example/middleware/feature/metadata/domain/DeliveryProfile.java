package com.example.middleware.feature.metadata.domain;

public class DeliveryProfile {

    private final String outputFolder;
    private final String filePrefix;
    private final String extension;
    private final String delimiter;
    private final boolean includeHeader;
    private final boolean includeTrailer;
    private final String trailerTemplate;

    public DeliveryProfile(
            String outputFolder,
            String filePrefix,
            String extension,
            String delimiter,
            boolean includeHeader,
            boolean includeTrailer,
            String trailerTemplate) {

        this.outputFolder = outputFolder;
        this.filePrefix = filePrefix;
        this.extension = extension;
        this.delimiter = delimiter;
        this.includeHeader = includeHeader;
        this.includeTrailer = includeTrailer;
        this.trailerTemplate = trailerTemplate;
    }

    public String getOutputFolder() {
        return outputFolder;
    }

    public String getFilePrefix() {
        return filePrefix;
    }

    public String getExtension() {
        return extension;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public boolean isIncludeHeader() {
        return includeHeader;
    }

    public boolean isIncludeTrailer() {
        return includeTrailer;
    }

    public String getTrailerTemplate() {
        return trailerTemplate;
    }
}