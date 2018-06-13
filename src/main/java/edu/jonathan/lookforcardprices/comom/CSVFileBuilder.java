package edu.jonathan.lookforcardprices.comom;

import java.util.ArrayList;
import java.util.List;

public class CSVFileBuilder {
    private List<List<String>> lines;

    private char delimiter = ',';
    private long maxNumberOfColumns = 0;

    private CSVFileBuilder(){
        lines = new ArrayList<>();
    }

    public static CSVFileBuilder createCSVFile(){
        return new CSVFileBuilder();
    }

    public char getDelimiter(){
        return delimiter;
    }

    public long getMaxColumns(){
        return maxNumberOfColumns;
    }

    public CSVFileBuilder withDelimiter( char delimiter ){
        this.delimiter = delimiter;
        return this;
    }

    public CSVLineBuilder createLine(){
        return new CSVLineBuilder(this);
    }

    protected void addLine(List<String> line) {
        maxNumberOfColumns = Math.max(maxNumberOfColumns, line.size());
        lines.add(line);
    }

    public String build(){
        StringBuilder builder = new StringBuilder();
        lines.stream().forEach( line ->
            builder.append( Strings.implodeListIntoString(line, delimiter) ).append("\n")
        );
        return builder.toString();
    }
}
