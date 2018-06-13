package edu.jonathan.lookforcardprices.comom;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CSVLineBuilder {
    private List<String> values;

    private CSVFileBuilder csvFileBuilder;

    protected CSVLineBuilder(CSVFileBuilder csvFileBuilder){
        this.csvFileBuilder = csvFileBuilder;
        values = new ArrayList<>();
    }

    public CSVLineBuilder append(Object value) {
        return append( String.valueOf(value) );
    }

    public CSVLineBuilder append(String value) {
        values.add( String.format("\"%s\"", value ) );
        return this;
    }

    public CSVFileBuilder buildLine(){
        csvFileBuilder.addLine( values );
        return csvFileBuilder;
    }
}
