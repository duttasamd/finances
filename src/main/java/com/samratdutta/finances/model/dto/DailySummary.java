package com.samratdutta.finances.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.samratdutta.finances.model.Expenditure;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
public class DailySummary {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @JsonIgnore
    private String  dateStr;
    private Double total;
    private Double closingBalance;

    public LocalDate getDate() {
        if(this.date == null && dateStr != null) {
            this.date = LocalDate.parse(dateStr);
        }
        return this.date;
    }
}
