package BusinessAnalysis.system.service;

import BusinessAnalysis.system.model.TrendData;
import BusinessAnalysis.system.repository.TrendRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrendService {
    @Autowired
    private TrendRepository trendRepository;

    public List<TrendData> getAllTrends() {
        return trendRepository.findAll();
    }

}
