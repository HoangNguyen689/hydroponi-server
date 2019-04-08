package com.ndh.hust.smartHome.fileProcess.pojos;

import com.ndh.hust.smartHome.fileProcess.beans.CSVBean;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
public class CSVTransfer {

    private List<CSVBean> csvList;

    public List<CSVBean> getCsvList() {
        if (csvList != null) {
            return csvList;
        }
        return new ArrayList<CSVBean>();
    }
}
