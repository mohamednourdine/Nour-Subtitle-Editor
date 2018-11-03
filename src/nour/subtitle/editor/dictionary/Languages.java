package nour.subtitle.editor.dictionary;

public enum Languages {
    ENGLISH("en"), TURKISH("tr"), AUTO("auto"), NOTSET("null");

    private final String value;

    Languages(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return this.value;
    }
}
