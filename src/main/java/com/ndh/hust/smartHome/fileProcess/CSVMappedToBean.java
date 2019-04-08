package com.ndh.hust.smartHome.fileProcess;

import com.ndh.hust.smartHome.fileProcess.beans.CSVBean;
import com.ndh.hust.smartHome.fileProcess.beans.Precipitation;
import com.ndh.hust.smartHome.fileProcess.pojos.CSVTransfer;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class CSVMappedToBean {

    public List<CSVBean> beanBuilder(Path path, Class clazz) throws Exception {
        CSVTransfer csvTransfer = new CSVTransfer();
        ColumnPositionMappingStrategy mappingStrategy = new ColumnPositionMappingStrategy();
        mappingStrategy.setType(clazz);

        Reader reader = Files.newBufferedReader(path);
        CsvToBean csvToBean = new CsvToBeanBuilder(reader)
                .withType(clazz)
                .withSkipLines(1)
                .withMappingStrategy(mappingStrategy)
                .withIgnoreLeadingWhiteSpace(true)
                .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
                .withIgnoreQuotations(true)
                .build();
        csvTransfer.setCsvList(csvToBean.parse());
        reader.close();
        return csvTransfer.getCsvList();
    }
}
