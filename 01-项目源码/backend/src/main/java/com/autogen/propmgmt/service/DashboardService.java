package com.autogen.propmgmt.service;

import com.autogen.propmgmt.dto.ChartItem;
import com.autogen.propmgmt.dto.DashboardStats;
import com.autogen.propmgmt.entity.PropBuilding;
import com.autogen.propmgmt.entity.PropUnit;
import com.autogen.propmgmt.entity.PropertyFee;
import com.autogen.propmgmt.entity.RepairRequest;
import com.autogen.propmgmt.entity.VisitorRecord;
import com.autogen.propmgmt.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final PropBuildingRepository buildingRepository;
    private final PropUnitRepository unitRepository;
    private final OwnerRepository ownerRepository;
    private final RepairRequestRepository repairRequestRepository;
    private final PropertyFeeRepository propertyFeeRepository;
    private final VisitorRecordRepository visitorRecordRepository;

    public DashboardStats getStats() {
        DashboardStats stats = new DashboardStats();
        stats.setBuildingCount(buildingRepository.count());
        stats.setUnitCount(unitRepository.count());
        stats.setOwnerCount(ownerRepository.count());
        stats.setRepairCount(repairRequestRepository.count());
        stats.setUnpaidFeeCount(propertyFeeRepository.findAll().stream()
                .filter(f -> "UNPAID".equals(f.getPayStatus())).count());
        stats.setPendingVisitorCount(visitorRecordRepository.findAll().stream()
                .filter(v -> "PENDING".equals(v.getStatus())).count());
        stats.setBuildingDistribution(buildBuildingDistribution());
        stats.setRepairStatusDistribution(buildRepairStatusDistribution());
        stats.setUnitStatusDistribution(buildUnitStatusDistribution());
        stats.setFeePayStatusDistribution(buildFeePayStatusDistribution());
        return stats;
    }

    private List<ChartItem> buildBuildingDistribution() {
        Map<Long, String> names = buildingRepository.findAll().stream()
                .collect(Collectors.toMap(PropBuilding::getId, PropBuilding::getBuildingName));
        Map<String, Long> map = new LinkedHashMap<>();
        for (PropUnit unit : unitRepository.findAll()) {
            String name = names.getOrDefault(unit.getBuildingId(), "UNKNOWN_BUILDING");
            map.merge(name, 1L, Long::sum);
        }
        return map.entrySet().stream()
                .map(e -> new ChartItem(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    private List<ChartItem> buildRepairStatusDistribution() {
        Map<String, Long> map = new LinkedHashMap<>();
        for (RepairRequest req : repairRequestRepository.findAll()) {
            String status = StringUtils.hasText(req.getStatus()) ? req.getStatus() : "UNKNOWN";
            map.merge(status, 1L, Long::sum);
        }
        return map.entrySet().stream()
                .map(e -> new ChartItem(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    private List<ChartItem> buildUnitStatusDistribution() {
        long occupied = unitRepository.findAll().stream().filter(u -> u.getStatus() != null && u.getStatus() == 1).count();
        long vacant = unitRepository.findAll().stream().filter(u -> u.getStatus() != null && u.getStatus() == 2).count();
        long decorating = unitRepository.findAll().stream().filter(u -> u.getStatus() != null && u.getStatus() == 0).count();
        return List.of(
                new ChartItem("OCCUPIED", occupied),
                new ChartItem("VACANT", vacant),
                new ChartItem("DECORATING", decorating)
        );
    }

    private List<ChartItem> buildFeePayStatusDistribution() {
        Map<String, Long> map = new LinkedHashMap<>();
        for (PropertyFee fee : propertyFeeRepository.findAll()) {
            String status = StringUtils.hasText(fee.getPayStatus()) ? fee.getPayStatus() : "UNKNOWN";
            map.merge(status, 1L, Long::sum);
        }
        return map.entrySet().stream()
                .map(e -> new ChartItem(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }
}
