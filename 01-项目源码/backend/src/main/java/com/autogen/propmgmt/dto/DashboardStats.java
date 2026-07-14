package com.autogen.propmgmt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStats {
    private long buildingCount;
    private long unitCount;
    private long ownerCount;
    private long repairCount;
    private long unpaidFeeCount;
    private long pendingVisitorCount;
    private List<ChartItem> buildingDistribution = new ArrayList<>();
    private List<ChartItem> repairStatusDistribution = new ArrayList<>();
    private List<ChartItem> unitStatusDistribution = new ArrayList<>();
    private List<ChartItem> feePayStatusDistribution = new ArrayList<>();
}
