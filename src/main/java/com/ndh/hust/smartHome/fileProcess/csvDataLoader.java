package com.ndh.hust.smartHome.fileProcess;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class csvDataLoader {
    public List<String[]> readAll(Reader reader) throws Exception {
        CSVParser csvParser = new CSVParserBuilder()
                .withSeparator(',')
                .withIgnoreQuotations(true)
                .build();

        CSVReader csvReader = new CSVReaderBuilder(reader)
                .withSkipLines(0)
                .withCSVParser(csvParser)
                .build();

        List<String[]> list = csvReader.readAll();
        reader.close();
        csvReader.close();
        return list;
    }

    public String readAllExample() throws Exception {
        Reader reader = Files.newBufferedReader(Paths.get("/home/hoang/workspace/smartHome/src/main/resources/sample/precipitation.csv"));
        return readAll(reader).toString();
    }

    public List<String[]> oneByOne(Reader reader) throws Exception {
        List<String[]> list = new ArrayList<>();
        CSVReader csvReader = new CSVReader(reader);
        String[] line;
        while ((line = csvReader.readNext()) != null) {
            list.add(line);
        }
        reader.close();
        csvReader.close();
        return list;
    }

    public String oneByOneExample() throws Exception {
        Reader reader = Files.newBufferedReader(Paths.get(ClassLoader.getSystemResource("sample/precipitation.csv").getPath()));
        return oneByOne(reader).toString();
    }

}
